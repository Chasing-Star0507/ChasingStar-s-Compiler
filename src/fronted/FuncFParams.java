package fronted;

import java.util.ArrayList;

public class FuncFParams extends Node {
    private ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public FuncFParams(ArrayList<FuncFParam> funcFParams) {
        this.funcFParams = funcFParams;
    }

    public ArrayList<FuncFParam> getFuncFParams() {
        return funcFParams;
    }

    public void print() {
        for (int i = 0; i < funcFParams.size(); i++) {
            if (i > 0) {
                System.out.println(TokenType.COMMA.print());
            }
            funcFParams.get(i).print();
        }
        System.out.println("<FuncFParams>");
    }
}
