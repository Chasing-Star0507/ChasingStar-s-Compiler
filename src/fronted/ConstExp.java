package fronted;

public class ConstExp  extends Node{
    private AddExp addExp;

    public ConstExp(AddExp addExp){
        this.addExp = addExp;
    }

    public void print(){
        addExp.print();
        System.out.println("<ConstExp>");
    }
}
