package middle;

public class RetInst extends Instruction{
    public RetInst(Value value){
        super("",IntegerType.VOID,OperatorType.RET);
        if(value != null){
            addOperand(value);
        }
    }

    public void print(){
        if (!getOperands().isEmpty()) {
            System.out.println("\t" + "ret i32 " + getOperands().get(0).getName());
        } else {
            System.out.println("\t" + "ret void");
        }
    }
}
