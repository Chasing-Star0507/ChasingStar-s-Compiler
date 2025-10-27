package fronted;

public class PrimaryExp extends Node {
    private Exp exp;
    private LVal lVal;
    private Number number;

    public PrimaryExp(Exp exp, LVal lVal, Number number) {
        this.exp = exp;
        this.lVal = lVal;
        this.number = number;
    }

    public Exp getExp() {
        return exp;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Number getNumber() {
        return number;
    }

    public void print() {
        if (exp != null) {
            System.out.println(TokenType.LPARENT.print());
            exp.print();
            System.out.println(TokenType.RPARENT.print());
        } else if (lVal != null) {
            lVal.print();
        } else {
            number.print();
        }
        System.out.println("<PrimaryExp>");
    }
}
