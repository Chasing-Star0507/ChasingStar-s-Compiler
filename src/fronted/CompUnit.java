package fronted;

import java.util.ArrayList;

public class CompUnit extends Node{
    private ArrayList<Decl> decls = new ArrayList<>();
    private ArrayList<FuncDef> funcDefs = new ArrayList<>();
    private MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls,ArrayList<FuncDef> funcDefs,MainFuncDef mainFuncDef){
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }


    public void print(){
        for(Decl decl : decls){
            decl.print();
        }
        for(FuncDef funcDef : funcDefs){
            funcDef.print();
        }
        mainFuncDef.print();
        System.out.println("<CompUnit>");
    }
}
