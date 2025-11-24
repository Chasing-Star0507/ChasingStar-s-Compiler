package fronted;

public class Number extends Node implements Calculation{
    private Token intConst;

    public Number(Token intConst) {
        this.intConst = intConst;
    }

    public void print() {
        System.out.println(intConst.toString());
        System.out.println("<Number>");
    }

    public int calculate(){
        return Integer.valueOf(intConst.getTokenContent());
        //ToDo 要小心！！！
    }
}
