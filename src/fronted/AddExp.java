package fronted;

import java.util.ArrayList;

public class AddExp extends Node {
    private ArrayList<MulExp> mulExps;
    private ArrayList<Token> symbols;

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Token> symbols) {
        this.mulExps = mulExps;
        this.symbols = symbols;
    }

    public ArrayList<MulExp> getMulExps() {
        return mulExps;
    }

    public ArrayList<Token> getSymbols() {
        return symbols;
    }

    public void print() {
        for (int i = 0; i < mulExps.size(); i++) {
            if (i > 0) {
                System.out.println("<AddExp>");
                System.out.println(symbols.get(i - 1).toString());
            }
            mulExps.get(i).print();
        }
        System.out.println("<AddExp>");
    }
}
