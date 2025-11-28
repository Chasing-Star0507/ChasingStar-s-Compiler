package backed;

public class MDRegAsm extends TextAssembly {
    private AsmOp op;
    private Register rd;

    public MDRegAsm(AsmOp op, Register rd) {
        this.op = op;
        this.rd = rd;
    }

    public void print() {
        System.out.println("    " + op + " " + rd);
    }
}
