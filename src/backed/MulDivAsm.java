package backed;

public class MulDivAsm extends TextAssembly{
    private AsmOp op;
    private Register rd;
    private Register rs;

    public MulDivAsm(AsmOp op,Register rd,Register rs){
        this.op = op;
        this.rd = rd;
        this.rs = rs;
    }

    public void print(){
        System.out.println("    " + op + " " + rd + ", " + rs);
    }
}
