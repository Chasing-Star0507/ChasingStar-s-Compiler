package backed;

import middle.*;
import middle.Module;

public class MipsBuilder {
    private Module module;

    public MipsBuilder(Module module){
        this.module = module;
    }

    public void build(){
        for(ConstString constString : module.getConstStrings()){
            buildConstString(constString);
        }
        for(GlobalVar globalVar : module.getGlobalVars()){
            buildGlobalVar(globalVar);
        }
        for(Function function : module.getFunctions()){
            if(function.getName().equals("@main")){
                buildFunction(function);
            }
        }
        for(Function function : module.getFunctions()){
            if(!function.getName().equals("@main")){
                buildFunction(function);
            }
        }
    }

    private void buildConstString(ConstString constString){
        new Asciiz(constString.getName().substring(2),constString.getStringValue());
    }

    private void buildGlobalVar(GlobalVar globalVar){
        ValueType valueType = ((PointType)globalVar.getValueType()).getTargetType();
        if(valueType instanceof IntegerType){
            if(globalVar.getInitialValue().getElements().isEmpty()){
                new Word(globalVar.getName().substring(1),0,false);
            }else{
                new Word(globalVar.getName().substring(1),globalVar.getInitialValue().getElements().get(0),false);
            }
        }else if(valueType instanceof ArrayType){
            new Word(globalVar.getName().substring(1),globalVar.getInitialValue().getElements(),true,globalVar.getInitialValue().getLength());
        }
    }

    private void buildFunction(Function function){

    }

    private void buildBasicBlock(BasicBlock basicBlock){

    }

    private void buildInstruction(Instruction instruction){

    }

    private void buildAllocInst(AllocInst allocInst){

    }

    private void buildBinaryInst(BinaryInst binaryInst){

    }

    private void buildBrInst(BrInst brInst){

    }

    private void buildCallInst(CallInst callInst){

    }

    private void buildGepInst(GepInst gepInst){

    }

    private void buildLoadInst(LoadInst loadInst){

    }

    private void buildRetInst(RetInst retInst){

    }

    private void buildStoreInst(StoreInst storeInst){

    }

    private void buildZextInst(ZextInst zextInst){

    }
}
