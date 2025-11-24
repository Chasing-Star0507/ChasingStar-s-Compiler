package middle;

public class BrInst extends Instruction{
    public BrInst(Value value,BasicBlock basicBlock1,BasicBlock basicBlock2){
        super("",IntegerType.VOID,OperatorType.BR);
        addOperand(value);
        addOperand(basicBlock1);
        addOperand(basicBlock2);
    }

    public BrInst(BasicBlock basicBlock){
        super("",IntegerType.VOID,OperatorType.BR);
        addOperand(basicBlock);
    }

    public Value getCondition(){
        return getOperands().get(0);
    }

    public Value getTrueBlock(){
        if(getOperands().size() == 3){
            return getOperands().get(1);
        }else{
            return getOperands().get(0);
        }
    }

    public Value getFalseBlock(){
        return getOperands().get(2);
    }

    public void print(){
        if (getOperands().size() == 3) {
            System.out.println("\t" + "br i1 " + getCondition().getName()
                    + ", label %" + getTrueBlock().getName()
                    + ", label %" + getFalseBlock().getName());
        } else if (getOperands().size() == 1) {
            System.out.println("\t" + "br label %" + getTrueBlock().getName());
        }
    }
}
