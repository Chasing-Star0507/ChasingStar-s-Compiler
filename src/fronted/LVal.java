package fronted;

public class LVal extends Node{
    private Token ident;
    private Exp exp;

    public LVal(Token ident,Exp exp){
        this.ident = ident;
        this.exp = exp;
    }

    public void print(){
        System.out.println(ident.toString());
        if(exp != null){
            System.out.println(TokenType.LBRACK.print());
            exp.print();
            System.out.println(TokenType.RBRACK.print());
        }
        System.out.println("<LVal>");
    }
}
