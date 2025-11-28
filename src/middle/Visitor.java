package middle;

import fronted.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Visitor {
    private CompUnit compUnit;
    private SymbolType curFuncType = null;
    private TableManager tableManager = TableManager.getINSTANCE1();

    //ToDo getInt记得改一下
    public Visitor(CompUnit compUnit) {
        this.compUnit = compUnit;
    }

    public void visit() {
        visitCompUnit();
    }

    private void visitCompUnit() {
        setLibFunctions();
        for (Decl decl : compUnit.getDecls()) {
            visitDecl(decl);
        }
        for (FuncDef funcDef : compUnit.getFuncDefs()) {
            visitFuncDef(funcDef);
        }
        visitMainFuncDef(compUnit.getMainFuncDef());
    }

    private void setLibFunctions() {
        FuncSymbol funcSymbol = new FuncSymbol("getint", SymbolType.INT, new ArrayList<>());
        tableManager.addSymbol(funcSymbol);
    }

    private void visitDecl(Decl decl) {
        if (decl instanceof ConstDecl) {
            visitConstDecl((ConstDecl) decl);
        } else {
            visitVarDecl((VarDecl) decl);
        }
    }

    private void visitConstDecl(ConstDecl constDecl) {
        for (ConstDef constDef : constDecl.getConstDefs()) {
            visitConstDef(constDef);
        }
    }

    private void visitConstDef(ConstDef constDef) {
        if (tableManager.isContainSymbol(constDef.getIdent().getTokenContent())) {
            ErrorHandler.addError(new ErrorToken(constDef.getIdent().getLineNum(), "b"));
            return;
        }
        if (constDef.getConstExp() != null) {
            visitConstExp(constDef.getConstExp());
        }
        visitConstInitVal(constDef.getConstInitival());
        int dimension = 0;
        if (constDef.getConstExp() != null) {
            dimension = 1;
        }
        VarSymbol varSymbol = new VarSymbol(constDef.getIdent().getTokenContent(), SymbolType.INT, true, dimension);
        tableManager.addSymbol(varSymbol);
    }

    private void visitConstExp(ConstExp constExp) {
        visitAddExp(constExp.getAddExp());
    }

    private void visitConstInitVal(ConstInitVal constInitVal) {
        for (ConstExp constExp : constInitVal.getConstExps()) {
            visitConstExp(constExp);
        }
    }

    private void visitVarDecl(VarDecl varDecl) {
        for (VarDef varDef : varDecl.getVarDefs()) {
            visitVarDef(varDef, varDecl.getType());
        }
    }

    private void visitVarDef(VarDef varDef, int type) {
        if (tableManager.isContainSymbol(varDef.getIdent().getTokenContent())) {
            ErrorHandler.addError(new ErrorToken(varDef.getIdent().getLineNum(), "b"));
            return;
        }
        if (varDef.getConstExp() != null) {
            visitConstExp(varDef.getConstExp());
        }
        if (varDef.getInitVal() != null) {
            visitInitVal(varDef.getInitVal());
        }
        int dimension = 0;
        if (varDef.getConstExp() != null) {
            dimension = 1;
        }
        SymbolType symbolType = SymbolType.INT;
        if (type == 1) {
            symbolType = SymbolType.STATIC;
        }
        VarSymbol varSymbol = new VarSymbol(varDef.getIdent().getTokenContent(), symbolType, false, dimension);
        tableManager.addSymbol(varSymbol);
    }

    private void visitInitVal(InitVal initVal) {
        for (Exp exp : initVal.getExps()) {
            visitExp(exp);
        }
    }

    private void visitExp(Exp exp) {
        visitAddExp(exp.getAddExp());
    }

    private void visitFuncDef(FuncDef funcDef) {
        SymbolType funcType = null;
        if (funcDef.getFuncType().getTokenType() == TokenType.INTTK) {
            funcType = SymbolType.INT;
        } else if (funcDef.getFuncType().getTokenType() == TokenType.VOIDTK) {
            funcType = SymbolType.VOID;
        }
        boolean flag = true;
        if (tableManager.isContainSymbol(funcDef.getIdent().getTokenContent())) {
            ErrorHandler.addError(new ErrorToken(funcDef.getIdent().getLineNum(), "b"));
            flag = false;
        }
        ArrayList<ParamSymbol> paramSymbols = new ArrayList<>();
        if (funcDef.getFuncParmas() != null) {
            for (FuncFParam funcFParam : funcDef.getFuncParmas().getFuncFParams()) {
                paramSymbols.add(new ParamSymbol(funcFParam.getIdent().getTokenContent(), SymbolType.INT, funcFParam.getType()));
            }
        }
        if (flag) {
            tableManager.addSymbol(new FuncSymbol(funcDef.getIdent().getTokenContent(), funcType, paramSymbols));
        }
        tableManager.createTable(funcType);
        if (funcDef.getFuncParmas() != null) {
            visitFuncFParams(funcDef.getFuncParmas());
        }
        curFuncType = funcType;
        visitBlock(funcDef.getBlock());
        tableManager.backTable();
        curFuncType = null;
    }

    private void visitFuncFParams(FuncFParams funcFParams) {
        for (FuncFParam funcFParam : funcFParams.getFuncFParams()) {
            visitFuncFParam(funcFParam);
        }
    }

    private void visitFuncFParam(FuncFParam funcFParam) {
        if (tableManager.isContainSymbol(funcFParam.getIdent().getTokenContent())) {
            ErrorHandler.addError(new ErrorToken(funcFParam.getIdent().getLineNum(), "b"));
            return;
        }
        int dimension = 0;
        if (funcFParam.getType() == 1) {
            dimension = 1;
        }
        VarSymbol varSymbol = new VarSymbol(funcFParam.getIdent().getTokenContent(), SymbolType.INT, false, dimension);
        tableManager.addSymbol(varSymbol);
    }

    private void visitMainFuncDef(MainFuncDef mainFuncDef) {
        tableManager.addSymbol(new FuncSymbol("main", SymbolType.INT, new ArrayList<>()));
        curFuncType = SymbolType.INT;
        tableManager.createTable(SymbolType.INT);
        visitBlock(mainFuncDef.getBlock());
        tableManager.backTable();
        curFuncType = null;
    }

    private void visitBlock(Block block) {
        for (BlockItem blockItem : block.getBlockItems()) {
            visitBlockItem(blockItem);
        }
        if (tableManager.isFuncTable()) {
            if (curFuncType == SymbolType.INT) {
                ArrayList<BlockItem> blockItems = block.getBlockItems();
                if (blockItems.isEmpty() || blockItems.get(blockItems.size() - 1).getStmt() == null || !(blockItems.get(blockItems.size() - 1).getStmt() instanceof ReturnStmt)) {
                    ErrorHandler.addError(new ErrorToken(block.getEndLineNum(), "g"));
                }
            }
        }
    }

    private void visitBlockItem(BlockItem blockItem) {
        if (blockItem.getDecl() != null) {
            visitDecl(blockItem.getDecl());
        } else {
            visitStmt(blockItem.getStmt());
        }
    }

    private void visitStmt(Stmt stmt) {
        if (stmt instanceof LValExpStmt) {
            visitLValExpStmt((LValExpStmt) stmt);
        } else if (stmt instanceof ExpStmt) {
            visitExpStmt((ExpStmt) stmt);
        } else if (stmt instanceof BlockStmt) {
            visitBlockStmt((BlockStmt) stmt);
        } else if (stmt instanceof IfStmt) {
            visitIfStmt((IfStmt) stmt);
        } else if (stmt instanceof ForStruct) {
            visitForStruct((ForStruct) stmt);
        } else if (stmt instanceof BreakStmt) {
            visitBreakStmt((BreakStmt) stmt);
        } else if (stmt instanceof ContinueStmt) {
            visitContinueStmt((ContinueStmt) stmt);
        } else if (stmt instanceof ReturnStmt) {
            visitReturnStmt((ReturnStmt) stmt);
        } else if (stmt instanceof PrintfStmt) {
            visitPrintfStmt((PrintfStmt) stmt);
        }
    }

    private void visitLValExpStmt(LValExpStmt lValExpStmt) {
        visitLVal(lValExpStmt.getlVal());
        if (tableManager.isConstant(lValExpStmt.getlVal().getIdent().getTokenContent())) {
            ErrorHandler.addError(new ErrorToken(lValExpStmt.getlVal().getIdent().getLineNum(), "h"));
        }
        visitExp(lValExpStmt.getExp());
    }

    private void visitExpStmt(ExpStmt expStmt) {
        if (expStmt.getExp() != null) {
            visitExp(expStmt.getExp());
        }
    }

    public void visitBlockStmt(BlockStmt blockStmt) {
        tableManager.createTable(null);
        visitBlock(blockStmt.getBlock());
        tableManager.backTable();
    }

    private void visitIfStmt(IfStmt ifStmt) {
        if (ifStmt.getCond() != null) {
            visitCond(ifStmt.getCond());
        }
        for (Stmt stmt : ifStmt.getStmts()) {
            visitStmt(stmt);
        }
    }

    private void visitForStruct(ForStruct forStruct) {
        if (forStruct.getForStmt1() != null) {
            visitForStmt(forStruct.getForStmt1());
        }
        if (forStruct.getCond() != null) {
            visitCond(forStruct.getCond());
        }
        if (forStruct.getForStmt2() != null) {
            visitForStmt(forStruct.getForStmt2());
        }
        if (forStruct.getStmt() != null) {
            tableManager.enterLoop();
            visitStmt(forStruct.getStmt());
            tableManager.exitLoop();
        }
    }

    private void visitBreakStmt(BreakStmt breakStmt) {
        if (tableManager.getLoopLevel() == 0) {
            ErrorHandler.addError(new ErrorToken(breakStmt.getToken().getLineNum(), "m"));
        }
    }

    private void visitContinueStmt(ContinueStmt continueStmt) {
        if (tableManager.getLoopLevel() == 0) {
            ErrorHandler.addError(new ErrorToken(continueStmt.getToken().getLineNum(), "m"));
        }
    }

    private void visitReturnStmt(ReturnStmt returnStmt) {
        if (curFuncType == SymbolType.VOID && returnStmt.getExp() != null) {
            ErrorHandler.addError(new ErrorToken(returnStmt.getToken().getLineNum(), "f"));
        }
        if (returnStmt.getExp() != null) {
            visitExp(returnStmt.getExp());
        }
    }

    private void visitPrintfStmt(PrintfStmt printfStmt) {
        for (Exp exp : printfStmt.getExps()) {
            visitExp(exp);
        }
        String stringConst = printfStmt.getToken().getTokenContent();
        int cnt = 0;
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(stringConst);
        while (matcher.find()) {
            cnt++;
        }
        if (cnt != printfStmt.getExps().size()) {
            ErrorHandler.addError(new ErrorToken(printfStmt.getPrintfToken().getLineNum(), "l"));
        }
    }

    private void visitForStmt(ForStmt forStmt) {
        for (LValExpStmt lValExpStmt : forStmt.getlValExpStmts()) {
            visitLVal(lValExpStmt.getlVal());
            if (tableManager.isConstant(lValExpStmt.getlVal().getIdent().getTokenContent())) {
                ErrorHandler.addError(new ErrorToken(lValExpStmt.getlVal().getIdent().getLineNum(), "h"));
            }
            visitExp(lValExpStmt.getExp());
        }
    }

    private void visitCond(Cond cond) {
        visitLOrExp(cond.getlOrExp());
    }

    private void visitLVal(LVal lVal) {
        if (tableManager.getSymbol(lVal.getIdent().getTokenContent()) == null) {
            //System.out.println("adjust");
            ErrorHandler.addError(new ErrorToken(lVal.getIdent().getLineNum(), "c"));
        }
        if (lVal.getExp() != null) {
            visitExp(lVal.getExp());
        }
    }

    private void visitAddExp(AddExp addExp) {
        for (MulExp mulExp : addExp.getMulExps()) {
            visitMulExp(mulExp);
        }
    }

    private void visitMulExp(MulExp mulExp) {
        for (UnaryExp unaryExp : mulExp.getUnaryExps()) {
            visitUnaryExp(unaryExp);
        }
    }

    private void visitUnaryExp(UnaryExp unaryExp) {
        if (unaryExp.getPrimaryExp() != null) {
            visitPrimaryExp(unaryExp.getPrimaryExp());
        } else if (unaryExp.getUnaryExp() != null) {
            visitUnaryExp(unaryExp.getUnaryExp());
        } else {
            if (unaryExp.getFuncRParams() != null) {
                visitFuncRParams(unaryExp.getFuncRParams());
            }
            //ToDo 这里关于getint的处理还有待商榷
//            if(unaryExp.getIdent().getTokenContent().equals("getint")){
//                return;
//            }
            if (tableManager.getSymbol(unaryExp.getIdent().getTokenContent()) == null) {
                ErrorHandler.addError(new ErrorToken(unaryExp.getIdent().getLineNum(), "c"));
                return;
            }
            FuncSymbol funcSymbol = (FuncSymbol) tableManager.getSymbol(unaryExp.getIdent().getTokenContent());
            checkParams(unaryExp, funcSymbol);
        }
    }

    private void visitPrimaryExp(PrimaryExp primaryExp) {
        if (primaryExp.getExp() != null) {
            visitExp(primaryExp.getExp());
        } else if (primaryExp.getlVal() != null) {
            visitLVal(primaryExp.getlVal());
        }
    }

    private void visitFuncRParams(FuncRParams funcRParams) {
        for (Exp exp : funcRParams.getExps()) {
            visitExp(exp);
        }
    }

    private void checkParams(UnaryExp unaryExp, FuncSymbol funcSymbol) {
        if (unaryExp.getFuncRParams() == null && !funcSymbol.getParamSymbols().isEmpty()) {
            ErrorHandler.addError(new ErrorToken(unaryExp.getIdent().getLineNum(), "d"));
            return;
        }
        if (unaryExp.getFuncRParams() != null && (unaryExp.getFuncRParams().getExps().size() != funcSymbol.getParamSymbols().size())) {
            ErrorHandler.addError(new ErrorToken(unaryExp.getIdent().getLineNum(), "d"));
            return;
        }
        if (unaryExp.getFuncRParams() != null) {
            for (int i = 0; i < unaryExp.getFuncRParams().getExps().size(); i++) {
                ParamSymbol paramSymbol = ToParam.expToParam(unaryExp.getFuncRParams().getExps().get(i));
                ParamSymbol paramSymbol1 = funcSymbol.getParamSymbols().get(i);
                if (paramSymbol == null) {
                    continue;
                }
                int dimension;
                if (paramSymbol.getName() == null) {
                    dimension = 0;
                } else {
                    Symbol symbol = tableManager.getSymbol(paramSymbol.getName());
                    if (symbol.getType() == SymbolType.VOID) {
                        dimension = -1;
                    } else if (symbol instanceof VarSymbol) {
                        dimension = ((VarSymbol) symbol).getDimension() - paramSymbol.getDimension();
                    } else {
                        dimension = 0;
                    }
                }
                if (dimension != paramSymbol1.getDimension()) {
                    ErrorHandler.addError(new ErrorToken(unaryExp.getIdent().getLineNum(), "e"));
                }
            }
        }
    }

    private void visitLOrExp(LOrExp lOrExp) {
        for (LAndExp lAndExp : lOrExp.getlAndExps()) {
            visitLAndExp(lAndExp);
        }
    }

    private void visitLAndExp(LAndExp lAndExp) {
        for (EqExp eqExp : lAndExp.getEqExps()) {
            visitEqExp(eqExp);
        }
    }

    private void visitEqExp(EqExp eqExp) {
        for (RelExq relExq : eqExp.getRelExqs()) {
            visitRelExp(relExq);
        }
    }

    private void visitRelExp(RelExq relExq) {
        for (AddExp addExp : relExq.getAddExps()) {
            visitAddExp(addExp);
        }
    }
}
