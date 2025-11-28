package backed;

public class CmpAsm extends TextAssembly {
    private AsmOp op;
    private Register rd;
    private Register rs;
    private Register rt;

    public CmpAsm(AsmOp op, Register rd, Register rs, Register rt) {
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public void print() {
        System.out.println("    " + op + " " + rd + ", " + rs + ", " + rt);
    }
}
