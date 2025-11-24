package middle;

public class PutstrInst extends Instruction{
    public PutstrInst(Value value){
        super("",IntegerType.VOID,OperatorType.CALL);
        addOperand(value);
    }

    public void print(){
        ConstString constString = (ConstString) getOperands().get(0);
        PointType pointerType = (PointType) constString.getValueType();
        System.out.println("\t" + "call void @putstr(i8* getelementptr inbounds (" +
         pointerType.getTargetType().toString() + ", " +
                pointerType.getTargetType().toString() + "* " +
                constString.getName() +
                ", i64 0, i64 0))");
    }
}
