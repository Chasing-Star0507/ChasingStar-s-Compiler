package middle;

public class ForLoop {
    private BasicBlock conditionBlock;
    private BasicBlock updateBlock;
    private BasicBlock loopBlock;
    private BasicBlock followBlock;

    public ForLoop(BasicBlock conditionBlock,BasicBlock loopBlock,BasicBlock updateBlock,BasicBlock followBlock) {
        this.conditionBlock = conditionBlock;
        this.loopBlock = loopBlock;
        this.updateBlock = updateBlock;
        this.followBlock = followBlock;
    }

    public BasicBlock getConditionBlock() {
        return conditionBlock;
    }

    public BasicBlock getUpdateBlock() {
        return updateBlock;
    }

    public BasicBlock getLoopBlock() {
        return loopBlock;
    }

    public BasicBlock getFollowBlock() {
        return followBlock;
    }
}
