package fronted;

public class VarDef extends Node{
    private Token ident;
    private ConstExp constExp = null;
    private InitVal initVal = null;

    public VarDef(Token ident,ConstExp constExp,InitVal initVal){
        this.ident = ident;
        this.constExp = constExp;
        this.initVal = initVal;
    }

    public void print(){
        System.out.println(ident.toString());
        if(constExp != null){
            System.out.println(TokenType.LBRACK.print());
            constExp.print();
            System.out.println(TokenType.RBRACK.print());
        }
        if(initVal != null){
            System.out.println(TokenType.ASSIGN.print());
            initVal.print();
        }
        System.out.println("<VarDef>");
    }
}
