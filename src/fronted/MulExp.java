package fronted;

import java.util.ArrayList;

public class MulExp extends Node implements Calculation {
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

    public int calculate() {
        int res = 0;
        for (int i = 0; i < unaryExps.size(); i++) {
            if (i > 0) {
                if (symbols.get(i - 1).getTokenType() == TokenType.MULT) {
                    res *= unaryExps.get(i).calculate();
                } else if (symbols.get(i - 1).getTokenType() == TokenType.DIV) {
                    res /= unaryExps.get(i).calculate();
                } else if (symbols.get(i - 1).getTokenType() == TokenType.MOD) {
                    res %= unaryExps.get(i).calculate();
                }
            } else {
                res += unaryExps.get(i).calculate();
            }
        }
        return res;
    }
}
