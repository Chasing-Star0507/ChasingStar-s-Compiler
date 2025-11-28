package middle;

public class BinaryInst extends Instruction{
    private OperatorType operatorType;

    public BinaryInst(OperatorType operatorType,Value value1,Value value2){
        super(getType(operatorType),operatorType);
        //ToDo 这里不对！！！
        this.operatorType = operatorType;
        addOperand(value1);
        addOperand(value2);
    }

    private static ValueType getType(OperatorType operatorType){
        if(OperatorType.isLogicCond(operatorType)){
            return IntegerType.i1;
        }else{
            return IntegerType.i32;
        }
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Value getOperand1(){
        return getOperands().get(0);
    }

    public Value getOperand2(){
        return getOperands().get(1);
    }

    public void print(){
        System.out.println("\t" + getName() + " = " + operatorType.toString() + " i32 " + getOperand1().getName() + ", " + getOperand2().getName());
    }
}
