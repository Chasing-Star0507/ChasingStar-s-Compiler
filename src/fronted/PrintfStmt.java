package fronted;

import java.util.ArrayList;

public class PrintfStmt extends Stmt{
    private Token token;
    private ArrayList<Exp> exps = new ArrayList<>();

    public PrintfStmt(Token token,ArrayList<Exp> exps){
        this.token = token;
        this.exps = exps;
    }

    public void print(){
        System.out.println(TokenType.PRINTFTK.print());
        System.out.println(TokenType.LPARENT.print());
        System.out.println(token.toString());
        for(Exp exp : exps){
            System.out.println(TokenType.COMMA.print());
            exp.print();
        }
        System.out.println(TokenType.RPARENT.print());
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<Stmt>");
    }
}
