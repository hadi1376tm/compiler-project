package compiler.symbolTable;
import compiler.symbolTable.symbolTableItem.SymbolTableItem;


import java.util.*;

public class SymbolTable {
    private SymbolTable pre;
    private Map<String, SymbolTableItem> items;
    private static Map<String, SymbolTable> symbolTables = new HashMap<>();
    public String name;
    private static SymbolTable top;
    private static Stack<SymbolTable> stack = new Stack<>();
    private static Queue<SymbolTable> queue = new LinkedList<>();

    public SymbolTable( String name, SymbolTable pre) {

        this.name = name;
        this.pre = pre;
        this.items = new HashMap<>();
    }

    public static void push(SymbolTable symbolTable) {
        if (top != null)
            stack.push(top);
        top = symbolTable;
        queue.offer(symbolTable);
        symbolTables.put(symbolTable.name , symbolTable);
    }


    public void put(SymbolTableItem item) {
        items.put(item.getKey(), item);
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

}
