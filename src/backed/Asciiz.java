package backed;

public class Asciiz extends DataAssembly{
    private String name;
    private String content;

    public Asciiz(String name,String content){
        this.name = name;
        this.content = content.replace("\\0A","\\n");
    }
}
