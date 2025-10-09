package fronted;

import java.util.ArrayList;

public class Block extends Node{
    private ArrayList<BlockItem> blockItems = new ArrayList<>();

    public Block(ArrayList<BlockItem> blockItems){
        this.blockItems = blockItems;
    }

    public void print(){
        System.out.println(TokenType.LBRACE.print());
        for(BlockItem blockItem : blockItems){
            blockItem.print();
        }
        System.out.println(TokenType.RBRACE.print());
        System.out.println("<Block>");
    }
}
