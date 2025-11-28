package backed;

public class JumpAsm extends TextAssembly{
    private AsmOp op;
    private String label;
    private Register rd;

    public JumpAsm(AsmOp op,String label){
        this.op = op;
        this.label = label;
        this.rd = null;
    }

    public JumpAsm(AsmOp op,Register rd){
        this.op = op;
        this.rd = rd;
    }

    public void print(){
        if(rd != null){
            System.out.println("    " + op + " " + rd);
        }else{
            System.out.println("    " + op + " " + label);
        }
    }
}
