package backed;

public enum AsmOp {
    ADD("add"),
    ADDU("addu"),
    ADDI("addi"),
    ADDIU("addiu"),
    SUB("sub"),
    SUBU("subu"),
    SUBI("subi"),
    SUBIU("subiu"),
    MUL("mul"),
    DIV("div"),
    MFLO("mflo"),
    MFHI("mfhi"),
    SLL("sll"),
    SLT("slt"),
    SLE("sle"),
    SGT("sgt"),
    SGE("sge"),
    SEQ("seq"),
    SNE("sne"),
    BEQ("beq"),
    BNE("bne"),
    BGE("bge"),
    BLE("ble"),
    BGT("bgt"),
    BLT("blt"),
    J("j"),
    JR("jr"),
    JAL("jal"),
    LW("lw"),
    SW("sw"),
    LA("la"),
    LI("li"),
    MOVE("move"),
    SYSCALL("syscall");

    private String op;

    AsmOp(String op){
        this.op = op;
    }

    public String toString(){
        return op;
    }
}
