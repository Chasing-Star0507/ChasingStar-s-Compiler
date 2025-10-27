package fronted;

import java.util.ArrayList;

public class ForStmt extends Node {
    private ArrayList<LValExpStmt> lValExpStmts = new ArrayList<>();

    public ForStmt(ArrayList<LValExpStmt> lValExpStmts) {
        this.lValExpStmts = lValExpStmts;
    }

    public ArrayList<LValExpStmt> getlValExpStmts() {
        return lValExpStmts;
    }

    public void print() {
        for (int i = 0; i < lValExpStmts.size(); i++) {
            if (i > 0) {
                System.out.println(TokenType.COMMA.print());
            }
            lValExpStmts.get(i).forStmtPrint();
        }
        System.out.println("<ForStmt>");
    }
}
