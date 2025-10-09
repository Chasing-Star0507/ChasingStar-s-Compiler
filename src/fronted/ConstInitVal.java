package fronted;

import java.util.ArrayList;

public class ConstInitVal extends Node{
    private int type;
    private ArrayList<ConstExp> constExps = new ArrayList<>();

    public ConstInitVal(int type,ArrayList<ConstExp> constExps){
        this.type = type;
        this.constExps = constExps;
    }

    public void print(){
        if(type == 0){
            constExps.get(0).print();
        }else{
            System.out.println(TokenType.LBRACE.print());
            for(int i = 0;i < constExps.size();i++){
                if(i > 0){
                    System.out.println(TokenType.COMMA.print());
                }
                constExps.get(i).print();
            }
            System.out.println(TokenType.RBRACE.print());
        }
        System.out.println("<ConstInitVal>");
    }
}
