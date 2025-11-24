package middle;

import java.util.ArrayList;

public class Function extends User{
    private ValueType funcRetType;
    private ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
    private ArrayList<FuncParam> funcParams = new ArrayList<>();
    private boolean isLibFunction = false;

    public Function(String name,ValueType valueType){
        super(name,new LabelType());
        this.funcRetType = valueType;
        Module.getINSTANCE().addFunction(this);
    }

    public Function(String name, ValueType returnType, boolean isBuiltIn) {
        super(name, new LabelType());
        this.funcRetType = returnType;
        this.isLibFunction = isBuiltIn;
    }

    public void addBasicblock(BasicBlock basicBlock){
        basicBlocks.add(basicBlock);
    }

    public void addFuncParam(FuncParam funcParam){
        funcParams.add(funcParam);
    }

    public ValueType getReturnType(){
        return funcRetType;
    }

    public ValueType getFuncRetType() {
        return funcRetType;
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public ArrayList<FuncParam> getFuncParams() {
        return funcParams;
    }

    public boolean isLibFunction() {
        return isLibFunction;
    }

    public void print(){
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        if(funcRetType.equals(IntegerType.i32)){
            sb.append("i32 ");
        }else if(funcRetType.equals(IntegerType.VOID)){
            sb.append("void ");
        }
        sb.append(getName());
        System.out.print(sb);
        System.out.print("(");
        for(int i = 0;i < funcParams.size();i++){
            if(i > 0){
                System.out.print(", ");
                funcParams.get(i).print();
            }else{
                funcParams.get(i).print();
            }
        }
        System.out.print(") ");
        System.out.println("{");
        for(BasicBlock basicBlock : basicBlocks){
            basicBlock.print();
        }
        System.out.println("}");
    }
}
