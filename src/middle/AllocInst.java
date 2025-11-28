package middle;

import java.util.ArrayList;

public class AllocInst extends Instruction {
    private ValueType targetType;
    private ArrayList<GepInst> gepInsts = new ArrayList<>();
    private ArrayList<StoreInst> storeInsts = new ArrayList<>();

    public AllocInst(ValueType valueType) {
        super(new PointType(valueType), OperatorType.ALLOC);
        this.targetType = valueType;
    }

    public void addGepInst(GepInst gepInst) {
        gepInsts.add(gepInst);
    }

    public void addStoreInst(StoreInst storeInst) {
        storeInsts.add(storeInst);
    }

    public void print() {
        System.out.println("\t" + getName() + " = alloca " + ((PointType) getValueType()).getTargetType().toString());
    }
}
