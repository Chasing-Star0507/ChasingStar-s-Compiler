package fronted;

public class ConstDef extends Node{
    private Token ident;
    private ConstExp constExp;
    private ConstInitVal constInitival;

    public ConstDef(Token ident, ConstExp constExp, ConstInitVal constInitival){
        this.ident = ident;
        this.constExp = constExp;
        this.constInitival = constInitival;
    }

    public void print(){
        System.out.println(ident.toString());
        if(constExp != null){
            System.out.println(TokenType.LBRACK.print());
            constExp.print();
            System.out.println(TokenType.RBRACK.print());
        }
        System.out.println(TokenType.ASSIGN.print());
        constInitival.print();
        System.out.println("<ConstDef>");
    }
}
