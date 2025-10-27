package middle;

public class ParamSymbol {
    private String name;
    private SymbolType type;
    private int dimension;

    public ParamSymbol(String name, SymbolType type, int dimension) {
        this.name = name;
        this.type = type;
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }

    public int getDimension() {
        return dimension;
    }
}
