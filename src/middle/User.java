package middle;

import java.util.ArrayList;

public class User extends Value{
    private ArrayList<Value> operands = new ArrayList<>();

    public User(String name,ValueType valueType){
        super(name,valueType);
    }

    public void addOperand(Value value){
        operands.add(value);
        value.addUse(this);
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }
}
