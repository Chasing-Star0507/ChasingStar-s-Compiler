package fronted;

public class UnaryExp extends Node implements Calculation {
    private PrimaryExp primaryExp;
    private Token ident;
    private FuncRParams funcRParams;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(PrimaryExp primaryExp, Token ident, FuncRParams funcRParams, UnaryOp unaryOp, UnaryExp unaryExp) {
        this.primaryExp = primaryExp;
        this.ident = ident;
        this.funcRParams = funcRParams;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public PrimaryExp getPrimaryExp() {
        return primaryExp;
    }

    public Token getIdent() {
        return ident;
    }

    public FuncRParams getFuncRParams() {
        return funcRParams;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    public void print() {
        if (primaryExp != null) {
            primaryExp.print();
        } else if (ident != null) {
            System.out.println(ident.toString());
            System.out.println(TokenType.LPARENT.print());
            if (funcRParams != null) {
                funcRParams.print();
            }
            System.out.println(TokenType.RPARENT.print());
        } else {
            unaryOp.print();
            unaryExp.print();
        }
        System.out.println("<UnaryExp>");
    }

    public int calculate() {
        int res = 0;
        if (unaryOp != null && unaryExp != null) {
            if (unaryOp.getOp().getTokenType() == TokenType.PLUS) {
                res = unaryExp.calculate();
            } else if (unaryOp.getOp().getTokenType() == TokenType.MINU) {
                res = -unaryExp.calculate();
            } else if (unaryOp.getOp().getTokenType() == TokenType.NOT) {
                res = unaryExp.calculate() == 0 ? 1 : 0;
            }
        } else if (primaryExp != null) {
            res = primaryExp.calculate();
        }
        return res;
    }
}
