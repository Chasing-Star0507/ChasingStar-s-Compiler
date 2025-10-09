package fronted;

public class Cond extends Node{
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp){
        this.lOrExp = lOrExp;
    }

    public void print(){
        lOrExp.print();
        System.out.println("<Cond>");
    }
}
