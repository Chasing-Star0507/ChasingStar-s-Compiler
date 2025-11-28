package middle;

import java.util.ArrayList;

public class BasicBlock extends User {
    private ArrayList<Instruction> instructions = new ArrayList<>();
    private Function function;

    public BasicBlock(String name) {
        super(name, new LabelType());
        this.function = IRData.getCurFunction();
        function.addBasicblock(this);
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public Function getFunction() {
        return function;
    }

    public void print() {
        System.out.println(getName() + ":");
        for (Instruction instruction : instructions) {
            instruction.print();
        }
    }
}
