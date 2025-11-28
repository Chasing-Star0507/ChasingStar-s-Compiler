package fronted;

import middle.TableManager;
import middle.VarSymbol;

public class LVal extends Node implements Calculation {
    private Token ident;
    private Exp exp;

    public LVal(Token ident, Exp exp) {
        this.ident = ident;
        this.exp = exp;
    }

    public Token getIdent() {
        return ident;
    }

    public Exp getExp() {
        return exp;
    }

    public void print() {
        System.out.println(ident.toString());
        if (exp != null) {
            System.out.println(TokenType.LBRACK.print());
            exp.print();
            System.out.println(TokenType.RBRACK.print());
        }
        System.out.println("<LVal>");
    }

    public int calculate() {
        TableManager tableManager = TableManager.getINSTANCE2();
        VarSymbol varSymbol = (VarSymbol) tableManager.getSymbol(ident.getTokenContent());
        int length = 0;
        if (exp != null) {
            length = exp.calculate();
        }
        if (!varSymbol.isConstant()) {
            return 0;
        }
        if (varSymbol.getDimension() == 0) {
            return varSymbol.getConstValue(0);
        } else {
            if (varSymbol.getInitialValue().getElements().size() > length) {
                return varSymbol.getConstValue(length);
            } else {
                return 0;
            }
        }
    }
}
