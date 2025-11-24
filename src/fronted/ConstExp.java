package fronted;

public class ConstExp extends Node implements Calculation{
    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void print() {
        addExp.print();
        System.out.println("<ConstExp>");
    }

    public int calculate(){
        return addExp.calculate();
    }
}
