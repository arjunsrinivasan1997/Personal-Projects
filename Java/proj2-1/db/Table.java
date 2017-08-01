package db;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that represents a table. Every Table is built using a LinkedHasMap, with column names as keys and ArrayDeques as values
 * Created by arjunsrinivasan on 2/21/17.
 */
public class Table implements Cloneable{
    public LinkedHashMap<String, column> values = new LinkedHashMap<>();
    public String name;
    private int columnSize = 0;
    public int rowSize = 0;

    public Table(String name, LinkedHashMap<String,String> columnNames) {
        this.name = name;
        for (String s : columnNames.keySet()) {
            column c = new column<>(s,columnNames.get(s));
            values.put(s, c);
            columnSize++;
        }
    }
    public Table(String name){
        this.name = name;
    }
    public Table(String name, column[] columns){
        for (column c: columns){
            values.put(c.name,c);
            columnSize++;
        }
        rowSize = values.get(columns[0].name).getItems().size();
    }
    public void addColumn(column c) {
        if (c.getItems().size() != rowSize() && rowSize != 0){
            throw new IllegalArgumentException("ERROR: Column being added has " + Integer.toString(c.getItems().size()) + " rows but table has " + Integer.toString(rowSize) + " rows");
        }
        values.put(c.name, c);
        columnSize++;
        rowSize = c.getItems().size();
    }
    public boolean contains(String columnName) {
        return values.keySet().contains(columnName);
    }

    public class row {
        private Object[] values;
        private int position;

        public row(Object[] v, int p) {
            values = v;
            position = p;
        }
        public Object[] values(){
            return values;
        }
        public int position(){
            return position;
        }
    }

    /**
     * Removes the first column from a table
     * @return instance of the column class that was removed from the table
     */
    private column removeFirst(){
        return values.remove(values.keySet().toArray()[0]);
    }
    /**
     * Sorts the columns of a table according to an order. Nondestructive.
     * @param order
     * @return Table instance that is sorted
     */
    public Table sort(String[] order) throws IllegalArgumentException{
        for (String s: order){
            if (!(values.keySet().contains(s))){
                throw new IllegalArgumentException("ERROR: column "+s+" is not in table");
            }
        }
        Table t = clone();
        LinkedHashMap<String,column> unsortedColumns = new LinkedHashMap<>();
        for (String s: t.values.keySet()){
            unsortedColumns.put(s,values.get(s));
        }
        column[] sortedColumns = new column[t.columnSize()];
        int i = 0;
        for (String s: order){
            sortedColumns[i] = unsortedColumns.get(s);
            i++;
        }
        Table sortedTable = new Table(name+ " ordered", sortedColumns);
        return sortedTable;
    }
    @Override
    protected  Table clone(){
        column[] columns = new column[columnSize];
        int i = 0;
        for (String s: values.keySet()){
            columns[i] = values.get(s);
            i++;
        }
        Table t = new Table(name+" copy", columns);
        return t;
    }

