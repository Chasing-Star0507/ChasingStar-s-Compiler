package middle;

public class GlobalVar extends User {
    private InitialValue initialValue;
    private boolean isConst;

    public GlobalVar(String name, ValueType type, InitialValue initialValue, boolean isConst) {
        super(name, type);
        this.initialValue = initialValue;
        this.isConst = isConst;
        //ToDo 这里要加一个判断条件
        Module.getINSTANCE().addGlobalVar(this);
    }

    public InitialValue getInitialValue() {
        return initialValue;
    }

    public boolean isConst() {
        return isConst;
    }

    public void print() {
//        System.out.println(getName());
//        System.out.print(" = dso_local" + (isConst ? " constant " : " global "));
//        initialValue.print();
//        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(" = dso_local ").append(isConst ? "constant " : "global ");
        sb.append(initialValue.toString());
        System.out.println(sb.toString());
    }
}
