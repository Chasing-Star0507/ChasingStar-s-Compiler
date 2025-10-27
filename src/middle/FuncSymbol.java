package middle;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private ArrayList<ParamSymbol> paramSymbols = new ArrayList<>();

    public FuncSymbol(String name, SymbolType type, ArrayList<ParamSymbol> paramSymbols) {
        super(name, type);
        this.paramSymbols = paramSymbols;
    }

    public ArrayList<ParamSymbol> getParamSymbols() {
        return paramSymbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getType() == SymbolType.INT) {
            sb.append(getName());
            sb.append(" IntFunc");
        } else if (getType() == SymbolType.VOID) {
            sb.append(getName());
            sb.append(" VoidFunc");
        }
        return sb.toString();
    }
}
