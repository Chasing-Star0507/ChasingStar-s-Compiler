package fronted;

public class BreakStmt extends Stmt {
    private Token token;

    public BreakStmt(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void print() {
        System.out.println(TokenType.BREAKTK.print());
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
