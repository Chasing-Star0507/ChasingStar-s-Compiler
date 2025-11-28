package middle;

public class IntegerType extends ValueType {
    private int bits;

    public IntegerType(int bits) {
        this.bits = bits;
    }

    public static IntegerType VOID = new IntegerType(0);
    public static IntegerType i32 = new IntegerType(32);
    public static IntegerType i8 = new IntegerType(8);
    public static IntegerType i1 = new IntegerType(1);

    public String toString() {
        return "i" + bits;
    }
}
