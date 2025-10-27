package fronted;

public class ContinueStmt extends Stmt {
    private Token token;

    public ContinueStmt(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void print() {
        System.out.println(TokenType.CONTINUETK.print());
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
