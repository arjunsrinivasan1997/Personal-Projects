package db;

import java.util.ArrayDeque;

/**
 * Created by arjunsrinivasan on 2/23/17.
 */
public class column<A> {
    ArrayDeque items;
    String name;
    String type;

    public column(String name, String type) {
        this.items = new ArrayDeque<A>();
        this.name = name;
        if (type != "int" && type != "string" && type != "float") {
            throw new IllegalArgumentException("Error: Type is not int, string or float");
        }
        this.type = type;

    }
    public String getType(){
        return type;
    }
    public ArrayDeque getItems(){
        return items;
    }

    public void addElement(Object item) {
        double d = 7.3/0.0;
        if (item.equals("NaN") || item.equals("NOVALUE")) {
            this.items.addLast(item);
        } else if (item.equals(d)){
            this.items.addLast((A) "NaN");
        } else {
            if (type == "int" && !(item instanceof Integer)) {
                throw new IllegalArgumentException("Error: Column type is int, but an int was not added to column");
            } else if (type == "string" && !(item instanceof String)) {
                throw new IllegalArgumentException("Error: Column type is string, but a string was not added to column");
            } else if (type == "float" && !(item instanceof Integer) && !(item instanceof Double) && !(item instanceof Float) && !(item.equals("BIG"))) {
                throw new IllegalArgumentException("Error: Column type is float, but a float was not added to column");
            } else {
                this.items.addLast(item);
            }
        }
    }
}
