package phase3.symbolTable.symbolTableItem.varItems;

import phase3.symbolTable.symbolTableItem.SymbolTableItem;


public abstract class VarSymbolTableItem extends SymbolTableItem {
    public static String var_modifier = "var_";
    protected String type;

    public String getKey() {
        return VarSymbolTableItem.var_modifier + name;
    }

    public String getType() {
        return type;
    }
}
