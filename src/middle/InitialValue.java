package middle;

import java.util.ArrayList;

public class InitialValue {
    private ValueType valueType;
    private int length;
    private ArrayList<Integer> elements;

    public InitialValue(ValueType valueType, int length, ArrayList<Integer> integers) {
        this.valueType = valueType;
        this.length = length;
        this.elements = integers;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Integer> getElements() {
        return elements;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (valueType.equals(IntegerType.i32)) {
            if (elements.isEmpty()) {
                sb.append("i32 0");
            } else {
                sb.append("i32 " + elements.get(0));
            }
        } else {
            sb.append("[" + length + " x i32]");
            if (elements.isEmpty()) {
                sb.append(" zeroinitializer");
            } else {
                sb.append(" [");
                sb.append("i32 " + elements.get(0));
                for (int i = 1; i < length; i++) {
                    sb.append(", ");
                    if (i < elements.size()) {
                        sb.append("i32 " + elements.get(i));
                    } else {
                        sb.append("i32 0");
                    }
                }
                sb.append("]");
            }
        }
        return sb.toString();
    }
}
