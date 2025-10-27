package fronted;

import java.util.ArrayList;

public class ConstDecl extends Decl {
    private Btype btype;
    private ArrayList<ConstDef> constDefs = new ArrayList<>();

    public ConstDecl(Btype btype, ArrayList<ConstDef> constDefs) {
        this.btype = btype;
        this.constDefs = constDefs;
    }

    public ArrayList<ConstDef> getConstDefs() {
        return constDefs;
    }

    public Btype getBtype() {
        return btype;
    }

    public void print() {
        System.out.println(TokenType.CONSTTK.print());
        btype.print();
        for (int i = 0; i < constDefs.size(); i++) {
            if (i > 0) {
                System.out.println(TokenType.COMMA.print());
            }
            constDefs.get(i).print();
        }
        System.out.println(TokenType.SEMICN.print());
        System.out.println("<ConstDecl>");
    }
}
