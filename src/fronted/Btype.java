package fronted;

public class Btype extends Node{
    private TokenType tokenType;

    public Btype(TokenType tokenType){
        this.tokenType = tokenType;
    }

    public void print(){
        System.out.println(tokenType.print());
    }
}
