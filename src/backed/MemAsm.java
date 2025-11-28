package backed;

public class MemAsm extends TextAssembly{
    private AsmOp op;
    private Register rd;
    private Register rs;
    private int immediate;

    public MemAsm(AsmOp op,Register rd,Register rs,int immediate){
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.immediate = immediate;
    }

    public void print(){
        System.out.println("    " + op + " " + rd + ", " + immediate + "(" + rs + ")");
    }
}
