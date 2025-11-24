package fronted;

public class UnaryOp extends Node {
    private Token op;

    public UnaryOp(Token op) {
        this.op = op;
    }

    public Token getOp() {
        return op;
    }

    public void print() {
        System.out.println(op.toString());
        System.out.println("<UnaryOp>");
    }
}
