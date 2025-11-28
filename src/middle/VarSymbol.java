package middle;

public class VarSymbol extends Symbol {
    private boolean isConstant;
    private int dimension;
    private InitialValue initialValue = null;
    private Value llvmValue = null;

    public VarSymbol(String name, SymbolType type, boolean isConstant, int dimension) {
        super(name, type);
        this.isConstant = isConstant;
        this.dimension = dimension;
        this.initialValue = null;
    }

    public VarSymbol(String name, SymbolType type, boolean isConstant, int dimension, InitialValue initialValue) {
        super(name, type);
        this.isConstant = isConstant;
        this.dimension = dimension;
        this.initialValue = initialValue;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public int getDimension() {
        return dimension;
    }

    public void setLlvmValue(Value value) {
        this.llvmValue = value;
    }

    public void setInitialValue(InitialValue initialValue) {
        this.initialValue = initialValue;
    }

    public InitialValue getInitialValue() {
        return initialValue;
    }

    public Value getLlvmValue() {
        return llvmValue;
    }

    public int getConstValue(int index) {
        return initialValue.getElements().get(index);
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
