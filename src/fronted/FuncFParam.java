package fronted;

public class FuncFParam extends Node {
    private int type;
    private Btype btype;
    private Token ident;

    public FuncFParam(int type, Btype btype, Token ident) {
        this.type = type;
        this.btype = btype;
        this.ident = ident;
    }

    public int getType() {
        return type;
    }

    public Btype getBtype() {
        return btype;
    }

    public Token getIdent() {
        return ident;
    }

    public void print() {
        btype.print();
        System.out.println(ident.toString());
        if (type == 1) {
            System.out.println(TokenType.LBRACK.print());
            System.out.println(TokenType.RBRACK.print());
        }
        System.out.println("<FuncFParam>");
    }
}
