package fronted;

public class Number extends Node{
    private Token intConst;

    public Number(Token intConst){
        this.intConst = intConst;
    }

    public void print(){
        System.out.println(intConst.toString());
        System.out.println("<Number>");
    }
}
