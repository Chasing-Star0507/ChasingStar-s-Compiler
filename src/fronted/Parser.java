package fronted;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

public class Parser {
    private ArrayList<Token> tokens;
    private int pos = 0;
    private static final Set<TokenType> EXP_FIRST = EnumSet.of(
            TokenType.PLUS, TokenType.MINU, TokenType.NOT, TokenType.IDENFR,
            TokenType.LPARENT, TokenType.INTCON
    );

    //ToDo 每个检查一遍，注意括号的大小，不用弄错了！

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public CompUnit getCompUnit() {
        return parseComUnit();
    }

    private void handlerError(TokenType type) {
        int line = tokens.get(pos - 1).getLineNum();
        if (tokens.get(pos).getTokenType() == type) {
            pos++;
        } else {
            //System.out.println(tokens.get(pos).toString());
            switch (type) {
                case SEMICN -> ErrorHandler.addError(new ErrorToken(line, "i"));
                case RPARENT -> ErrorHandler.addError(new ErrorToken(line, "j"));
                case RBRACK -> ErrorHandler.addError(new ErrorToken(line, "k"));
            }
        }
    }

    private CompUnit parseComUnit() {
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();
        //ToDo Attention！！！判断条件！！！
        while (!(tokens.get(pos + 2).getTokenType() == TokenType.LPARENT && tokens.get(pos + 1).getTokenType() == TokenType.IDENFR)
                && tokens.get(pos + 1).getTokenType() != TokenType.MAINTK) {
            //System.out.println(1);
            decls.add(parseDecl());
        }
//        System.out.println(decls.size());
//        for(Decl decl : decls){
//            decl.print();
//        }
        while (tokens.get(pos + 1).getTokenType() != TokenType.MAINTK) {
            funcDefs.add(parseFuncDef());
        }
        //System.out.println(1);
        MainFuncDef mainFuncDef = parseMainFuncDef();
        return new CompUnit(decls, funcDefs, mainFuncDef);
    }

    private Decl parseDecl() {
        if (tokens.get(pos).getTokenType() == TokenType.CONSTTK) {
            return parseConstDecl();
        } else {
            return parseVarDecl();
        }
    }

    private ConstDecl parseConstDecl() {
        pos++;
        Btype btype = parseBtype();
        ArrayList<ConstDef> constDefs = new ArrayList<>();
        constDefs.add(parseConstDef());
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            constDefs.add(parseConstDef());
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        return new ConstDecl(btype, constDefs);
    }

    private Btype parseBtype() {
        TokenType tokenType = tokens.get(pos).getTokenType();
        pos++;
        return new Btype(tokenType);
    }

    private ConstDef parseConstDef() {
        Token ident = tokens.get(pos);
        pos++;
        ConstExp constExp = null;
        if (tokens.get(pos).getTokenType() == TokenType.LBRACK) {
            pos++;
            constExp = parseConstExp();
            //pos++;
            handlerError(TokenType.RBRACK);
        }
        pos++;
        ConstInitVal constInitval = parseConstInitVal();
        return new ConstDef(ident, constExp, constInitval);
    }

    private ConstExp parseConstExp() {
        AddExp addExp = parseAddExp();
        return new ConstExp(addExp);
    }

    private ConstInitVal parseConstInitVal() {
        int type = 0;
        ArrayList<ConstExp> constExps = new ArrayList<>();
        if (tokens.get(pos).getTokenType() == TokenType.LBRACE) {
            type = 1;
            pos++;
            if(tokens.get(pos).getTokenType() == TokenType.RBRACE){
                pos++;
                return new ConstInitVal(type,constExps);
            }
            constExps.add(parseConstExp());
            while (tokens.get(pos).getTokenType() != TokenType.RBRACE) {
                pos++;
                constExps.add(parseConstExp());
            }
            pos++;
        } else {
            constExps.add(parseConstExp());
        }
        return new ConstInitVal(type, constExps);
    }

    private VarDecl parseVarDecl() {
        int type = 0;
        if (tokens.get(pos).getTokenType() == TokenType.STATICTK) {
            type = 1;
            pos++;
        }
        Btype btype = parseBtype();
        //btype.print();
        ArrayList<VarDef> varDefs = new ArrayList<>();
        varDefs.add(parseVarDef());
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            varDefs.add(parseVarDef());
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        return new VarDecl(type, btype, varDefs);
    }

