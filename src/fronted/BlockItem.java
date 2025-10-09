package fronted;

public class BlockItem extends Node{
    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl){
        this.decl = decl;
        this.stmt = null;
    }

    public BlockItem(Stmt stmt){
        this.decl = null;
        this.stmt = stmt;
    }

    public void print(){
        if(decl == null){
            stmt.print();
        }else{
            decl.print();
        }
        //System.out.println("<BlockItem>");
    }
}
