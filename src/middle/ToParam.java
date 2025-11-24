package middle;

import fronted.*;

public class ToParam {
    public static ParamSymbol expToParam(Exp exp) {
        return addToParam(exp.getAddExp());
    }

    private static ParamSymbol addToParam(AddExp addExp) {
        return unaryToParam(addExp.getMulExps().get(0).getUnaryExps().get(0));
    }

    private static ParamSymbol unaryToParam(UnaryExp unaryExp) {
        if (unaryExp.getPrimaryExp() != null) {
            return primaryToParam(unaryExp.getPrimaryExp());
        } else if (unaryExp.getIdent() != null) {
            TableManager tableManager = TableManager.getINSTANCE1();
            Symbol symbol = tableManager.getSymbol(unaryExp.getIdent().getTokenContent());
            if (symbol instanceof FuncSymbol) {
                return new ParamSymbol(unaryExp.getIdent().getTokenContent(), ((FuncSymbol) symbol).getType(), 0);
            } else {
                return null;
            }
        } else {
            return unaryToParam(unaryExp.getUnaryExp());
        }
    }

    private static ParamSymbol primaryToParam(PrimaryExp primaryExp) {
        if (primaryExp.getExp() != null) {
            return expToParam(primaryExp.getExp());
        } else if (primaryExp.getlVal() != null) {
            return lValToParam(primaryExp.getlVal());
        } else {
            return new ParamSymbol(null, SymbolType.INT, 0);
        }
    }

    private static ParamSymbol lValToParam(LVal lVal) {
        //感觉这里倒是不需要再回到TableManager里面寻找了，只有int类型
        int dimension = 0;
        if (lVal.getExp() != null) {
            dimension = 1;
        }
        return new ParamSymbol(lVal.getIdent().getTokenContent(), SymbolType.INT, dimension);
    }

}