    private VarDef parseVarDef() {
        Token ident = tokens.get(pos);
        pos++;
        ConstExp constExp = null;
        if (tokens.get(pos).getTokenType() == TokenType.LBRACK) {
            pos++;
            constExp = parseConstExp();
            //pos++;
            handlerError(TokenType.RBRACK);
        }
        InitVal initVal = null;
        if (tokens.get(pos).getTokenType() == TokenType.ASSIGN) {
            pos++;
            initVal = parseInitVal();
        }
        return new VarDef(ident, constExp, initVal);
    }

    private InitVal parseInitVal() {
        int type = 0;
        ArrayList<Exp> exps = new ArrayList<>();
        if (tokens.get(pos).getTokenType() == TokenType.LBRACE) {
            type = 1;
            pos++;
            if(tokens.get(pos).getTokenType() == TokenType.RBRACE){
                pos++;
                return new InitVal(type,exps);
            }
            exps.add(parseExp());
            while (tokens.get(pos).getTokenType() != TokenType.RBRACE) {
                pos++;
                exps.add(parseExp());
            }
            pos++;
        } else {
            exps.add(parseExp());
        }
        return new InitVal(type, exps);
    }

    private Exp parseExp() {
        AddExp addExp = parseAddExp();
        return new Exp(addExp);
    }

    private FuncDef parseFuncDef() {
        FuncType funcType = parseFuncType();
        Token ident = tokens.get(pos);
        pos += 2;
        FuncFParams funcFParams = null;
        if (tokens.get(pos).getTokenType() == TokenType.INTTK) {
            funcFParams = parseFuncFParmas();
        }
        //pos++;
        handlerError(TokenType.RPARENT);
        Block block = parseBlock();
        return new FuncDef(funcType, ident, funcFParams, block);
    }

    private FuncType parseFuncType() {
        TokenType tokenType = tokens.get(pos).getTokenType();
        pos++;
        return new FuncType(tokenType);
    }

