package backed;

import java.util.ArrayList;

public class Word extends DataAssembly {
    private String name;
    private ArrayList<Integer> integers = new ArrayList<>();
    private boolean isArray;
    private int length;

    public Word(String name, int value, boolean isArray) {
        this.name = name;
        integers.add(value);
        this.isArray = isArray;
        this.length = 0;
    }

    public Word(String name, ArrayList<Integer> integers, boolean isArray, int length) {
        this.name = name;
        this.integers = integers;
        this.isArray = isArray;
        this.length = length;
    }

    public void print() {
        if (!isArray) {
            System.out.println(name + ": .word " + integers.get(0));
        } else {
            if (integers.isEmpty()) {
                System.out.println(name + ": .word 0:" + length);
            } else {
                System.out.print(name + ": .word ");
                for (int i = 0; i < length; i++) {
                    if (i == 0) {
                        System.out.print(integers.get(0));
                    } else {
                        if (i < integers.size()) {
                            System.out.print(", " + integers.get(i));
                        } else {
                            System.out.print(", 0");
                        }
                    }
                }
                System.out.println();
            }
        }
    }
}
