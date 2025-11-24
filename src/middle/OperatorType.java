package middle;

public enum OperatorType {
    ADD("add"),
    SUB("sub"),
    MUL("mul"),
    SDIV("sdiv"),
    SREM("srem"),
    ICMP_EQ("icmp eq"),
    ICMP_NE("icmp ne"),
    ICMP_SGT("icmp sgt"),
    ICMP_SGE("icmp sge"),
    ICMP_SLT("icmp slt"),
    ICMP_SLE("icmp sle"),
    ALLOC("alloc"),
    LOAD("load"),
    STORE("store"),
    GEP(""),
    RET("ret"),
    BR("br"),
    CALL("call"),
    ZEXT("zext");

    private String name;

    OperatorType(String name){
        this.name = name;
    }

    public static boolean isLogicCond(OperatorType opType){
        return opType == OperatorType.ICMP_EQ || opType == OperatorType.ICMP_NE ||
                opType == OperatorType.ICMP_SLE || opType == OperatorType.ICMP_SLT ||
                opType == OperatorType.ICMP_SGE || opType == OperatorType.ICMP_SGT;
    }

    public String toString(){
        return name;
    }
}
