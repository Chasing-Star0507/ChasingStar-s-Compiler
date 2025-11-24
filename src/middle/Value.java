package middle;

public class Value {
    private String name;
    private ValueType valueType;

    public Value(String name,ValueType valueType){
        this.name = name;
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setName(String name){
        this.name = name;
    }
}
