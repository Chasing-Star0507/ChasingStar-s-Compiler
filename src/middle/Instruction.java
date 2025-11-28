package middle;

public abstract class Instruction extends User {
    private OperatorType operatorType;
    private BasicBlock basicBlock;

    public Instruction(ValueType valueType, OperatorType operatorType) {
        super(IRData.getVarName(), valueType);
        this.operatorType = operatorType;
        this.basicBlock = IRData.getCurBasicBlock();
        basicBlock.addInstruction(this);
    }

    public Instruction(String name, ValueType valueType, OperatorType operatorType) {
        super(name, valueType);
        this.operatorType = operatorType;
        this.basicBlock = IRData.getCurBasicBlock();
        basicBlock.addInstruction(this);
    }

    public abstract void print();
}
