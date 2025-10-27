package fronted;

public class Token {
    private TokenType tokenType;
    private String tokenContent;
    private int lineNum;

    public Token(TokenType tokenType, String tokenContent, int lineNum) {
        this.tokenType = tokenType;
        this.tokenContent = tokenContent;
        this.lineNum = lineNum;
    }

    public String getTokenContent() {
        return tokenContent;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String toString() {
        return tokenType.name() + " " + tokenContent;
    }
}
