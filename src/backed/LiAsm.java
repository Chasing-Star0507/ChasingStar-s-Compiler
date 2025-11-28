package backed;

public class LiAsm extends TextAssembly{
    private AsmOp op;
    private Register rd;
    private int immediate;

    public LiAsm(AsmOp op,Register rd,int immediate){
        this.op = op;
        this.rd = rd;
        this.immediate = immediate;
    }

    public void print(){
        System.out.println("    " + op + " " + rd + ", " + immediate);
    }
}
