package fronted;

import java.util.ArrayList;

public class InitVal extends Node {
    private int type;
    private ArrayList<Exp> exps = new ArrayList<>();

    public InitVal(int type, ArrayList<Exp> exps) {
        this.type = type;
        this.exps = exps;
    }

    public int getType() {
        return type;
    }

    public ArrayList<Exp> getExps() {
        return exps;
    }

    public void print() {
        if (type == 0) {
            exps.get(0).print();
        } else {
            System.out.println(TokenType.LBRACE.print());
            for (int i = 0; i < exps.size(); i++) {
                if (i > 0) {
                    System.out.println(TokenType.COMMA.print());
                }
                exps.get(i).print();
            }
            System.out.println(TokenType.RBRACE.print());
        }
        System.out.println("<InitVal>");
    }
}
