package middle;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SymbolTable {
    private LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();
    private SymbolTable parent;
    private ArrayList<SymbolTable> childrens = new ArrayList<>();
    private SymbolType type;
    private int counter;

    public SymbolTable(SymbolType type, SymbolTable parent, int counter) {
        this.parent = parent;
        this.type = type;
        this.counter = counter;
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return symbols;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public ArrayList<SymbolTable> getChildrens() {
        return childrens;
    }

    public SymbolType getType() {
        return type;
    }

    public int getCounter() {
        return counter;
    }

    public void addSymbol(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public boolean isContainSymbol(String name) {
//        if (name.equals("getint")) {
//            return true;
//        }
        return symbols.containsKey(name);
    }

    public Symbol getSymbol(String name) {
//        if (name.equals("getint")) {
//            return new FuncSymbol("getint", SymbolType.INT, new ArrayList<>());
//        } else {
//            return symbols.get(name);
//        }
        return symbols.get(name);
    }

    public void addChild(SymbolTable symbolTable) {
        childrens.add(symbolTable);
    }

    public boolean isFuncTable() {
        return type == SymbolType.INT;
    }

    public void print() {
        for (String name : symbols.keySet()) {
            Symbol symbol = symbols.get(name);
            if (symbol instanceof VarSymbol) {
                System.out.println(counter + " " + ((VarSymbol) symbol).toString());
            } else if (symbol instanceof FuncSymbol) {
                if (!symbol.getName().equals("main")) {
                    System.out.println(counter + " " + ((FuncSymbol) symbol).toString());
                }
            }
        }
        for (SymbolTable symbolTable : childrens) {
            symbolTable.print();
        }
    }
}
