package middle;

public class ArrayType extends ValueType{
    private ValueType valueType;
    private int length;

    public ArrayType(int length ,ValueType valueType){
        this.length = length;
        this.valueType = valueType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public int getLength() {
        return length;
    }

    public String toString(){
        return "[" + length + " x " + valueType.toString() + "]";
    }
}
