package fronted;

import java.util.ArrayList;

public class FuncRParams extends Node{
    private ArrayList<Exp> exps = new ArrayList<>();

    public FuncRParams(ArrayList<Exp> exps){
        this.exps = exps;
    }

    public void print(){
        for(int i = 0;i < exps.size();i++){
            if(i > 0){
                System.out.println(TokenType.COMMA.print());
            }
            exps.get(i).print();
        }
        System.out.println("<FuncRParams>");
    }
}
