package fronted;

public class FuncDef extends Node {
    private FuncType funcType;
    private Token ident;
    private FuncFParams funcParmas;
    private Block block;

    public FuncDef(FuncType funcType, Token ident, FuncFParams funcParmas, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.funcParmas = funcParmas;
        this.block = block;
    }

    public FuncType getFuncType() {
        return funcType;
    }

    public Token getIdent() {
        return ident;
    }

    public FuncFParams getFuncParmas() {
        return funcParmas;
    }

    public Block getBlock() {
        return block;
    }

    public void print() {
        funcType.print();
        System.out.println(ident.toString());
        System.out.println(TokenType.LPARENT.print());
        if (funcParmas != null) {
            funcParmas.print();
        }
        System.out.println(TokenType.RPARENT.print());
        block.print();
        System.out.println("<FuncDef>");
    }
}
