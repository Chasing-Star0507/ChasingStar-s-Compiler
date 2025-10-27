package middle;

public class Symbol {
    private String name;
    private SymbolType type;

    public Symbol(String name, SymbolType symbolType) {
        this.name = name;
        this.type = symbolType;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }
}
