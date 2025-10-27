package fronted;

public class ReturnStmt extends Stmt {
    private Token token;
    private Exp exp;

    public ReturnStmt(Token token, Exp exp) {
        this.token = token;
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    public Token getToken() {
        return token;
    }

    public void print() {
        System.out.println(TokenType.RETURNTK.print());
        if (exp != null) {
            exp.print();
        }
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
