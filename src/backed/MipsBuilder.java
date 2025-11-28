package backed;

import middle.*;
import middle.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

import static middle.OperatorType.*;

public class MipsBuilder {
    private Module module;
    private int curStackOffset;
    private HashMap<Value,Integer> varToOffset;
    private HashMap<Value,Register> varToReg;
    private Function curFunction;
    private final Map<Class<? extends Instruction>, Consumer<Instruction>> instructionHandlers = new HashMap<>();
    private boolean isInMain;

    public MipsBuilder(Module module){
        this.module = module;
        initInstHandler();
    }

    private void initInstHandler(){
        instructionHandlers.put(AllocInst.class,inst -> buildAllocInst((AllocInst) inst));
        instructionHandlers.put(BinaryInst.class,inst -> {if(OperatorType.isLogicCond(((BinaryInst) inst).getOperatorType())){
            buildIcmp((BinaryInst) inst);
        }else{
            buildBinaryInst((BinaryInst) inst);
        }
        });
        instructionHandlers.put(BrInst.class,inst -> buildBrInst((BrInst) inst));
        instructionHandlers.put(CallInst.class,inst -> buildCallInst((CallInst) inst));
        instructionHandlers.put(GepInst.class,inst -> buildGepInst((GepInst) inst));
        instructionHandlers.put(PutintInst.class,inst -> buildPutintInst((PutintInst) inst));
        instructionHandlers.put(PutstrInst.class,inst -> buildPutstrInst((PutstrInst) inst));
        instructionHandlers.put(LoadInst.class,inst -> buildLoadInst((LoadInst) inst));
        instructionHandlers.put(RetInst.class,inst -> buildRetInst((RetInst) inst));
        instructionHandlers.put(StoreInst.class,inst -> buildStoreInst((StoreInst) inst));
        instructionHandlers.put(ZextInst.class,inst -> buildZextInst((ZextInst) inst));
    }

    public void build(){
        for(ConstString constString : module.getConstStrings()){
            buildConstString(constString);
        }
        for(GlobalVar globalVar : module.getGlobalVars()){
            buildGlobalVar(globalVar);
        }
        isInMain = true;
        for(Function function : module.getFunctions()){
            if(function.getName().equals("@main")){
                buildFunction(function);
            }
        }
        isInMain = false;
        for(Function function : module.getFunctions()){
            if(!function.getName().equals("@main")){
                buildFunction(function);
            }
        }
    }

    private void buildConstString(ConstString constString){
        new Asciiz(constString.getName().substring(2),constString.getStringValue());
    }

    private void buildGlobalVar(GlobalVar globalVar){
        ValueType valueType = ((PointType)globalVar.getValueType()).getTargetType();
        if(valueType instanceof IntegerType){
            if(globalVar.getInitialValue().getElements().isEmpty()){
                new Word(globalVar.getName().substring(1),0,false);
            }else{
                new Word(globalVar.getName().substring(1),globalVar.getInitialValue().getElements().get(0),false);
            }
        }else if(valueType instanceof ArrayType){
            new Word(globalVar.getName().substring(1),globalVar.getInitialValue().getElements(),true,globalVar.getInitialValue().getLength());
        }
    }

    private void buildFunction(Function function){
        curStackOffset = 0;
        curFunction = function;
        varToReg = new HashMap<>();
        varToOffset = new HashMap<>();
        new Label(function.getName().substring(1));
        for(int i = 0; i < function.getFuncParams().size();i++){
            curStackOffset -= 4;
            if(i < 3){
                varToReg.put(function.getFuncParams().get(i),Register.getOffset(Register.A1,i));
            }
            varToOffset.put(function.getFuncParams().get(i),curStackOffset);
        }
        for(BasicBlock basicBlock : function.getBasicBlocks()){
            for(Instruction instruction : basicBlock.getInstructions()){
                if(!instruction.getName().isEmpty() && !varToOffset.containsKey(instruction) && !varToReg.containsKey(instruction)){
                    //instruction.print();
                    curStackOffset -= 4;
                    varToOffset.put(instruction,curStackOffset);
                }
            }
        }
        for(BasicBlock basicBlock : function.getBasicBlocks()){
            buildBasicBlock(basicBlock);
        }
    }

