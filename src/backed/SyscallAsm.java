package backed;

public class SyscallAsm extends TextAssembly {
    private AsmOp op;

    public SyscallAsm(AsmOp op) {
        this.op = op;
    }

    public void print() {
        System.out.println("    " + "syscall");
    }
}
