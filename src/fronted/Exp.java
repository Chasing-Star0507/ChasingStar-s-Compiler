package fronted;

public class Exp extends Node implements Calculation {
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void print() {
        addExp.print();
        System.out.println("<Exp>");
    }

    public int calculate() {
        return addExp.calculate();
    }
}
