package fronted;

import java.util.ArrayList;

public class MulExp extends Node {
    private ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    private ArrayList<Token> symbols = new ArrayList<>();

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<Token> symbols) {
        this.unaryExps = unaryExps;
        this.symbols = symbols;
    }

    public ArrayList<UnaryExp> getUnaryExps() {
        return unaryExps;
    }

    public ArrayList<Token> getSymbols() {
        return symbols;
    }

    public void print() {
        for (int i = 0; i < unaryExps.size(); i++) {
            if (i > 0) {
                System.out.println("<MulExp>");
                System.out.println(symbols.get(i - 1).toString());
            }
            unaryExps.get(i).print();
        }
        System.out.println("<MulExp>");
    }
}
