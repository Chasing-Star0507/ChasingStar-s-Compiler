package fronted;

import java.util.ArrayList;

public class EqExp extends Node {
    private ArrayList<RelExq> relExqs = new ArrayList<>();
    private ArrayList<Token> symbols;

    public EqExp(ArrayList<RelExq> relExqs, ArrayList<Token> symbols) {
        this.relExqs = relExqs;
        this.symbols = symbols;
    }

    public ArrayList<RelExq> getRelExqs() {
        return relExqs;
    }

    public ArrayList<Token> getSymbols() {
        return symbols;
    }

    public void print() {
        for (int i = 0; i < relExqs.size(); i++) {
            if (i > 0) {
                System.out.println("<EqExp>");
                System.out.println(symbols.get(i - 1).toString());
            }
            relExqs.get(i).print();
        }
        System.out.println("<EqExp>");
    }
}
