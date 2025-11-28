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

    public void print(){
        System.out.println(".data:");
        for(DataAssembly dataAssembly : dataAssemblies){
            dataAssembly.print();
        }
        System.out.println();
        System.out.println(".text:");
        for(TextAssembly textAssembly : textAssemblies){
            textAssembly.print();
        }
    }
}
