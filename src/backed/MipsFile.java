package backed;

import java.util.ArrayList;

public class MipsFile {
    private static MipsFile INSTANCE = new MipsFile();
    private ArrayList<DataAssembly> dataAssemblies = new ArrayList<>();
    private ArrayList<TextAssembly> textAssemblies = new ArrayList<>();

    public static MipsFile getINSTANCE(){
        return INSTANCE;
    }

    public void addDataAssembly(DataAssembly dataAssembly){
        dataAssemblies.add(dataAssembly);
    }

    public void addTextAssembly(TextAssembly textAssembly){
        textAssemblies.add(textAssembly);
    }
}
