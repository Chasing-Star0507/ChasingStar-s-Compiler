package fronted;

public class BlockStmt extends Stmt{
    private Block block;

    public BlockStmt(Block block){
        this.block = block;
    }

    public void print(){
        block.print();
        System.out.println("<Stmt>");
    }
}
