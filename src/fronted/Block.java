package fronted;

import java.util.ArrayList;

public class Block extends Node {
    private ArrayList<BlockItem> blockItems = new ArrayList<>();
    private int endLineNum;

    public Block(ArrayList<BlockItem> blockItems, int endLineNum) {
        this.blockItems = blockItems;
        this.endLineNum = endLineNum;
    }

    public ArrayList<BlockItem> getBlockItems() {
        return blockItems;
    }

    public int getEndLineNum() {
        return endLineNum;
    }

    public void print() {
        System.out.println(TokenType.LBRACE.print());
        for (BlockItem blockItem : blockItems) {
            blockItem.print();
        }
        System.out.println(TokenType.RBRACE.print());
        System.out.println("<Block>");
    }
}
