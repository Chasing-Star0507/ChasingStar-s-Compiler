package fronted;

public class MainFuncDef extends Node {
    private Block block;

    public MainFuncDef(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void print() {
        System.out.println(TokenType.INTTK.print());
        System.out.println(TokenType.MAINTK.print());
        System.out.println(TokenType.LPARENT.print());
        System.out.println(TokenType.RPARENT.print());
        block.print();
        System.out.println("<MainFuncDef>");
    }
}
