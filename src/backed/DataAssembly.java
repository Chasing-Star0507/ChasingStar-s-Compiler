package backed;

public abstract class DataAssembly extends Assembly{
    public DataAssembly(){
        MipsFile.getINSTANCE().addDataAssembly(this);
    }

    public abstract void print();
}
