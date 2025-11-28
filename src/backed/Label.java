package backed;

public class Label extends TextAssembly {
    private String name;

    public Label(String name){
        this.name = name;
    }

    public void print(){
        System.out.println(name + ":");
    }
}
