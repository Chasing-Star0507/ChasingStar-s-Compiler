package middle;

public class ZextInst extends Instruction {
    public ZextInst(Value value) {
        super(IntegerType.i32, OperatorType.ZEXT);
        addOperand(value);
    }

    public void print() {
        System.out.println("\t" + getName() + " = zext i1 " + getOperands().get(0).getName() + " to i32");
    }
}