    /**
     * Only for testing purposes so that new tables do not have to be created to test columnsInOrder
     */
    public static String[] columnsInOrder(String[] t1Keys, String[] t2Keys) {
        ArrayList<String> orderedKeys = new ArrayList<>();
        for (String s : t1Keys) {
            if (Arrays.asList(t2Keys).contains(s)) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("CC");
        for (String s : t1Keys) {
            if (!(orderedKeys.contains(s))) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("T1");
        for (String s : t2Keys) {
            if (!(orderedKeys.contains(s))) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("T2");
        String[] a = Arrays.copyOf(orderedKeys.toArray(), orderedKeys.toArray().length, String[].class);
        return a;
    }

    public int columnSize() {
        return columnSize;
    }
    public int rowSize(){
        return rowSize;
    }

    /**
     * Adds a row to the bottom of the table
     */
    public void addRow(Object[] items) {
        if (items.length < columnSize) {
            throw new IllegalArgumentException("Row being added does not have enough items");
        } else if (items.length > columnSize) {
            throw new IllegalArgumentException("Row being added has too many items");
        }
        int i = 0;
        for (String s : values.keySet()) {
            values.get(s).addElement(items[i]);
            i++;
        }
        rowSize ++;
    }

    public void addRows(Object[][] items) {
        if (items[0].length < columnSize) {
            throw new IllegalArgumentException("Row being added does not have enough items");
        } else if (items[0].length > columnSize) {
            throw new IllegalArgumentException("Row being added has too many items");
        }
        for (Object[] o : items) {
            addRow(o);
        }
    }

    /**
     * Gets a row from index i from the table. If index is -1, returns a String[] of the column names. Returns an Object or String array.
     */
    public Object[] getRow(int index) {
        if (index == -1){
            return Arrays.copyOf(values.keySet().toArray(), values.keySet().toArray().length, String[].class);
        } else {
            Object[] a = new Object[columnSize];
            int i = 0;
            for (String s : values.keySet()) {
                a[i] = values.get(s).getItems().toArray()[index];
                i++;
            }
            return a;
        }
    }

    /**
     * Does the same thing as getRow(index), but converts all items in row to a string and returns that
     * @param index
     * @return String[] with all elements in the row being converted to strings
     */
    public String[] getRowString(int index) {
        String[] row;
        if (index == -1) {
            return Arrays.copyOf(values.keySet().toArray(), values.keySet().toArray().length, String[].class);
        } else {
            row = new String[columnSize];
            int position = 0;
            for (String s : values.keySet()) {
                if (values.get(s).getType() == "string") {
                    row[position] = (String) values.get(s).items.toArray()[index];
                } else if (values.get(s).getType() == "int") {
                    row[position] = Integer.toString((Integer) values.get(s).items.toArray()[index]);
                } else{
                    row[position] = Double.toString((Double) values.get(s).items.toArray()[index]);
                }
                position ++;
            }
            return row;
        }
    }



    /**
     * Gets row containing element x from column c ignoring rows in items ArrayList ordered by String[] order Returns an instance of the row class
     */
    public row getRow(Object element, String c, ArrayList<Integer> ignore,String[] order) {
        int rowposition = -1;
        for (int i = 0; i < values.get(c).getItems().size(); i++) {
            if (values.get(c).getItems().toArray()[i].equals(element) && !(ignore.contains(i))) {
                rowposition = i;
                break;
            }
        }
        if (rowposition == -1) {
            for (int i = 0; i < values.get(c).getItems().size(); i++) {
                if (values.get(c).getItems().toArray()[i].equals(element)) {
                    rowposition = i;
                    break;
                }
            }
        }
        if (rowposition == -1){
            throw new IllegalArgumentException("Element does not exist in column");
        }
        Object[] a = new Object[order.length];
        int x = 0;
        for (String s: order){
            a[x] = values.get(s).getItems().toArray()[rowposition];
            x++;
        }
        row r = new row(a, rowposition);
        return r;
    }

    public Table join(Table t) {
        String[] newColumns = columnsInOrder(t);
        int commonColumns = 0;
        int x = 0;
        for (String s:newColumns){
            if (s == "CC"){
                commonColumns = x;
            } else{
                x++;
            }
        }
        LinkedHashMap<String, String> newColumns2 = new LinkedHashMap<>();
        String[] a = t1Columns(newColumns);
        String[] b = t2columns(newColumns);
        for (String s: a){
            newColumns2.put(s,values.get(s).getType());
        }
        for (String s: b){
            newColumns2.put(s,t.values.get(s).getType());
        }
        Table joinedTable = new Table(name+ "+"+t.name, newColumns2);



        // Trigger Cartesian joinedTable //
        if (commonColumns == 0) {
            for (int i = 0; i < rowSize(); i++) {
                Object[] t1row = new Object[rowSize()];
                t1row = getRow(i);
                for (int j = 0; j < t.rowSize(); j++) {
                    Object[] t2row = new Object[t.rowSize()];
                    t2row = t.getRow(j);
                    Object[] newRow = new Object[t1row.length+t2row.length];
                    System.arraycopy(t1row,0,newRow,0,t1row.length);
                    System.arraycopy(t2row,0,newRow,t1row.length,t2row.length);
                    joinedTable.addRow(newRow);
                }
            }
            return joinedTable;
        } else {
            String column1 = newColumns[0];
            ArrayList<Object> commonElements = new ArrayList<>();
            Object[] columnElements = values.get(column1).getItems().toArray();
            for (Object o : columnElements) {
                for (int i = 0; i < t.values.get(column1).getItems().size(); i++) {
                    if (t.values.get(column1).getItems().toArray()[i].equals(o)) {
                        commonElements.add(o);
                    }
                }
            }
            //System.out.println(commonElements);
            if (commonElements.size() == 0){
                return joinedTable;
            }
            ArrayList<Integer> c = new ArrayList<>();
            ArrayList<Integer> d = new ArrayList<>();
            String[] t1Columns = t1Columns(newColumns);
            String[] t2Columns = t2columns(newColumns);
            for (Object o: commonElements){
                row r1 = getRow(o,column1,c,t1Columns);
                c.add(r1.position());
                row r2 = t.getRow(o,column1,d,t2Columns);
                d.add(r2.position());
                Object[] newRow = new Object[r1.values().length+r2.values().length-commonColumns];
                System.arraycopy(r1.values(),0,newRow,0,r1.values().length);
                System.arraycopy(r2.values(),  commonColumns,newRow,r1.values().length,r2.values().length-commonColumns);
                joinedTable.addRow(newRow);
            }
            return joinedTable;
        }
    }

    public static String[] t1Columns(String[] items){
        ArrayList<String> a = new ArrayList<>();
        int i = 0;
        for(String s: items) {
            if (s == "CC") {
                break;
            }
            a.add(s);
            i++;
        }
        i ++;
        for (int j = i; j < items.length; j++) {
            if (items[j] == "T1"){
                break;
            }
            a.add(items[j]);
        }
        String[] t1Columns = Arrays.copyOf(a.toArray(), a.toArray().length, String[].class);
        return t1Columns;
    }
    public static String[] t2columns(String[] items){
        ArrayList<String> a = new ArrayList<>();
        int i = 0;
        for(String s: items) {
            if (s == "CC") {
                break;
            }
            a.add(s);
            i++;
        }
        i ++;
        boolean c = false;
        while (!c) {
            if (items[i] == "T1") {
                c = true;
            } else{
                i++;
            }
        }
        i++;
        for (int j = i; j <items.length ; j++) {
            if (items[j] == "T2") {
                break;
            }
            a.add(items[j]);
        }

        String[] t2Columns = Arrays.copyOf(a.toArray(), a.toArray().length, String[].class);
        return t2Columns;
    }

    public String[] columnsInOrder(Table t) {
        // Received help from http://stackoverflow.com/questions/1018750/how-to-convert-object-array-to-string-array-in-java to convert an object array to a string array/
        String[] t1Keys = Arrays.copyOf(values.keySet().toArray(), values.keySet().toArray().length, String[].class);
        String[] t2Keys = Arrays.copyOf(t.values.keySet().toArray(), t.values.keySet().toArray().length, String[].class);
        ArrayList<String> orderedKeys = new ArrayList<>();
        for (String s : t1Keys) {
            if (Arrays.asList(t2Keys).contains(s)) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("CC");
        for (String s : t1Keys) {
            if (!(orderedKeys.contains(s))) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("T1");
        for (String s : t2Keys) {
            if (!(orderedKeys.contains(s))) {
                orderedKeys.add(s);
            }
        }
        orderedKeys.add("T2");
        String[] a = Arrays.copyOf(orderedKeys.toArray(), orderedKeys.toArray().length, String[].class);
        return a;
    }

    /**
     * Prints the table
     */
    public String printColumns(){
        StringBuilder result =  new StringBuilder("");
        int counter = 0;
        for (String s : values.keySet()) {
            if (counter == 0){
                result.append(s);
            }
            else{
                result.append(s);
            }
            if (values.get(s).getType() == "string") {
                result.append(" string");
            } else if (values.get(s).getType() == "int") {
                result.append(" int");
            } else {
                result.append(" float");
            }
            counter++;
            if (!(counter == columnSize)) {
                result.append(",");
            }
        }
        result.append("\n");
        return result.toString();
    }
    public String printTable() {
        StringBuilder result;
        if (values.get(values.keySet().toArray()[0]).getItems().size() == 0){
            return printColumns();
        }
        int a = values.get(values.keySet().toArray()[0]).getItems().size();
        int counter = 1;
        result = new StringBuilder(printColumns());
        for (int i = 0; i < rowSize() ; i++) {
            int counter2 = 1;
            for (String s : values.keySet()) {
                Object o = values.get(s).getItems().toArray()[i];
                if (values.get(s).getType().equals("string")) {
                    result.append(values.get(s).getItems().toArray()[i]);
                } else if (values.get(s).getType().equals("float")) {
                    if (values.get(s).getItems().toArray()[i].equals("NOVALUE")){
                        result.append("NOVALUE");
                    } else if (String.valueOf(values.get(s).getItems().toArray()[i]).equals("NaN")){
                        result.append("NaN");
                    }
                    else {
                        double d = Math.round((Double)values.get(s).getItems().toArray()[i] * 1000.0) / 1000.0;
                        String ss = String.format("%.03f", d);
                        result.append(ss);
                    }
                } else {
                    result.append(values.get(s).getItems().toArray()[i]);
                }
                if (!(counter2 == columnSize)) {
                    result.append(",");
                }
                counter2++;
            } if (!(counter == rowSize)) {
                result.append("\n");
            }
            counter++;
        }
        return result.toString();
    }
    @Override
    public String toString(){
        return printTable();
    }
}
