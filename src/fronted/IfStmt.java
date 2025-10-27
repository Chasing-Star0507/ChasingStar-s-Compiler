package fronted;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    private Cond cond;
    private ArrayList<Stmt> stmts = new ArrayList<>();

    public IfStmt(Cond cond, ArrayList<Stmt> stmts) {
        this.cond = cond;
        this.stmts = stmts;
    }

    public Cond getCond() {
        return cond;
    }

    public ArrayList<Stmt> getStmts() {
        return stmts;
    }

    public void print() {
        System.out.println(TokenType.IFTK.print());
        System.out.println(TokenType.LPARENT.print());
        cond.print();
        System.out.println(TokenType.RPARENT.print());
        if (stmts.size() > 1) {
            stmts.get(0).print();
            System.out.println(TokenType.ELSETK.print());
            stmts.get(1).print();
        } else {
            stmts.get(0).print();
        }
        System.out.println("<Stmt>");
    }
}
