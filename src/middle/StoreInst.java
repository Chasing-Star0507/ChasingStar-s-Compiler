package middle;

public class StoreInst extends Instruction {
    public StoreInst(Value lValue, Value rValue) {
        super("", IntegerType.VOID, OperatorType.STORE);
        addOperand(lValue);
        addOperand(rValue);
    }

    public void print() {
        System.out.println("\t" + "store i32 " + getOperands().get(1).getName() + ", i32* " + getOperands().get(0).getName());
    }
}
