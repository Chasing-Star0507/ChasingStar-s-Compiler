package backed;

import java.util.Objects;

public class BrAsm extends TextAssembly {
    private AsmOp op;
    private String label;
    private Register rs;
    private Register rt;
    private int number;

    public BrAsm(AsmOp op, String label, Register rs, Register rt) {
        this.op = op;
        this.label = label;
        this.rs = rs;
        this.rt = rt;
    }

    public BrAsm(AsmOp op, String label, Register rs, int number) {
        this.op = op;
        this.label = label;
        this.rs = rs;
        this.rt = null;
        this.number = number;
    }

    public void print() {
        System.out.println("    " + op + " " + rs + ", " + Objects.requireNonNullElse(rt, number) + ", " + label);
    }
}
