package middle;

import java.util.ArrayList;

public class Value {
    private String name;
    private ValueType valueType;
    private ArrayList<User> users = new ArrayList<>();

    public Value(String name,ValueType valueType){
        this.name = name;
        this.valueType = valueType;
    }

    public void addUse(User user){
        users.add(user);
    }

    public String getName() {
        return name;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setName(String name){
        this.name = name;
    }
}
