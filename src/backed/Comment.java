package backed;

import middle.Instruction;

public class Comment extends TextAssembly {
    private Instruction instruction;

    public Comment(Instruction instruction) {
        this.instruction = instruction;
    }

    public void print() {
        System.out.print("#");
        instruction.print();
    }
}
