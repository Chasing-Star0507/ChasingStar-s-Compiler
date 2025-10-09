package fronted;

import java.util.ArrayList;

public class VarDecl extends Decl{
    private int type;
    private Btype btype;
    private ArrayList<VarDef> varDefs = new ArrayList<>();

    public VarDecl(int type,Btype btype,ArrayList<VarDef> varDefs){
        this.type = type;
        this.btype = btype;
        this.varDefs = varDefs;
    }

    public void print(){
        if(type == 1){
            System.out.println(TokenType.STATICTK.print());
        }
        btype.print();
        for(int i = 0;i<varDefs.size();i++){
            if(i > 0){
                System.out.println(TokenType.COMMA.print());
            }
            varDefs.get(i).print();
        }
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<VarDecl>");
    }
}
