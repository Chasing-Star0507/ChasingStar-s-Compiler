package fronted;

public class ExpStmt extends Stmt {
    private Exp exp;

    public ExpStmt(Exp exp) {
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    public void print() {
        if (exp != null) {
            exp.print();
        }
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
