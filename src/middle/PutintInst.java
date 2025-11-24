package middle;

public class PutintInst extends Instruction{
    public PutintInst(Value value){
        super("",IntegerType.VOID,OperatorType.CALL);
        //有待商榷🤔
        addOperand(value);
    }

    public void print(){
        System.out.println("\t" + "call void @putint(i32 " + getOperands().get(0).getName() + ")");
    }
}
