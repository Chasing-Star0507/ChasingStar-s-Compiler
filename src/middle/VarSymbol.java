package middle;

public class VarSymbol extends Symbol {
    private boolean isConstant;
    private int dimension;

    public VarSymbol(String name, SymbolType type, boolean isConstant, int dimension) {
        super(name, type);
        this.isConstant = isConstant;
        this.dimension = dimension;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public int getDimension() {
        return dimension;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (dimension == 0) {
            if (isConstant) {
                sb.append(getName());
                sb.append(" ConstInt");
            } else {
                if (getType() == SymbolType.INT) {
                    sb.append(getName());
                    sb.append(" Int");
                } else if (getType() == SymbolType.STATIC) {
                    sb.append(getName());
                    sb.append(" StaticInt");
                }
            }
        } else {
            if (isConstant) {
                sb.append(getName());
                sb.append(" ConstIntArray");
            } else {
                if (getType() == SymbolType.INT) {
                    sb.append(getName());
                    sb.append(" IntArray");
                } else if (getType() == SymbolType.STATIC) {
                    sb.append(getName());
                    sb.append(" StaticIntArray");
                }
            }
        }
        return sb.toString();
    }
}
