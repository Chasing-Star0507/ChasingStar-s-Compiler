package middle;

import java.util.HashMap;
import java.util.Stack;

public class IRData {
    private static Stack<ForLoop> forLoops = new Stack<>();
    private static HashMap<String, ConstString> constStringHashMap = new HashMap<>();
    private static Function curFunction;
    private static BasicBlock curBasicBlock;
    private static int cnt = 0;
    private static int staticCnt = 0;
    private static int constStringCnt = 0;

    public static void reset() {
        cnt = 0;
        staticCnt = 0;
    }

    public static int getStaticCnt() {
        return staticCnt++;
    }

    public static String getBasicBlockName() {
        return "b" + cnt++;
    }

    public static String getVarName() {
        return "%v" + cnt++;
    }

    public static String getStringName() {
        return "@.s." + constStringCnt++;
    }

    public static Function getCurFunction() {
        return curFunction;
    }

    public static BasicBlock getCurBasicBlock() {
        return curBasicBlock;
    }

    public static void setCurBasicBlock(BasicBlock basicBlock) {
        curBasicBlock = basicBlock;
    }

    public static void setCurFunction(Function function) {
        curFunction = function;
    }

    public static void push(ForLoop forLoop) {
        forLoops.push(forLoop);
    }

    public static void pop() {
        forLoops.pop();
    }

    public static ForLoop peek() {
        return forLoops.peek();
    }

    public static boolean isContainString(String s) {
        return constStringHashMap.containsKey(s);
    }

    public static ConstString getConstString(String s) {
        return constStringHashMap.get(s);
    }

    public static void putConstString(String s, ConstString constString) {
        constStringHashMap.put(s, constString);
    }
}
