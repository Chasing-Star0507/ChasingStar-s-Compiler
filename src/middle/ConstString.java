package middle;

public class ConstString extends Value {
    private String stringValue;

    public ConstString(String name, String stringValue) {
        super(name, new PointType(new ArrayType(stringValue.length() + 1, IntegerType.i8)));
        this.stringValue = stringValue.replace("\n", "\\0A");
        Module.getINSTANCE().addConstString(this);
        IRData.putConstString(stringValue, this);
    }

    public String getStringValue() {
        return stringValue;
    }

    public void print() {
        System.out.println(getName() + " = private unnamed_addr constant "
                + ((PointType) getValueType()).getTargetType()
                + " c\"" + stringValue + "\\00\"" + ", align 1");
    }
}
