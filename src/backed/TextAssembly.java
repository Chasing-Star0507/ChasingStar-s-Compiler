package backed;

public abstract class TextAssembly extends Assembly{
    public TextAssembly(){
        MipsFile.getINSTANCE().addTextAssembly(this);
    }

    public abstract void print();
}
