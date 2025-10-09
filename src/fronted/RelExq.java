package fronted;

import java.util.ArrayList;

public class RelExq extends Node{
    private ArrayList<AddExp> addExps = new ArrayList<>();
    private ArrayList<Token> symbols;

    public RelExq(ArrayList<AddExp> addExps,ArrayList<Token> symbols){
        this.addExps = addExps;
        this.symbols = symbols;
    }

    public void print(){
        for(int i = 0;i < addExps.size();i++){
            if(i > 0){
                System.out.println("<RelExp>");
                System.out.println(symbols.get(i - 1).toString());
            }
            addExps.get(i).print();
        }
        System.out.println("<RelExp>");
    }
}
