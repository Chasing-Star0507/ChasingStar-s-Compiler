package fronted;

import java.util.ArrayList;

public class LOrExp extends Node{
    private ArrayList<LAndExp> lAndExps = new ArrayList<>();

    public LOrExp(ArrayList<LAndExp> lAndExps){
        this.lAndExps = lAndExps;
    }

    public void print(){
        for(int i = 0;i < lAndExps.size();i++){
            if(i > 0){
                System.out.println("<LOrExp>");
                System.out.println(TokenType.OR.print());
            }
            lAndExps.get(i).print();
        }
        System.out.println("<LOrExp>");
    }
}
