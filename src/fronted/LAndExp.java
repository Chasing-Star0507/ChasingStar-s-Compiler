package fronted;

import java.util.ArrayList;

public class LAndExp extends Node {
    private ArrayList<EqExp> eqExps = new ArrayList<>();

    public LAndExp(ArrayList<EqExp> eqExps) {
        this.eqExps = eqExps;
    }

    public ArrayList<EqExp> getEqExps() {
        return eqExps;
    }

    public void print() {
        for (int i = 0; i < eqExps.size(); i++) {
            if (i > 0) {
                System.out.println("<LAndExp>");
                System.out.println(TokenType.AND.print());
            }
            eqExps.get(i).print();
        }
        System.out.println("<LAndExp>");
    }
}
