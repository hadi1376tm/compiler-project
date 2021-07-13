package phase3.symbolTable;


import phase3.symbolTable.symbolTableItem.varItems.FieldSymbolTableItem;
import phase3.symbolTable.symbolTableItem.varItems.LocalVariableSymbolTableItem;
import phase3.symbolTable.symbolTableItem.SymbolTableItem;

import java.util.*;

public class SymbolTable {
    private SymbolTable pre;
    private Map<String, SymbolTableItem> items;
    private static Map<String, SymbolTable> symbolTables = new HashMap<>();
    public String name;
    private static SymbolTable top;
    private static Stack<SymbolTable> stack = new Stack<>();
    private static Queue<SymbolTable> queue = new LinkedList<>();
    public int line;
    public int column;

    public SymbolTable(int line, int column, String name, SymbolTable pre) {

        this.name = name;
        this.pre = pre;
        this.items = new HashMap<>();
        this.column = column;
        this.line = line;
    }

    public static void push(SymbolTable symbolTable) {
        if (top != null)
            stack.push(top);
        top = symbolTable;
        queue.offer(symbolTable);
        symbolTables.put(symbolTable.name , symbolTable);
    }


    public boolean put(SymbolTableItem item) {

            if (isAlreadyExists(item)) {
                return true;
            }
            items.put(item.getKey(), item);
            return false;

    }

    public boolean isAlreadyExists(SymbolTableItem item) {
        SymbolTable pre = this;
        while (pre != null) {
            if (pre.items.containsKey(item.getKey())) {
                if (item instanceof FieldSymbolTableItem) {
                    if (!(pre.items.get(item.getKey()) instanceof FieldSymbolTableItem)) {
                        return false;
                    }
                }
                if(item instanceof LocalVariableSymbolTableItem) {
                    if (!(pre.items.get(item.getKey()) instanceof LocalVariableSymbolTableItem)) {
                        return false;
                    }
                }
                return true;
            }
            pre = pre.pre;
        }
        return false;
    }

    public SymbolTableItem get(String key){
        return items.get(key);
    }


    public SymbolTable getPreSymbolTable() {
        return pre;
    }

    @Override
    public String toString() {

            if (pre != null) {
                return "parent scope : " + pre.name + "  \n" +
                        "------------  " + name + "  ------------\n" +
                        printItems() +
                        "---------------------------------------\n\n";
            } else {
                return "\n------------  " + name + "  ------------\n" +
                        printItems() +
                        "---------------------------------------\n\n";
            }
        }



    public String printItems() {
        String itemsStr = "";
        for (Map.Entry<String, SymbolTableItem> entry : items.entrySet()) {
            SymbolTableItem symbolTableItem = entry.getValue();
            itemsStr += "Key = " + entry.getKey() + "  | Value = " + symbolTableItem + "\n";
        }
        return itemsStr;
    }

    public static void printAll() {
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }
    public static SymbolTable getSymbolTableByKey(String key) {
        return symbolTables.get(key);
    }
}
