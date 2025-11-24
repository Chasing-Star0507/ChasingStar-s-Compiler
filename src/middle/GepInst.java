package middle;

public class GepInst extends Instruction{
    public GepInst(Value value, Value index) {
        super(new PointType(IntegerType.i32), OperatorType.GEP);
        addOperand(value);
        addOperand(index);
    }

    public void print(){
        Value pointer = getOperands().get(0);
        PointType pointerType = (PointType) pointer.getValueType();
        ValueType targetType = pointerType.getTargetType();
        StringBuilder sb = new StringBuilder(getName())
                .append(" = getelementptr inbounds ")
                .append(targetType)
                .append(", ")
                .append(targetType + "*")
                .append(" ")
                .append(pointer.getName());
        if (targetType instanceof ArrayType) {
            sb.append(", i32 0, i32 ");
        } else {
            sb.append(", i32 ");
        }
        sb.append(getOperands().get(1).getName());
        System.out.println("\t" + sb);
    }
}
