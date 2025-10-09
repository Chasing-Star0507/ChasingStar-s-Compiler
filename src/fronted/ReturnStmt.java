package fronted;

public class ReturnStmt extends Stmt{
    private Exp exp;

    public ReturnStmt(Exp exp){
        this.exp = exp;
    }

    public void print(){
        System.out.println(TokenType.RETURNTK.print());
        if(exp != null){
            exp.print();
        }
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
