package middle;

public class LoadInst extends Instruction{
    public LoadInst(Value pointer) {
        super(IntegerType.i32, OperatorType.LOAD);
        addOperand(pointer);
    }

    public void print(){
        System.out.println("\t" + getName() + " = load i32, i32* " + getOperands().get(0).getName());
    }
}
