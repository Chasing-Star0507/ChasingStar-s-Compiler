package middle;

import java.util.ArrayList;

public class Module extends Value {
    private static Module INSTANCE = new Module();
    private ArrayList<String> libraryFunctions = new ArrayList<>();
    private ArrayList<ConstString> constStrings = new ArrayList<>();
    private ArrayList<GlobalVar> globalVars = new ArrayList<>();
    private ArrayList<Function> functions = new ArrayList<>();

    public static Module getINSTANCE() {
        return INSTANCE;
    }

    public Module() {
        super("wxq", new LabelType());
        libraryFunctions.add("declare i32 @getint()");
        libraryFunctions.add("declare void @putint(i32)");
        libraryFunctions.add("declare void @putstr(i8*)");
    }

    public void addGlobalVar(GlobalVar globalVar) {
        globalVars.add(globalVar);
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public void addConstString(ConstString constString) {
        constStrings.add(constString);
    }

    public ArrayList<String> getLibraryFunctions() {
        return libraryFunctions;
    }

    public ArrayList<ConstString> getConstStrings() {
        return constStrings;
    }

    public ArrayList<GlobalVar> getGlobalVars() {
        return globalVars;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public void print() {
        for (String s : libraryFunctions) {
            System.out.println(s);
        }
        System.out.println();
        for (ConstString constString : constStrings) {
            constString.print();
        }
        System.out.println();
        for (GlobalVar globalVar : globalVars) {
            globalVar.print();
        }
        System.out.println();
        for (Function function : functions) {
            function.print();
            System.out.println();
        }
    }
}
