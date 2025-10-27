package fronted;

public class FuncType extends Node {
    private TokenType tokenType;

    public FuncType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void print() {
        System.out.println(tokenType.print());
        System.out.println("<FuncType>");
    }
}
