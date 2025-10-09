package fronted;

public class Exp extends Node{
    private AddExp addExp;

    public Exp(AddExp addExp){
        this.addExp = addExp;
    }

    public void print(){
        addExp.print();
        System.out.println("<Exp>");
    }
}
