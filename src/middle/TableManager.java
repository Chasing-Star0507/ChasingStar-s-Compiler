package middle;

public class TableManager {
    private static TableManager INSTANCE = new TableManager();
    private SymbolTable currentTable = new SymbolTable(null, null, 1);
    private int loopLevel = 0;
    private int cnt = 1;

    public static TableManager getINSTANCE() {
        return INSTANCE;
    }

    public SymbolTable getCurrentTable() {
        return currentTable;
    }

    public int getCnt() {
        return cnt;
    }

    public void addSymbol(Symbol symbol) {
        currentTable.addSymbol(symbol);
    }

    public boolean isContainSymbol(String name) {
        return currentTable.isContainSymbol(name);
    }

    public void createTable(SymbolType symbolType) {
        cnt++;
        SymbolTable symbolTable = new SymbolTable(symbolType, currentTable, cnt);
        currentTable.addChild(symbolTable);
        currentTable = symbolTable;
    }

    public void backTable() {
        currentTable = currentTable.getParent();
    }

    public boolean isFuncTable() {
        return currentTable.isFuncTable();
    }

    public Symbol getSymbol(String name) {
        SymbolTable table = currentTable;
        while (table != null) {
            if (table.isContainSymbol(name)) {
                return table.getSymbol(name);
            }
            table = table.getParent();
        }
        return null;
    }

    public boolean isConstant(String name) {
        Symbol symbol = getSymbol(name);
        if (symbol instanceof VarSymbol) {
            return ((VarSymbol) symbol).isConstant();
        } else {
            return false;
        }
    }

    public void enterLoop() {
        loopLevel++;
    }

    public void exitLoop() {
        loopLevel--;
    }

    public int getLoopLevel() {
        return loopLevel;
    }
}
