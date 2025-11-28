package middle;

public class FuncParam extends Value {
    public FuncParam(String name, ValueType valueType) {
        super(name, valueType);
    }

    public void print() {
        if (getValueType() instanceof IntegerType) {
            System.out.print("i32 " + getName());
        } else {
            System.out.print("i32* " + getName());
        }
    }
}
