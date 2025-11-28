package backed;

public class CalcAsm extends TextAssembly{
    private AsmOp op;
    private Register rd;
    private Register rt;
    private Register rs;
    private int immediate = 0;

    public CalcAsm(AsmOp op,Register rd,Register rs,Register rt){
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public CalcAsm(AsmOp op,Register rd,Register rs,int immediate){
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = null;
        this.immediate = immediate;
    }

    public void print(){
        if(rt != null){
            System.out.println("    " + op + " " + rd + ", " + rs + ", " + rt);
        }else{
            System.out.println("    " + op + " " + rd + ", " + rs + ", " + immediate);
        }
    }
}
