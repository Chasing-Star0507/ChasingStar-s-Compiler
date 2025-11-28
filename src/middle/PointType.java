package middle;

public class PointType extends ValueType {
    private ValueType targetType;

    public PointType(ValueType valueType) {
        this.targetType = valueType;
    }

    public ValueType getTargetType() {
        return targetType;
    }

    public String toString() {
        return "i32*";
    }
}
