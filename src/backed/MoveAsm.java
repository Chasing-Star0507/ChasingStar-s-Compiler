package backed;

public class MoveAsm {
    private AsmOp op;
    private Register rd;
    private Register rs;

    public MoveAsm(AsmOp op, Register rd, Register rs) {
        this.op = op;
        this.rd = rd;
        this.rs = rs;
    }

    public void print() {
        System.out.println("    " + op + " " + rd + ", " + rs);
    }
}
