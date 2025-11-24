package backed;

import java.util.ArrayList;

public class Word extends DataAssembly{
    private String name;
    private ArrayList<Integer> integers;
    private boolean isArray;
    private int length;

    public Word(String name,int value,boolean isArray){
        this.name = name;
        integers.add(value);
        this.isArray = isArray;
        this.length = 0;
    }

    public Word(String name,ArrayList<Integer> integers,boolean isArray,int length){
        this.name = name;
        this.integers = integers;
        this.isArray = isArray;
        this.length = length;
    }
}