    private void buildBasicBlock(BasicBlock basicBlock){
        new Label(curFunction.getName().substring(1) +  "_" + basicBlock.getName());
        for(Instruction instruction : basicBlock.getInstructions()){
            buildInstruction(instruction);
        }
    }

    private void buildInstruction(Instruction instruction){
        Consumer<Instruction> handler = instructionHandlers.get(instruction.getClass());
        if(handler != null){
            //new Comment(instruction);
            handler.accept(instruction);
        }
    }

    private void buildAllocInst(AllocInst allocInst){
        ValueType valueType = ((PointType)allocInst.getValueType()).getTargetType();
        if(valueType instanceof ArrayType){
            curStackOffset -= ((ArrayType) valueType).getLength() * 4;
        }else{
            curStackOffset -= 4;
        }
        if(varToReg.containsKey(allocInst)){
            new CalcAsm(AsmOp.ADDIU,varToReg.get(allocInst),Register.SP,curStackOffset);
        }else{
            new CalcAsm(AsmOp.ADDIU,Register.K0,Register.SP,curStackOffset);
            new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(allocInst));
        }
    }

    private void buildBinaryInst(BinaryInst binaryInst){
        Value value1 = binaryInst.getOperand1();
        Value value2 = binaryInst.getOperand2();
        int cnt = 0;
        if(value1 instanceof ConstInt){
            cnt++;
        }
        if(value2 instanceof ConstInt){
            cnt++;
        }
        Register reg = Register.K0;
        if(varToReg.containsKey(binaryInst)){
            reg = varToReg.get(binaryInst);
        }
        if(cnt == 0){
            calcNonConst(binaryInst,reg);
        }else if(cnt == 1){
            calcOneConst(binaryInst,reg);
        }else{
            calcTwoConst(binaryInst,reg);
        }
        if(!varToReg.containsKey(binaryInst)){
            new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(binaryInst));
        }
    }

    private void calcNonConst(BinaryInst binaryInst,Register register){
        Value value1 = binaryInst.getOperand1();
        Value value2 = binaryInst.getOperand2();
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        if(varToReg.containsKey(value1)){
            register1 = varToReg.get(value1);
        }else{
            new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value1));
        }
        if(varToReg.containsKey(value2)){
            register2 = varToReg.get(value2);
        }else{
            new MemAsm(AsmOp.LW,register2,Register.SP,varToOffset.get(value2));
        }
        OperatorType op = binaryInst.getOperatorType();
        if(op == OperatorType.ADD){
            new CalcAsm(AsmOp.ADDU,register,register1,register2);
        }else if(op == OperatorType.SUB){
            new CalcAsm(AsmOp.SUBU,register,register1,register2);
        }else if(op == OperatorType.MUL){
            //Todo 乘除法这边小心一下吧
            new CalcAsm(AsmOp.MUL,register,register1,register2);
        }else if(op == OperatorType.SDIV){
            new MulDivAsm(AsmOp.DIV,register1,register2);
            new MDRegAsm(AsmOp.MFLO,register);
        }else if(op == OperatorType.SREM){
            new MulDivAsm(AsmOp.DIV,register1,register2);
            new MDRegAsm(AsmOp.MFHI,register);
        }
    }

    private void calcOneConst(BinaryInst binaryInst,Register register){
        Value value1 = binaryInst.getOperand1();
        Value value2 = binaryInst.getOperand2();
        OperatorType op = binaryInst.getOperatorType();
        if(value1 instanceof ConstInt){
            Register register1 = Register.K0;
            if(varToReg.containsKey(value2)){
                register1 = varToReg.get(value2);
            }else{
                new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value2));
            }
            if(op == OperatorType.ADD){
                new CalcAsm(AsmOp.ADDIU,register,register1,((ConstInt) value1).getIntValue());
            }else if(op ==OperatorType.SUB){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value1).getIntValue());
                new CalcAsm(AsmOp.SUBU,register,Register.K1,register1);
            }else if(op == OperatorType.MUL){
                //有待商榷😋
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value1).getIntValue());
                new CalcAsm(AsmOp.MUL,register,Register.K1,register1);
            }else if(op == OperatorType.SDIV){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value1).getIntValue());
                new MulDivAsm(AsmOp.DIV,Register.K1,register1);
                new MDRegAsm(AsmOp.MFLO,register);
            }else if(op == OperatorType.SREM){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value1).getIntValue());
                new MulDivAsm(AsmOp.DIV,Register.K1,register1);
                new MDRegAsm(AsmOp.MFHI,register);
            }
        }else{
            Register register1 = Register.K0;
            if(varToReg.containsKey(value1)){
                register1 = varToReg.get(value1);
            }else{
                new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value1));
            }
            if(op == OperatorType.ADD){
                new CalcAsm(AsmOp.ADDIU,register,register1,((ConstInt) value2).getIntValue());
            }else if(op ==OperatorType.SUB){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value2).getIntValue());
                new CalcAsm(AsmOp.SUBU,register,register1,Register.K1);
            }else if(op == OperatorType.MUL){
                //有待商榷😋
                //ToDo 其实可以进一步优化！
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value2).getIntValue());
                new CalcAsm(AsmOp.MUL,register,register1,Register.K1);
            }else if(op == OperatorType.SDIV){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value2).getIntValue());
                new MulDivAsm(AsmOp.DIV,register1,Register.K1);
                new MDRegAsm(AsmOp.MFLO,register);
            }else if(op == OperatorType.SREM){
                new LiAsm(AsmOp.LI,Register.K1,((ConstInt) value2).getIntValue());
                new MulDivAsm(AsmOp.DIV,register1,Register.K1);
                new MDRegAsm(AsmOp.MFHI,register);
            }
        }
    }

    private void calcTwoConst(BinaryInst binaryInst,Register register){
        ConstInt constInt1 = (ConstInt) binaryInst.getOperand1();
        ConstInt constInt2 = (ConstInt) binaryInst.getOperand2();
        new LiAsm(AsmOp.LI,Register.K0,constInt1.getIntValue());
        new LiAsm(AsmOp.LI,Register.K1,constInt2.getIntValue());
        OperatorType op = binaryInst.getOperatorType();
        if(op == OperatorType.ADD){
            new CalcAsm(AsmOp.ADDU,register,Register.K0,Register.K1);
        }else if(op == OperatorType.SUB){
            new CalcAsm(AsmOp.SUBU,register,Register.K0,Register.K1);
        }else if(op == OperatorType.MUL){
            new CalcAsm(AsmOp.MUL,register,Register.K0,Register.K1);
        }else if(op == OperatorType.SDIV){
            new MulDivAsm(AsmOp.DIV,Register.K0,Register.K1);
            new MDRegAsm(AsmOp.MFLO,register);
        }else if( op == OperatorType.SREM){
            new MulDivAsm(AsmOp.DIV,Register.K0,Register.K1);
            new MDRegAsm(AsmOp.MFHI,register);
        }
    }

    private void buildIcmp(BinaryInst binaryInst){
        boolean flag = true;
        for(User user : binaryInst.getUsers()){
            if(!(user instanceof BrInst)){
                flag = false;
                break;
            }
        }
        if(flag){
            return;
        }
        Value value1 = binaryInst.getOperand1();
        Value value2 = binaryInst.getOperand2();
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        if(value1 instanceof ConstInt){
            new LiAsm(AsmOp.LI,register1,((ConstInt) value1).getIntValue());
        }else if(varToReg.containsKey(value1)){
            register1 = varToReg.get(value1);
        }else{
            new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value1));
        }
        if(value2 instanceof ConstInt){
            new LiAsm(AsmOp.LI,register2,((ConstInt) value2).getIntValue());
        }else if(varToReg.containsKey(value2)){
            register2 = varToReg.get(value2);
        }else{
            new MemAsm(AsmOp.LW,register2,Register.SP,varToOffset.get(value2));
        }
        AsmOp asmOp = switch (binaryInst.getOperatorType()) {
            case ICMP_EQ -> AsmOp.SEQ;
            case ICMP_NE -> AsmOp.SNE;
            case ICMP_SLT -> AsmOp.SLT;
            case ICMP_SLE -> AsmOp.SLE;
            case ICMP_SGT -> AsmOp.SGT;
            case ICMP_SGE -> AsmOp.SGE;
            default -> null;
        };
        if(varToReg.containsKey(binaryInst)){
            new CmpAsm(asmOp,varToReg.get(binaryInst),register1,register2);
        }else{
            new CmpAsm(asmOp,Register.K0,register1,register2);
            new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(binaryInst));
        }
    }

    private void buildBrInst(BrInst brInst){
        if(brInst.getOperands().size() == 1){
            new JumpAsm(AsmOp.J,curFunction.getName().substring(1) +  "_" + brInst.getTrueBlock().getName());
        }else{
            //有点意思🤣其实算是个优化吗？😋
            BinaryInst binaryInst = (BinaryInst) brInst.getCondition();
            boolean flag = true;
            for(User user : binaryInst.getUsers()){
                if(!(user instanceof BrInst)){
                    flag = false;
                    break;
                }
            }
            if(!flag){
                if(varToReg.containsKey(binaryInst)){
                    new BrAsm(AsmOp.BEQ,curFunction.getName().substring(1) +  "_" + brInst.getTrueBlock().getName(),varToReg.get(binaryInst),1);
                }else{
                    new MemAsm(AsmOp.LW,Register.K0,Register.SP,varToOffset.get(binaryInst));
                    new BrAsm(AsmOp.BEQ,curFunction.getName().substring(1) +  "_" + brInst.getTrueBlock().getName(),Register.K0,1);
                }
            }else{
                //ToDo 这里可以进一步优化！
                AsmOp op = switch (binaryInst.getOperatorType()) {
                    case ICMP_EQ -> AsmOp.BEQ;
                    case ICMP_NE -> AsmOp.BNE;
                    case ICMP_SLT -> AsmOp.BLT;
                    case ICMP_SLE -> AsmOp.BLE;
                    case ICMP_SGT -> AsmOp.BGT;
                    case ICMP_SGE -> AsmOp.BGE;
                    default -> null;
                };
                Value value1 = binaryInst.getOperand1();
                Value value2 = binaryInst.getOperand2();
                Register register1 = Register.K0;
                Register register2 = Register.K1;
                if(value1 instanceof ConstInt){
                    new LiAsm(AsmOp.LI,register1,((ConstInt) value1).getIntValue());
                }else if(varToReg.containsKey(value1)){
                    register1 = varToReg.get(value1);
                }else{
                    new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value1));
                }
                if(value2 instanceof ConstInt){
                    new LiAsm(AsmOp.LI,register2,((ConstInt) value2).getIntValue());
                }else if(varToReg.containsKey(value2)){
                    register2 = varToReg.get(value2);
                }else{
                    new MemAsm(AsmOp.LW,register2,Register.SP,varToOffset.get(value2));
                }
                new BrAsm(op,curFunction.getName().substring(1) +  "_" + brInst.getTrueBlock().getName(),register1,register2);
            }
            new JumpAsm(AsmOp.J,curFunction.getName().substring(1) +  "_" + brInst.getFalseBlock().getName());
        }
    }

    private void buildCallInst(CallInst callInst){
        Function function = (Function) callInst.getOperands().get(0);
        if(function.getName().equals("@getint")){
            new LiAsm(AsmOp.LI,Register.V0, 5);
            new SyscallAsm(AsmOp.SYSCALL);
            if (varToReg.containsKey(callInst)) {
                new MoveAsm(AsmOp.MOVE,varToReg.get(callInst), Register.V0);
            } else {
                new MemAsm(AsmOp.SW, Register.V0, Register.SP, varToOffset.get(callInst));
            }
        }else{
            //ToDo 已经分配的寄存器应该有所保存！！！
            ArrayList<Register> allocatedRegs = new ArrayList<>(new HashSet<>(callInst.getActiveReg()));
//            ArrayList<MemAsm> lwAssemblies = new ArrayList<>();
//            ArrayList<MemAsm> swAssemblies = new ArrayList<>();
            for (Register reg : varToReg.values()) {
                if (reg == Register.A1 || reg == Register.A2 || reg == Register.A3) {
                    allocatedRegs.add(reg);
                }
            }
            for (int i = 0; i < allocatedRegs.size(); i++) {
                new MemAsm(AsmOp.SW, allocatedRegs.get(i), Register.SP, curStackOffset - 4 * (i + 1));
            }
            new MemAsm(AsmOp.SW, Register.RA, Register.SP, curStackOffset - 4 * (allocatedRegs.size() + 1));
            for (int i = 1; i < callInst.getOperands().size(); i++) {
                Value value = callInst.getOperands().get(i);
                if (i <= 3) {
                    Register register = Register.getOffset(Register.A0, i);
                    if (value instanceof ConstInt) {
                        new LiAsm(AsmOp.LI,register, ((ConstInt) value) .getIntValue());
                    } else if (varToReg.containsKey(value)) {
                        new MoveAsm(AsmOp.MOVE,register, varToReg.get(value));
                    } else {
                        new MemAsm(AsmOp.LW, register, Register.SP, varToOffset.get(value));
                    }
                } else {
                    Register register = Register.K0;
                    if (value instanceof ConstInt) {
                        new LiAsm(AsmOp.LI,register, ((ConstInt) value) .getIntValue());
                    } else if (varToReg.containsKey(value)) {
                        new MoveAsm(AsmOp.MOVE,register, varToReg.get(value));
                    } else {
                        new MemAsm(AsmOp.LW, register, Register.SP, varToOffset.get(value));
                    }
                    new MemAsm(AsmOp.SW, register, Register.SP, curStackOffset - 4 * (allocatedRegs.size() + i + 1));
                }
            }
            new CalcAsm(AsmOp.ADDIU,Register.SP, Register.SP, curStackOffset - 4 * (allocatedRegs.size() + 1));
            new JumpAsm(AsmOp.JAL,function.getName().substring(1));
            new MemAsm(AsmOp.LW, Register.RA, Register.SP, 0);
            new CalcAsm(AsmOp.ADDIU,Register.SP, Register.SP, -(curStackOffset - 4 * (allocatedRegs.size() + 1)));
            for (int i = 0; i < allocatedRegs.size(); i++) {
                new MemAsm(AsmOp.LW, allocatedRegs.get(i), Register.SP, curStackOffset - 4 * (i + 1));
            }
//            jalAsm.setLoadWords(lwAssemblies);
//            jalAsm.setStoreWords(swAssemblies);
            if (!function.getReturnType().equals(IntegerType.VOID)) {
                if (varToReg.containsKey(callInst)) {
                    new MoveAsm(AsmOp.MOVE,varToReg.get(callInst), Register.V0);
                } else {
                    new MemAsm(AsmOp.SW, Register.V0, Register.SP, varToOffset.get(callInst));
                }
            }
        }
    }

    private void buildGepInst(GepInst gepInst){
        Value value1 = gepInst.getOperands().get(0);
        Value value2 = gepInst.getOperands().get(1);
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        if(value1 instanceof GlobalVar){
            new LaAsm(AsmOp.LA,register1,((GlobalVar) value1).getName().substring(1));
        }else if(varToReg.containsKey(value1)){
            register1 = varToReg.get(value1);
        }else{
            new MemAsm(AsmOp.LW,register1,Register.SP,varToOffset.get(value1));
        }
        if(value2 instanceof ConstInt){
            if(varToReg.containsKey(gepInst)){
                new CalcAsm(AsmOp.ADDIU,varToReg.get(gepInst),register1,4 * ((ConstInt) value2) .getIntValue());
            }else{
                new CalcAsm(AsmOp.ADDIU,Register.K0,register1,4 * ((ConstInt) value2) .getIntValue());
                new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(gepInst));
            }
        }else{
            if(varToReg.containsKey(value2)){
                register2 = varToReg.get(value2);
            }else{
                new MemAsm(AsmOp.LW,register2,Register.SP,varToOffset.get(value2));
            }
            //ToDo 有待商榷
            new CalcAsm(AsmOp.SLL,Register.K1,register2,2);
            if(varToReg.containsKey(gepInst)){
                new CalcAsm(AsmOp.ADDU,varToReg.get(gepInst),register1,Register.K1);
            }else{
                new CalcAsm(AsmOp.ADDU,Register.K0,register1,Register.K1);
                new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(gepInst));
            }
        }
    }

    private void buildPutintInst(PutintInst putintInst){
        Value value = putintInst.getOperands().get(0);
        if(value instanceof ConstInt){
            new LiAsm(AsmOp.LI,Register.A0,((ConstInt) value) .getIntValue());
        }else if(varToReg.containsKey(value)){
            new MoveAsm(AsmOp.MOVE,Register.A0,varToReg.get(value));
        }else{
            new MemAsm(AsmOp.LW,Register.A0,Register.SP,varToOffset.get(value));
        }
        new LiAsm(AsmOp.LI,Register.V0, 1);
        new SyscallAsm(AsmOp.SYSCALL);
    }

    private void buildPutstrInst(PutstrInst putstrInst){
        //ToDo 可以优化吗？
        ConstString constString = (ConstString) putstrInst.getOperands().get(0);
        new LaAsm(AsmOp.LA,Register.A0, constString.getName().substring(2));
        new LiAsm(AsmOp.LI,Register.V0, 4);
        new SyscallAsm(AsmOp.SYSCALL);
    }

    private void buildLoadInst(LoadInst loadInst){
        Value value = loadInst.getOperands().get(0);
        Register reg = Register.K0;
        if(value instanceof GlobalVar){
            new LaAsm(AsmOp.LA,Register.K0,value.getName().substring(1));
        }else if(varToReg.containsKey(value)){
            reg = varToReg.get(value);
        }else{
            new MemAsm(AsmOp.LW,Register.K0,Register.SP,varToOffset.get(value));
        }
        if(varToReg.containsKey(loadInst)){
            //ToDo 有待商榷 就这一条感觉store也可以啊hhh🤔
            //仔细思考发现不对🤣
            new MemAsm(AsmOp.LW,varToReg.get(loadInst),reg,0);
        }else{
            new MemAsm(AsmOp.LW,Register.K0,reg,0);
            new MemAsm(AsmOp.SW,Register.K0,Register.SP,varToOffset.get(loadInst));
        }
    }

    private void buildRetInst(RetInst retInst){
        if (isInMain) {
            new LiAsm(AsmOp.LI,Register.V0, 10);
            new SyscallAsm(AsmOp.SYSCALL);
        } else {
            if (!retInst.getOperands().isEmpty()) {
                Value value = retInst.getOperands().get(0);
                if (value instanceof ConstInt) {
                    new LiAsm(AsmOp.LI,Register.V0, ((ConstInt) value).getIntValue());
                } else if (varToReg.containsKey(value)) {
                    //ToDo 不用move会报错，please小心！
                    new MoveAsm(AsmOp.MOVE,Register.V0, varToReg.get(value));
                } else {
                    new MemAsm(AsmOp.LW, Register.V0, Register.SP, varToOffset.get(value));
                }
            }
            new JumpAsm(AsmOp.JR, Register.RA);
        }
    }

    private void buildStoreInst(StoreInst storeInst){
        //ToDo 顺序可以注意一下
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        if(storeInst.getOperands().get(1) instanceof ConstInt){
            new LiAsm(AsmOp.LI,Register.K0,((ConstInt)storeInst.getOperands().get(1)).getIntValue());
        }else if(varToReg.containsKey(storeInst.getOperands().get(1))){
//            new MemAsm(AsmOp.LW,Register.K0,varToReg.get(storeInst.getOperands().get(1)),0);
            register1 = varToReg.get(storeInst.getOperands().get(1));
        }else{
            new MemAsm(AsmOp.LW,Register.K0,Register.SP,varToOffset.get(storeInst.getOperands().get(1)));
        }
        if(storeInst.getOperands().get(0) instanceof GlobalVar){
            new LaAsm(AsmOp.LA,Register.K1,storeInst.getOperands().get(0).getName().substring(1));
        }else if(varToReg.containsKey(storeInst.getOperands().get(0))){
            register2 = varToReg.get(storeInst.getOperands().get(0));
        }else{
            //((Instruction)storeInst.getOperands().get(0)).print();
            //storeInst.print();
            new MemAsm(AsmOp.LW,Register.K1,Register.SP,varToOffset.get(storeInst.getOperands().get(0)));
        }
        new MemAsm(AsmOp.SW,register1,register2,0);
    }

    private void buildZextInst(ZextInst zextInst){
        Value value = zextInst.getOperands().get(0);
        Register register = Register.K0;
        if(value instanceof ConstInt){
            new LiAsm(AsmOp.LI,register,((ConstInt) value).getIntValue());
        }else if(varToReg.containsKey(value)){
            register = varToReg.get(value);
        }else{
            new MemAsm(AsmOp.LW,register,Register.SP,varToOffset.get(value));
        }
        if(varToReg.containsKey(zextInst)){
            new MoveAsm(AsmOp.MOVE,varToReg.get(zextInst),register);
        }else{
            new MemAsm(AsmOp.SW,register,Register.SP,varToOffset.get(zextInst));
        }
    }
}
