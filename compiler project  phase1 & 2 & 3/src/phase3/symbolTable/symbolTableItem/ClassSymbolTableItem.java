package phase3.symbolTable.symbolTableItem;

public class ClassSymbolTableItem extends ClassDeclaration {

    public static final String classModifier = "class_";
    public String parent = null;


    public ClassSymbolTableItem(String name) {
        this.name = name;

    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        String out;
        if(parent != null) {
            out = "Class: " + "(name: " + this.name + ") " + "(parent : " + parent;
        }else {
            return "Class: " + "(name: " + this.name + ") ";
        }

        return out +")";
    }


    @Override
    public String getKey() {
        return ClassSymbolTableItem.classModifier + name;
    }


}
