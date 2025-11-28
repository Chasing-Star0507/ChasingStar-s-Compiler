package middle;

public class ConstInt extends Value {
    private int intValue;

    public ConstInt(int intValue) {
        super(String.valueOf(intValue), IntegerType.i32);
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
