package middle;

import java.util.ArrayList;

public class CallInst extends Instruction{
    public CallInst(Function calledFunction, ArrayList<Value> parameters) {
        super("", calledFunction.getReturnType(), OperatorType.CALL);
        addOperand(calledFunction);
        for (Value param : parameters) {
            addOperand(param);
        }
        if (!calledFunction.getReturnType().equals(IntegerType.VOID)) {
            setName(IRData.getVarName());
        }
    }

    public void print(){
        StringBuilder sb = new StringBuilder();
        Function function = (Function) getOperands().get(0);
        sb.append(getOperands().get(0).getName() + "(");
        for(int i = 1;i < getOperands().size();i++){
            ValueType valueType = function.getFuncParams().get(i - 1).getValueType();
            if(valueType instanceof IntegerType){
                if(i > 1){
                    sb.append(", i32 " + getOperands().get(i).getName());
                }else{
                    sb.append("i32 " + getOperands().get(i).getName());
                }
            }else{
                if(i > 1){
                    sb.append(", i32* " + getOperands().get(i).getName());
                }else{
                    sb.append("i32* " + getOperands().get(i).getName());
                }
            }
        }
        sb.append(")");
        if(getValueType().equals(IntegerType.VOID)){
            System.out.println("\tcall void " + sb);
        }else{
            System.out.println("\t" + getName() + " = " + "call i32 " + sb);
        }
    }
}
