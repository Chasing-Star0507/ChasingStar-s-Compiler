package middle;

public class LabelType extends ValueType{
    private static int cnt = 1;
    private int id;

    public LabelType(){
        this.id = cnt++;
    }
}
