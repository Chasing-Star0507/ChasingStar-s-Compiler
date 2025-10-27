package fronted;

public class LValExpStmt extends Stmt {
    private LVal lVal;
    private Exp exp;

    public LValExpStmt(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Exp getExp() {
        return exp;
    }

    public void print() {
        lVal.print();
        System.out.println(TokenType.ASSIGN.print());
        exp.print();
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }

    public void forStmtPrint() {
        lVal.print();
        System.out.println(TokenType.ASSIGN.print());
        exp.print();
    }
}