    private FuncFParams parseFuncFParmas() {
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        funcFParams.add(parseFuncFParma());
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            funcFParams.add(parseFuncFParma());
        }
        return new FuncFParams(funcFParams);
    }

    private FuncFParam parseFuncFParma() {
        int type = 0;
        Btype btype = parseBtype();
        Token ident = tokens.get(pos);
        pos++;
        if (tokens.get(pos).getTokenType() == TokenType.LBRACK) {
            type = 1;
            pos += 1;
            handlerError(TokenType.RBRACK);
        }
        return new FuncFParam(type, btype, ident);
    }

    private MainFuncDef parseMainFuncDef() {
        pos += 3;
        handlerError(TokenType.RPARENT);
        //System.out.println(tokens.get(pos));
        Block block = parseBlock();
        return new MainFuncDef(block);
    }

    private Block parseBlock() {
        pos++;
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (tokens.get(pos).getTokenType() != TokenType.RBRACE) {
            blockItems.add(parseBlockItem());
        }
        int endLineNum = tokens.get(pos).getLineNum();
        pos++;
        return new Block(blockItems, endLineNum);
    }

    private BlockItem parseBlockItem() {
        if (tokens.get(pos).getTokenType() == TokenType.CONSTTK || tokens.get(pos).getTokenType() == TokenType.INTTK
                || tokens.get(pos).getTokenType() == TokenType.STATICTK) {
            Decl decl = parseDecl();
            return new BlockItem(decl);
        } else {
            Stmt stmt = parseStmt();
            return new BlockItem(stmt);
        }
    }

    private Stmt parseStmt() {
        if (tokens.get(pos).getTokenType() == TokenType.IFTK) {
            return parseIfStmt();
        } else if (tokens.get(pos).getTokenType() == TokenType.FORTK) {
            return parseForStruct();
        } else if (tokens.get(pos).getTokenType() == TokenType.BREAKTK) {
            Token token = tokens.get(pos);
            pos += 1;
            handlerError(TokenType.SEMICN);
            return new BreakStmt(token);
        } else if (tokens.get(pos).getTokenType() == TokenType.CONTINUETK) {
            Token token = tokens.get(pos);
            pos += 1;
            handlerError(TokenType.SEMICN);
            return new ContinueStmt(token);
        } else if (tokens.get(pos).getTokenType() == TokenType.RETURNTK) {
            return parseReturnStmt();
        } else if (tokens.get(pos).getTokenType() == TokenType.PRINTFTK) {
            return parsePrintfStmt();
        } else if (tokens.get(pos).getTokenType() == TokenType.LBRACE) {
            Block block = parseBlock();
            return new BlockStmt(block);
        } else if (tokens.get(pos).getTokenType() == TokenType.IDENFR) {
            int tempos = pos;
            LVal lVal = parseLVal();
            if (tokens.get(pos).getTokenType() == TokenType.ASSIGN) {
                pos++;
                Exp exp = parseExp();
                //pos++;
                handlerError(TokenType.SEMICN);
                return new LValExpStmt(lVal, exp);
            }
            pos = tempos;
        }
        Exp exp = null;
        if (tokens.get(pos).getTokenType() != TokenType.SEMICN) {
            exp = parseExp();
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        return new ExpStmt(exp);
    }

    private IfStmt parseIfStmt() {
        pos += 2;
        Cond cond = parseCond();
        //pos++;
        handlerError(TokenType.RPARENT);
        ArrayList<Stmt> stmts = new ArrayList<>();
        stmts.add(parseStmt());
        if (tokens.get(pos).getTokenType() == TokenType.ELSETK) {
            pos++;
            stmts.add(parseStmt());
        }
        return new IfStmt(cond, stmts);
    }

    private ForStruct parseForStruct() {
        pos += 2;
        ForStmt forStmt1 = null;
        if (tokens.get(pos).getTokenType() != TokenType.SEMICN) {
            forStmt1 = parseForStmt();
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        Cond cond = null;
        if (tokens.get(pos).getTokenType() != TokenType.SEMICN) {
            cond = parseCond();
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        ForStmt forStmt2 = null;
        if (tokens.get(pos).getTokenType() == TokenType.IDENFR) {
            forStmt2 = parseForStmt();
        }
        //pos++;
        handlerError(TokenType.RPARENT);
        Stmt stmt = parseStmt();
        return new ForStruct(forStmt1, cond, forStmt2, stmt);
    }

    private ReturnStmt parseReturnStmt() {
        Token token = tokens.get(pos);
        pos++;
        Exp exp = null;
        if (tokens.get(pos).getTokenType() != TokenType.SEMICN) {
            exp = parseExp();
        }
        //pos++;
        handlerError(TokenType.SEMICN);
        return new ReturnStmt(token, exp);
    }

    private PrintfStmt parsePrintfStmt() {
        Token printfToken = tokens.get(pos);
        pos += 2;
        Token stringConst = tokens.get(pos);
        pos++;
        //System.out.println("adjust " + tokens.get(pos).toString());
        ArrayList<Exp> exps = new ArrayList<>();
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            exps.add(parseExp());
        }
        //pos+=1;
        handlerError(TokenType.RPARENT);
        handlerError(TokenType.SEMICN);
        return new PrintfStmt(printfToken, stringConst, exps);
    }

    private ForStmt parseForStmt() {
        ArrayList<LValExpStmt> lValExps = new ArrayList<>();
        lValExps.add(parseLValExp());
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            lValExps.add(parseLValExp());
        }
        return new ForStmt(lValExps);
    }

    private LValExpStmt parseLValExp() {
        LVal lVal = parseLVal();
        pos++;
        Exp exp = parseExp();
        return new LValExpStmt(lVal, exp);
    }

    private Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        return new Cond(lOrExp);
    }

    private LVal parseLVal() {
        Token ident = tokens.get(pos);
        pos++;
        Exp exp = null;
        if (tokens.get(pos).getTokenType() == TokenType.LBRACK) {
            pos++;
            exp = parseExp();
            handlerError(TokenType.RBRACK);
        }
        return new LVal(ident, exp);
    }

    private AddExp parseAddExp() {
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<Token> symbols = new ArrayList<>();
        mulExps.add(parseMulExp());
        while (tokens.get(pos).getTokenType() == TokenType.PLUS
                || tokens.get(pos).getTokenType() == TokenType.MINU) {
            symbols.add(tokens.get(pos));
            pos++;
            mulExps.add(parseMulExp());
        }
        return new AddExp(mulExps, symbols);
    }

    private MulExp parseMulExp() {
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> symbols = new ArrayList<>();
        unaryExps.add(parseUnaryExp());
        while (tokens.get(pos).getTokenType() == TokenType.MULT
                || tokens.get(pos).getTokenType() == TokenType.MOD
                || tokens.get(pos).getTokenType() == TokenType.DIV) {
            symbols.add(tokens.get(pos));
            pos++;
            unaryExps.add(parseUnaryExp());
        }
        return new MulExp(unaryExps, symbols);
    }

    private UnaryExp parseUnaryExp() {
        if (tokens.get(pos).getTokenType() == TokenType.IDENFR && tokens.get(pos + 1).getTokenType() == TokenType.LPARENT) {
            Token ident = tokens.get(pos);
            pos += 2;
            FuncRParams funcRParams = null;
            if (EXP_FIRST.contains(tokens.get(pos).getTokenType())) {
                funcRParams = parseFuncRParmas();
            }
            //pos++;
            handlerError(TokenType.RPARENT);
            return new UnaryExp(null, ident, funcRParams, null, null);
        } else if (tokens.get(pos).getTokenType() == TokenType.PLUS || tokens.get(pos).getTokenType() == TokenType.MINU || tokens.get(pos).getTokenType() == TokenType.NOT) {
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp = parseUnaryExp();
            return new UnaryExp(null, null, null, unaryOp, unaryExp);
        } else {
            PrimaryExp primaryExp = parsePrimaryExp();
            return new UnaryExp(primaryExp, null, null, null, null);
        }
    }

    private FuncRParams parseFuncRParmas() {
        ArrayList<Exp> exps = new ArrayList<>();
        exps.add(parseExp());
        while (tokens.get(pos).getTokenType() == TokenType.COMMA) {
            pos++;
            exps.add(parseExp());
        }
        return new FuncRParams(exps);
    }

    private UnaryOp parseUnaryOp() {
        Token op = tokens.get(pos);
        pos++;
        return new UnaryOp(op);
    }

    private PrimaryExp parsePrimaryExp() {
        //System.out.println("is "+ tokens.get(pos).toString());
        if (tokens.get(pos).getTokenType() == TokenType.LPARENT) {
            pos++;
            Exp exp = parseExp();
            //pos++;
            handlerError(TokenType.RPARENT);
            return new PrimaryExp(exp, null, null);
        } else if (tokens.get(pos).getTokenType() == TokenType.IDENFR) {
            LVal lVal = parseLVal();
            return new PrimaryExp(null, lVal, null);
        } else {
            Number number = parseNumber();
            return new PrimaryExp(null, null, number);
        }
    }

    private Number parseNumber() {
        Token intConst = tokens.get(pos);
        pos++;
        return new Number(intConst);
    }

    private LOrExp parseLOrExp() {
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        while (tokens.get(pos).getTokenType() == TokenType.OR) {
            pos++;
            lAndExps.add(parseLAndExp());
        }
        return new LOrExp(lAndExps);
    }

    private LAndExp parseLAndExp() {
        ArrayList<EqExp> eqExps = new ArrayList<>();
        eqExps.add(parseEqExp());
        while (tokens.get(pos).getTokenType() == TokenType.AND) {
            pos++;
            eqExps.add(parseEqExp());
        }
        return new LAndExp(eqExps);
    }

    private EqExp parseEqExp() {
        ArrayList<RelExq> relExqs = new ArrayList<>();
        ArrayList<Token> symbols = new ArrayList<>();
        relExqs.add(parseRelExq());
        while (tokens.get(pos).getTokenType() == TokenType.EQL || tokens.get(pos).getTokenType() == TokenType.NEQ) {
            symbols.add(tokens.get(pos));
            pos++;
            relExqs.add(parseRelExq());
        }
        return new EqExp(relExqs, symbols);
    }

    private RelExq parseRelExq() {
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> symbols = new ArrayList<>();
        addExps.add(parseAddExp());
        while (tokens.get(pos).getTokenType() == TokenType.LEQ || tokens.get(pos).getTokenType() == TokenType.LSS || tokens.get(pos).getTokenType() == TokenType.GEQ || tokens.get(pos).getTokenType() == TokenType.GRE) {
            symbols.add(tokens.get(pos));
            pos++;
            addExps.add(parseAddExp());
        }
        return new RelExq(addExps, symbols);
    }

}
