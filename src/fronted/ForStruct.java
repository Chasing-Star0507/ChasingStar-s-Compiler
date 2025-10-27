package fronted;

public class ForStruct extends Stmt {
    private ForStmt forStmt1;
    private Cond cond;
    private ForStmt forStmt2;
    private Stmt stmt;

    public ForStruct(ForStmt forStmt1, Cond cond, ForStmt forStmt2, Stmt stmt) {
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
    }

    public ForStmt getForStmt1() {
        return forStmt1;
    }

    public Cond getCond() {
        return cond;
    }

    public ForStmt getForStmt2() {
        return forStmt2;
    }

    public Stmt getStmt() {
        return stmt;
    }

    public void print() {
        System.out.println(TokenType.FORTK.print());
        System.out.println(TokenType.LPARENT.print());
        if (forStmt1 != null) {
            forStmt1.print();
        }
        System.out.println(TokenType.SEMICN.print());
        if (cond != null) {
            cond.print();
        }
        System.out.println(TokenType.SEMICN.print());
        if (forStmt2 != null) {
            forStmt2.print();
        }
        System.out.println(TokenType.RPARENT.print());
        stmt.print();
        System.out.println("<Stmt>");
    }
}
