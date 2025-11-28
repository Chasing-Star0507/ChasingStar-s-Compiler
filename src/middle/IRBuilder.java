package middle;

import fronted.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRBuilder {
    private CompUnit compUnit;
    private TableManager tableManager = TableManager.getINSTANCE2();
    private boolean isGlobal;

    //ToDo getInt还没处理！！！我认为getInt可以当成一个函数来处理！而不需要单独列出！

    public IRBuilder(CompUnit compUnit){
        this.compUnit = compUnit;
    }

    public void build(){
        buildCompUnit();
    }

    private void setBuiltInFunctions() {
        FuncSymbol symbol = new FuncSymbol("getint", SymbolType.INT, new ArrayList<>());
        Function function = new Function("@getint", IntegerType.i32,true);
        symbol.setLlvmValue(function);
        tableManager.addSymbol(symbol);

        symbol = new FuncSymbol("putint", SymbolType.VOID, new ArrayList<>());
        function = new Function("@putint", IntegerType.VOID,true);
        symbol.setLlvmValue(function);
        tableManager.addSymbol(symbol);

        symbol = new FuncSymbol("putstr", SymbolType.VOID, new ArrayList<>());
        function = new Function("@putstr", IntegerType.VOID,true);
        symbol.setLlvmValue(function);
        tableManager.addSymbol(symbol);
    }

    private void buildCompUnit(){
        setBuiltInFunctions();
        isGlobal = true;
        for(Decl decl : compUnit.getDecls()){
            buildDecl(decl);
        }
        isGlobal = false;
        for(FuncDef funcDef : compUnit.getFuncDefs()){
            buildFuncDef(funcDef);
            IRData.reset();
        }
        buildMainFuncDef(compUnit.getMainFuncDef());
    }

    private void buildDecl(Decl decl){
        if(decl instanceof ConstDecl){
            buildConstDecl((ConstDecl)decl);
        }else {
            buildVarDecl((VarDecl)decl);
        }
    }

    private void buildConstDecl(ConstDecl constDecl){
        for(ConstDef constDef : constDecl.getConstDefs()){
            buildConstDef(constDef);
        }
    }

    private void buildConstDef(ConstDef constDef){
        int dimension = 0;
        int length = 0;
        ValueType valueType = null;
        if (constDef.getConstExp() != null) {
            dimension = 1;
            length = constDef.getConstExp().calculate();
            valueType = new ArrayType(length,IntegerType.i32);
        }else{
            valueType = IntegerType.i32;
        }
        ArrayList<Integer> integers = calculateConstInitVal(constDef.getConstInitival());
        InitialValue initialValue = new InitialValue(valueType,length,integers);
        VarSymbol varSymbol = new VarSymbol(constDef.getIdent().getTokenContent(), SymbolType.INT, true, dimension,initialValue);
        tableManager.addSymbol(varSymbol);
        if(isGlobal){
            String name = "@" + constDef.getIdent().getTokenContent();
            ValueType type = new PointType(valueType);
            GlobalVar globalVar = new GlobalVar(name,type,initialValue,true);
            varSymbol.setLlvmValue(globalVar);
            //ToDo 这里可以加入setLLVM 不知道有什么用 else部分也要记得加
        }else {
            Instruction instruction = new AllocInst(valueType);
            if(dimension == 0){
                int i = integers.get(0);
                new StoreInst(instruction,new ConstInt(i));
            }else{
                //ToDo 这里要非常小心！！！！！！！！
                AllocInst allocInst = (AllocInst) instruction;
                for(int i = 0 ;i < integers.size();i++){
                    GepInst gepInst = new GepInst(instruction,new ConstInt(i));
                    allocInst.addGepInst(gepInst);
                    StoreInst storeInst = new StoreInst(gepInst,new ConstInt(integers.get(i)));
                    allocInst.addStoreInst(storeInst);
                }
            }
            varSymbol.setLlvmValue(instruction);
            //ToDo main再来做有点难了
        }
    }

    private ArrayList<Integer> calculateConstInitVal(ConstInitVal constInitVal){
        ArrayList<Integer> integers = new ArrayList<>();
        for(ConstExp constExp : constInitVal.getConstExps()){
            integers.add(constExp.calculate());
        }
        return integers;
    }

    private void buildVarDecl(VarDecl varDecl){
        for(VarDef varDef : varDecl.getVarDefs()){
            buildVarDef(varDef,varDecl.getType());
        }
    }

    private void buildVarDef(VarDef varDef,int type){
        int dimension = 0;
        int length = 0;
        ValueType valueType = null;
        if (varDef.getConstExp() != null) {
            dimension = 1;
            length = varDef.getConstExp().calculate();
            valueType = new ArrayType(length,IntegerType.i32);
        }else{
            valueType = IntegerType.i32;
        }
        SymbolType symbolType = SymbolType.INT;
        if (type == 1) {
            symbolType = SymbolType.STATIC;
        }
        VarSymbol varSymbol = new VarSymbol(varDef.getIdent().getTokenContent(), symbolType, false, dimension);
        tableManager.addSymbol(varSymbol);
        if(isGlobal){
            //ToDo 可能出现null 要小心！！
            ArrayList<Integer> integers = new ArrayList<>();
            if (varDef.getInitVal() != null) {
                integers = calculateInitVal(varDef.getInitVal());
            }
            InitialValue initialValue  = new InitialValue(valueType,length,integers);
            varSymbol.setInitialValue(initialValue);
            String name = "@" + varDef.getIdent().getTokenContent();
            ValueType valueType1 = new PointType(valueType);
            GlobalVar globalVar = new GlobalVar(name,valueType1,initialValue,false);
            varSymbol.setLlvmValue(globalVar);
            //ToDo more
        }else{
            if(type == 1){
                //ToDo static 当成globalVar处理 多加留意！
                ArrayList<Integer> integers = new ArrayList<>();
                Function function = IRData.getCurFunction();
                if (varDef.getInitVal() != null) {
                    integers = calculateInitVal(varDef.getInitVal());
                }
                InitialValue initialValue  = new InitialValue(valueType,length,integers);
                varSymbol.setInitialValue(initialValue);
                String name = function.getName() +  "." + varDef.getIdent().getTokenContent() + "." + IRData.getStaticCnt();
                ValueType valueType1 = new PointType(valueType);
                GlobalVar globalVar = new GlobalVar(name,valueType1,initialValue,false);
                varSymbol.setLlvmValue(globalVar);
            }else{
                ArrayList<Value> values = new ArrayList<>();
                Instruction instruction = new AllocInst(valueType);
                varSymbol.setLlvmValue(instruction);
                if(dimension == 0){
                    if(varDef.getInitVal() != null){
                        values = buildInitVal(varDef.getInitVal());
                        StoreInst storeInst = new StoreInst(instruction,values.get(0));
                        ((AllocInst) instruction).addStoreInst(storeInst);
                    }
                }else{
                    //ToDo 这里要非常小心！！！！！！！！
                    AllocInst allocInst = (AllocInst) instruction;
                    if(varDef.getInitVal() != null){
                        values = buildInitVal(varDef.getInitVal());
                    }
                    for(int i = 0 ;i < values.size();i++){
                        GepInst gepInst = new GepInst(instruction,new ConstInt(i));
                        allocInst.addGepInst(gepInst);
                        StoreInst storeInst = new StoreInst(gepInst,values.get(i));
                        allocInst.addStoreInst(storeInst);
                    }
                }
            }
        }
    }

    private ArrayList<Value> buildInitVal(InitVal initVal){
        ArrayList<Value> values = new ArrayList<>();
        for(Exp exp : initVal.getExps()){
            values.add(buildExp(exp));
        }
        return values;
    }

    private ArrayList<Integer> calculateInitVal(InitVal initVal){
        ArrayList<Integer> integers = new ArrayList<>();
        for(Exp exp : initVal.getExps()){
            integers.add(exp.calculate());
        }
        return integers;
    }

    private void buildFuncDef(FuncDef funcDef){
        SymbolType funcType = null;
        ValueType funcRetType = null;
        if (funcDef.getFuncType().getTokenType() == TokenType.INTTK) {
            funcType = SymbolType.INT;
            funcRetType = IntegerType.i32;
            //ToDo more
        } else if (funcDef.getFuncType().getTokenType() == TokenType.VOIDTK) {
            funcType = SymbolType.VOID;
            funcRetType = IntegerType.VOID;
        }
        ArrayList<ParamSymbol> paramSymbols = new ArrayList<>();
        if (funcDef.getFuncParmas() != null) {
            for (FuncFParam funcFParam : funcDef.getFuncParmas().getFuncFParams()) {
                paramSymbols.add(new ParamSymbol(funcFParam.getIdent().getTokenContent(), SymbolType.INT, funcFParam.getType()));
            }
        }
        FuncSymbol funcSymbol = new FuncSymbol(funcDef.getIdent().getTokenContent(), funcType, paramSymbols);
        tableManager.addSymbol(funcSymbol);
        String name = "@" + funcDef.getIdent().getTokenContent();
        Function function = new Function(name,funcRetType);
        IRData.setCurFunction(function);
        BasicBlock basicBlock = new BasicBlock(IRData.getBasicBlockName());
        tableManager.createTable(funcType);
        //curFuncType = funcType; 可能有用的！
        IRData.setCurBasicBlock(basicBlock);
        funcSymbol.setLlvmValue(function);
        if(funcDef.getFuncParmas() != null){
            buildFuncParmas(funcDef.getFuncParmas());
        }
        buildBlock(funcDef.getBlock());
        //ToDo 最后一句的处理但是不知道有什么用？ 是对void函数的处理吗？但是int函数不会出现这种问题？
        BasicBlock block = IRData.getCurBasicBlock();
        if(block.getInstructions().isEmpty() || !(block.getInstructions().get(block.getInstructions().size() - 1) instanceof RetInst)){
            if(funcType == SymbolType.VOID){
                new RetInst(null);
            }
        }
        tableManager.backTable();
        IRData.setCurFunction(null);
        //curFuncType = null;
    }

    private void buildFuncParmas(FuncFParams funcFParams){
        for(FuncFParam funcFParam : funcFParams.getFuncFParams()){
            buildFuncFParam(funcFParam);
        }
    }

    private void buildFuncFParam(FuncFParam funcFParam){
        int dimension = 0;
        ValueType valueType = IntegerType.i32;
        if (funcFParam.getType() == 1) {
            dimension = 1;
            valueType = new PointType(valueType);
        }
        VarSymbol varSymbol = new VarSymbol(funcFParam.getIdent().getTokenContent(), SymbolType.INT, false, dimension);
        FuncParam funcParam = new FuncParam(IRData.getVarName(),valueType);
        //ToDo 这里没做完呢！！！仔细检查！
        tableManager.addSymbol(varSymbol);
        if (valueType instanceof IntegerType) {
            Instruction instruction = new AllocInst(valueType);
            varSymbol.setLlvmValue(instruction);
            new StoreInst(instruction, funcParam);
        } else {
            varSymbol.setLlvmValue(funcParam);
        }
        IRData.getCurFunction().addFuncParam(funcParam);
    }

    private void buildBlock(Block block){
        for(BlockItem blockItem : block.getBlockItems()){
            buildBlockItem(blockItem);
        }
    }

    private void buildBlockItem(BlockItem blockItem){
        if(blockItem.getDecl() != null){
            buildDecl(blockItem.getDecl());
        }else{
            buildStmt(blockItem.getStmt());
        }
    }

    private void buildMainFuncDef(MainFuncDef mainFuncDef){
        FuncSymbol funcSymbol = new FuncSymbol("main", SymbolType.INT, new ArrayList<>());
        tableManager.addSymbol(funcSymbol);
        Function function = new Function("@main",IntegerType.i32);
        IRData.setCurFunction(function);
        funcSymbol.setLlvmValue(function);
        BasicBlock basicBlock = new BasicBlock(IRData.getBasicBlockName());
        //curFuncType = SymbolType.INT;
        tableManager.createTable(SymbolType.INT);
        IRData.setCurBasicBlock(basicBlock);
        buildBlock(mainFuncDef.getBlock());
        tableManager.backTable();
        IRData.setCurFunction(null);
        //curFuncType = null;
    }

    private void buildStmt(Stmt stmt){
        if (stmt instanceof LValExpStmt) {
            buildLValExpStmt((LValExpStmt) stmt);
        } else if (stmt instanceof ExpStmt) {
            buildExpStmt((ExpStmt) stmt);
        } else if (stmt instanceof BlockStmt) {
            buildBlockStmt((BlockStmt) stmt);
        } else if (stmt instanceof IfStmt) {
            buildIfStmt((IfStmt) stmt);
        } else if (stmt instanceof ForStruct) {
            buildForStruct((ForStruct) stmt);
        } else if (stmt instanceof BreakStmt) {
            buildBreakStmt((BreakStmt) stmt);
        } else if (stmt instanceof ContinueStmt) {
            buildContinueStmt((ContinueStmt) stmt);
        } else if (stmt instanceof ReturnStmt) {
            buildReturnStmt((ReturnStmt) stmt);
        } else if (stmt instanceof PrintfStmt) {
            buildPrintfStmt((PrintfStmt) stmt);
        }
    }

    private void buildLValExpStmt(LValExpStmt lValExpStmt){
        Value lValue = buildLeftLVal(lValExpStmt.getlVal());
        Value rValue = buildExp(lValExpStmt.getExp());
        new StoreInst(lValue,rValue);
    }

    private Value buildLeftLVal(LVal lVal){
        Value value = null;
        if(lVal.getExp() != null){
            value = buildExp(lVal.getExp());
        }
        VarSymbol varSymbol = (VarSymbol) tableManager.getSymbol(lVal.getIdent().getTokenContent());
        if(varSymbol.getDimension() == 0){
            return varSymbol.getLlvmValue();
        }else{
            return new GepInst(varSymbol.getLlvmValue(),value);
        }
    }

    private void buildExpStmt(ExpStmt expStmt){
        if(expStmt.getExp() != null){
            buildExp(expStmt.getExp());
        }
    }

    private void buildBlockStmt(BlockStmt blockStmt){
        tableManager.createTable(null);
        buildBlock(blockStmt.getBlock());
        tableManager.backTable();
    }

    private void buildIfStmt(IfStmt ifStmt){
        BasicBlock trueBlock = new BasicBlock(IRData.getBasicBlockName());
        if(ifStmt.getStmts().size() > 1){
            BasicBlock falseBlock = new BasicBlock(IRData.getBasicBlockName());
            BasicBlock followBlock = new BasicBlock(IRData.getBasicBlockName());
            buildCond(ifStmt.getCond(),trueBlock,falseBlock);
            IRData.setCurBasicBlock(trueBlock);
            buildStmt(ifStmt.getStmts().get(0));
            new BrInst(followBlock);
            IRData.setCurBasicBlock(falseBlock);
            buildStmt(ifStmt.getStmts().get(1));
            new BrInst(followBlock);
            IRData.setCurBasicBlock(followBlock);
        }else{
            BasicBlock followBlock = new BasicBlock(IRData.getBasicBlockName());
            buildCond(ifStmt.getCond(),trueBlock,followBlock);
            IRData.setCurBasicBlock(trueBlock);
            buildStmt(ifStmt.getStmts().get(0));
            new BrInst(followBlock);
            IRData.setCurBasicBlock(followBlock);
        }
    }

    private void buildCond(Cond cond, BasicBlock trueBlock,BasicBlock falseBlock){
        buildLOrExp(cond.getlOrExp(),trueBlock,falseBlock);
    }

    private void buildForStruct(ForStruct forStruct){
        //ToDo 有待研究哈哈哈
        if(forStruct.getForStmt1() != null){
            buildForStmt(forStruct.getForStmt1());
        }
        BasicBlock conditionBlock = new BasicBlock(IRData.getBasicBlockName());
        BasicBlock loopBlock = new BasicBlock(IRData.getBasicBlockName());
        BasicBlock updateBlock = new BasicBlock(IRData.getBasicBlockName());
        BasicBlock followBlock = new BasicBlock(IRData.getBasicBlockName());
        IRData.push(new ForLoop(conditionBlock,loopBlock,updateBlock,followBlock));
        new BrInst(conditionBlock);//这个我不理解为什么要加上这句
        IRData.setCurBasicBlock(conditionBlock);
        if(forStruct.getCond() != null){
            buildCond(forStruct.getCond(),loopBlock,followBlock);
        }else{
            new BrInst(loopBlock);
        }
        IRData.setCurBasicBlock(loopBlock);
        buildStmt(forStruct.getStmt());
        new BrInst(updateBlock);
        IRData.setCurBasicBlock(updateBlock);
        if(forStruct.getForStmt2() != null){
            buildForStmt(forStruct.getForStmt2());
            new BrInst(conditionBlock);
        }else{
            new BrInst(conditionBlock);
        }
//        if(forStruct.getCond() != null){
//            buildCond(forStruct.getCond(),loopBlock,followBlock);
//        }else{
//            new BrInst(loopBlock);
//        }
        IRData.setCurBasicBlock(followBlock);
        IRData.pop();
    }

    private void buildForStmt(ForStmt forStmt){
        for(LValExpStmt lValExpStmt : forStmt.getlValExpStmts()){
            buildLValExpStmt(lValExpStmt);
        }
    }

    private void buildBreakStmt(BreakStmt breakStmt){
        new BrInst(IRData.peek().getFollowBlock());
    }

    private void buildContinueStmt(ContinueStmt continueStmt){
        new BrInst(IRData.peek().getUpdateBlock());
    }

    private void buildReturnStmt(ReturnStmt returnStmt){
        Value value = null;
        if(returnStmt.getExp() != null){
            value = buildExp(returnStmt.getExp());
        }
        new RetInst(value);
    }

    private void buildPrintfStmt(PrintfStmt printfStmt){
        ArrayList<Value> values = new ArrayList<>();
        for(Exp exp : printfStmt.getExps()){
            values.add(buildExp(exp));
        }
        String stringConst = printfStmt.getToken().getTokenContent();
        stringConst = stringConst.substring(1,stringConst.length() - 1);
        stringConst = stringConst.replace("\\n","\n");//有什么用？
        int pos = 0, cnt = 0;
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(stringConst);
        while (matcher.find()) {
            int start  = matcher.start();
            String temString = stringConst.substring(pos,start);
            if(!temString.isEmpty()){
                ConstString constString;
                if(IRData.isContainString(temString)){
                    constString = IRData.getConstString(temString);
                }else{
                    constString = new ConstString(IRData.getStringName(),temString);
                }
                new PutstrInst(constString);
            }
            new PutintInst(values.get(cnt));
            cnt++;
            pos = start + 2;
        }
        if(pos < stringConst.length()){
            String temString = stringConst.substring(pos);
            if(!temString.isEmpty()){
                ConstString constString;
                if(IRData.isContainString(temString)){
                    constString = IRData.getConstString(temString);
                }else{
                    constString = new ConstString(IRData.getStringName(),temString);
                }
                new PutstrInst(constString);
            }
        }
    }

    private Value buildExp(Exp exp){
        return buildAddExp(exp.getAddExp());
    }

    private Value buildAddExp(AddExp addExp){
        Value left = buildMulExp(addExp.getMulExps().get(0));
        Value right;
        Instruction instruction;
        for(int i = 1;i < addExp.getMulExps().size();i++){
            TokenType op = addExp.getSymbols().get(i - 1).getTokenType();
            right = buildMulExp(addExp.getMulExps().get(i));
            if(op == TokenType.PLUS){
                instruction = new BinaryInst(OperatorType.ADD,left,right);
            }else{
                instruction = new BinaryInst(OperatorType.SUB,left,right);
            }
            left = instruction;
        }
        return left;
    }

    private Value buildMulExp(MulExp mulExp){
        Value left = buildUnaryExp(mulExp.getUnaryExps().get(0));
        Value right;
        Instruction instruction;
        for(int i = 1;i < mulExp.getUnaryExps().size();i++){
            TokenType op = mulExp.getSymbols().get(i - 1).getTokenType();
            right = buildUnaryExp(mulExp.getUnaryExps().get(i));
            if(op == TokenType.MULT){
                instruction = new BinaryInst(OperatorType.MUL,left,right);
            }else if(op == TokenType.DIV){
                instruction = new BinaryInst(OperatorType.SDIV,left,right);
            }else{
                instruction = new BinaryInst(OperatorType.SREM,left,right);
            }
            left = instruction;
        }
        return left;
    }

    private Value buildUnaryExp(UnaryExp unaryExp){
        if(unaryExp.getPrimaryExp() != null){
            return buildPrimaryExp(unaryExp.getPrimaryExp());
        }else if(unaryExp.getUnaryExp() != null){
            TokenType op = unaryExp.getUnaryOp().getOp().getTokenType();
            Value left = buildUnaryExp(unaryExp.getUnaryExp());
            Value right = new ConstInt(0);
            Instruction instruction;
            if(op == TokenType.PLUS){
                return left;
            }else if(op == TokenType.MINU){
                instruction = new BinaryInst(OperatorType.SUB,right,left);
                return instruction;
            }else{
                //ToDo 这里要注意
                instruction = new BinaryInst(OperatorType.ICMP_EQ, right, left);
                return new ZextInst(instruction);
            }
        }else{
            //System.out.println(unaryExp.getIdent().getTokenContent());
            FuncSymbol funcSymbol = (FuncSymbol) tableManager.getSymbol(unaryExp.getIdent().getTokenContent());

            Function function = (Function) funcSymbol.getLlvmValue();
//            if(function != null){
//                System.out.println("true");
//            }else{
//                System.out.println("false");
//            }
            ArrayList<Value> params = new ArrayList<>();
            if(unaryExp.getFuncRParams() != null){
                for(Exp exp : unaryExp.getFuncRParams().getExps()){
                    Value value = buildExp(exp);
                    params.add(value);
                }
            }
            return new CallInst(function,params);
        }
    }

    private Value buildPrimaryExp(PrimaryExp primaryExp){
        if(primaryExp.getExp() != null){
            return buildExp(primaryExp.getExp());
        }else if(primaryExp.getlVal() != null){
            return buildRightLVal(primaryExp.getlVal());
        }else{
            return new ConstInt(primaryExp.getNumber().calculate());
        }
    }

    private Value buildRightLVal(LVal lVal){
        Value value = null;
        if(lVal.getExp() != null){
            value = buildExp(lVal.getExp());
        }
        VarSymbol varSymbol = (VarSymbol) tableManager.getSymbol(lVal.getIdent().getTokenContent());
        if(varSymbol.getDimension() == 0){
            return new LoadInst(varSymbol.getLlvmValue());
        }else{
            if(value == null){
                return new GepInst(varSymbol.getLlvmValue(),new ConstInt(0));
            }else{
                Instruction instruction = new GepInst(varSymbol.getLlvmValue(),value);
                return new LoadInst(instruction);
            }
        }
    }

    //好复杂的logic 🤣
    private void buildLOrExp(LOrExp lOrExp,BasicBlock trueBlock,BasicBlock falseBlock){
        for(int i = 0;i < lOrExp.getlAndExps().size();i++){
            if(i == lOrExp.getlAndExps().size() - 1){
                buildLAndExp(lOrExp.getlAndExps().get(i) ,trueBlock,falseBlock);
            }else{
                BasicBlock nextBlock = new BasicBlock(IRData.getBasicBlockName());
                buildLAndExp(lOrExp.getlAndExps().get(i),trueBlock,nextBlock);
                IRData.setCurBasicBlock(nextBlock);
            }
        }
    }

    private void buildLAndExp(LAndExp lAndExp,BasicBlock trueBlock,BasicBlock falseBlock){
        for(int i = 0;i< lAndExp.getEqExps().size();i++){
            if(i == lAndExp.getEqExps().size() -1){
                Value value = buildEqExp(lAndExp.getEqExps().get(i));
                new BrInst(value,trueBlock,falseBlock);
            }else{
                BasicBlock nextBlock = new BasicBlock(IRData.getBasicBlockName());
                Value value = buildEqExp(lAndExp.getEqExps().get(i));
                new BrInst(value,nextBlock,falseBlock);
                IRData.setCurBasicBlock(nextBlock);
            }
        }
    }

    private Value buildEqExp(EqExp eqExp){
        Value left = buildRelExp(eqExp.getRelExqs().get(0));
        Value right;
        Instruction instruction;
        if(eqExp.getRelExqs().size() == 1 && left.getValueType().equals(IntegerType.i32)){
            return new BinaryInst(OperatorType.ICMP_NE,left,new ConstInt(0));
        }else{
            for(int i = 1;i < eqExp.getRelExqs().size();i++){
                TokenType op = eqExp.getSymbols().get(i - 1).getTokenType();
                right = buildRelExp(eqExp.getRelExqs().get(i));
                if (!left.getValueType().equals(IntegerType.i32)) {
                    left = new ZextInst(left);
                }
                if (!right.getValueType().equals(IntegerType.i32)) {
                    right = new ZextInst(right);
                }
                if(op == TokenType.EQL){
                    instruction = new BinaryInst(OperatorType.ICMP_EQ,left,right);
                }else{
                    instruction = new BinaryInst(OperatorType.ICMP_NE,left,right);
                }
                left = instruction;
            }
            return left;
        }
    }

    private Value buildRelExp(RelExq relExq){
        Value left = buildAddExp(relExq.getAddExps().get(0));
        Value right;
        Instruction instruction;
        if(relExq.getAddExps().size() == 1){
            return left;
        }else{
            for(int i = 1; i < relExq.getAddExps().size();i++){
                TokenType op = relExq.getSymbols().get(i - 1).getTokenType();
                right = buildAddExp(relExq.getAddExps().get(i));
                if(op == TokenType.GRE){
                    instruction = new BinaryInst(OperatorType.ICMP_SGT,left,right);
                }else if(op == TokenType.LSS){
                    instruction = new BinaryInst(OperatorType.ICMP_SLT,left,right);
                }else if(op == TokenType.GEQ){
                    instruction = new BinaryInst(OperatorType.ICMP_SGE,left,right);
                }else{
                    instruction = new BinaryInst(OperatorType.ICMP_SLE,left,right);
                }
                left = instruction;
            }
            return left;
        }
    }
}

