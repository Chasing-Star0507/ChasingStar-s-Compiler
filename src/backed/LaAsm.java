package backed;

public class LaAsm extends TextAssembly {
    private AsmOp op;
    private Register rd;
    private String label;

    public LaAsm(AsmOp op, Register rd, String label) {
        this.op = op;
        this.rd = rd;
        this.label = label;
    }

    public void print() {
        System.out.println("    " + op + " " + rd + ", " + label);
    }
}
