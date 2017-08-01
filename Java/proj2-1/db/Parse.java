package db;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parse {
    // Various common constructs, simplifies parsing.
    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";
    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);
    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");
    private static int tableCount = 0;
    public Database parserDatabase;

    private static boolean switchFunctionString(String x, String operation, String y) {
        char[] xArray = x.toCharArray();
        char[] yArray = y.toCharArray();
        StringBuilder xString = new StringBuilder();
        StringBuilder yString = new StringBuilder();
        String xx = x;
        String yy = y;
        char c = '\'';
        if (xArray[0] == c) {
            for (int i = 1; i < x.length() - 1; i++) {
                xString.append(xArray[i]);
            }
        }
        if (yArray[0] == c) {
            for (int i = 1; i < y.length() - 1; i++) {
                yString.append(yArray[i]);
            }
        }
        if (yString.length() != 0) {
            yy = yString.toString();
        }
        if (xString.length() != 0) {
            xx = xString.toString();
        }
        if (yy.equals("NOVALUE") || xx.equals("NOVALUE")) {
            return false;
        }
        switch (operation) {
            case ("=="):
                return xx.equals(yy);
            case ("!="):
                return (!xx.equals(yy));
            case (">"):
                int a = xx.compareTo(yy);
                return a > 0;
            case ("<"):
                int b = xx.compareTo(yy);
                return b < 0;
            case ("<="):
                return xx.compareTo(yy) <= 0;
            case (">="):
                return xx.compareTo(yy) >= 0;
        }
        return false;
    }

    private static boolean switchFunctionInt(Integer x, String operation, Integer y) {
        switch (operation) {
            case ("=="):
                return x.equals(y);
            case ("!="):
                return (!x.equals(y));
            case (">"):
                int a = x.compareTo(y);
                return a > 0;
            case ("<"):
                int b = x.compareTo(y);
                return b < 0;
            case ("<="):
                return x.compareTo(y) <= 0;
            case (">="):
                return x.compareTo(y) >= 0;
        }
        return false;
    }

    private static boolean switchFunctionDouble(Double x, String operation, Double y) {
        switch (operation) {
            case ("=="):
                return x.equals(y);
            case ("!="):
                return (!x.equals(y));
            case (">"):
                int a = x.compareTo(y);
                boolean aa = a > 0;
                return a > 0;
            case ("<"):
                int b = x.compareTo(y);
                return b < 0;
            case ("<="):
                return x.compareTo(y) <= 0;
            case (">="):
                return x.compareTo(y) >= 0;
        }
        return false;
    }

    private static boolean switchFunctionNaN(Object x, String operation, Object y) throws IOException{
        if (x.equals("NaN")){
            switch (operation) {
                case ("=="):
                    return x.equals(y);
                case ("!="):
                    return (!x.equals(y));
                case (">"):
                    if (x.equals(y)) {
                        return false;
                    }
                    return true;
                case ("<"):
                    return false;
                case ("<="):
                    if (x.equals(y)) {
                        return true;
                    }
                    return false;
                case (">="):
                    return true;
            }
            return false;
        } else{
            switch (operation) {
                case ("=="):
                    return y.equals(x);
                case ("!="):
                    return (!y.equals(x));
                case (">"):
                    return false;
                case ("<"):
                    if (y.equals(x)){
                        return false;
                    } return true;
                case ("<="):
                    return true;
                case (">="):
                    if (x.equals(y)) {
                        return true;
                    }
                    return false;
            }
        }
        throw new IOException("ERROR: No switch condition was triggered meaning NaN was not passed into the functio");
    }

    private static Integer binaryintOperator(int start, int next, String operand) throws ArithmeticException {
        switch (operand) {
            case ("\\+"):
                return start + next;
            case ("\\*"):
                return start * next;
            case ("\\-"):
                return start - next;
            case ("\\/"):
                return start / next;
        }
        return 0;
    }

    private static String binarystringOperator(String first, String second, String operand) {
        switch (operand) {
            case ("\\+"):
                first = first.replaceAll("'", "");
                second = second.replaceAll("'", "");
                return "'" + first + second + "'";
        }
        return null;
    }

    private static double binaryfloatOperator(Double start, Double next, String operand) throws ArithmeticException {
        switch (operand) {
            case ("\\+"):
                return start + next;
            case ("\\*"):
                return start * next;
            case ("\\-"):
                return start - next;
            case ("\\/"):
                return start / next;
        }
        return 0.0;
    }
    private static double binaryfloatOperator(Double start, int next, String operand) throws ArithmeticException {
        switch (operand) {
            case ("\\+"):
                return start + next;
            case ("\\*"):
                return start * next;
            case ("\\-"):
                return start - next;
            case ("\\/"):
                return start / next;
        }
        return 0.0;
    }

    private String createSelectedTable(String name, String exprs, String tables, String conds) throws IOException {
        //System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
        //     " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        Table t = selectTable(exprs, tables, conds);
        t.name = name;
        parserDatabase.getTableArray().add(t);
        return "";
    }

    public String main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return "ERROR:";
        }
        return eval(args[0]);
    }

    private String eval(String query) throws IOException {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return selectchecker(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
            return "ERROR: Eval error";
        }
    }

    private String createTable(String expr) {
        Matcher m;
        try {
            if ((m = CREATE_NEW.matcher(expr)).matches()) {
                return createNewTable(m.group(1), m.group(2).split(COMMA));
            } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
                return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
            } else {
                System.err.printf("Malformed create: %s\n", expr);
                return "ERROR: Create Table Error";
            }
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String createNewTable(String name, String[] cols) throws IOException {
        //System.out.println("You are trying to create a new table " + name +" with columns "+ cols);
        int i = 0;
        for (String s : cols) {
            cols[i] = s.trim().replaceAll(" +", " ");
            i++;
        }
        for (Table x : parserDatabase.getTableArray()) {
            if (x.name.equals(name)) {
                return ("Already a table with that name");
            }
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (int ii = 0; i < cols.length - 1; i++) {
            joiner.add(cols[ii]);
        }
        String colSentence = joiner.toString() + " and " + cols[cols.length - 1];
        LinkedHashMap colums = new LinkedHashMap();
        for (String x : cols) {
            if (!x.contains("int") && !x.contains("string") && !x.contains("float")) {
                throw new IOException("ERROR: " + x + "is not a valid type");
            }
            if (x.contains("int") && !x.contains("integer")) {
                x = x.replaceAll(" int$", "");
                colums.put(x, "int");
            } else if (x.contains("string")) {
                x = x.replaceAll(" string$", "");
                colums.put(x, "string");
            } else if (x.contains("float")) {
                x = x.replaceAll(" float", "");
                colums.put(x, "float");
            }
        }
        Table newTable = new Table(name, colums);
        parserDatabase.getTableArray().add(newTable);
        return ("");
    }

    private String loadTable(String name) throws IOException {

        int listLength = parserDatabase.getTableArray().toArray().length;
        for (int i = 0; i < listLength; i++) {
            if (parserDatabase.contains(name)) {
                parserDatabase.getTableArray().remove(i);
            }
        }
        File fileReader;
        Scanner sc;
        fileReader = new File(name + ".tbl");
        try {
            sc = new Scanner(fileReader);
        } catch (IOException e) {
            return "ERROR: File does not exist in this directory";
        }
        String[] colNames = sc.nextLine().split(",");
        for (String s : colNames) {
            if (s.contains("int") && s.split("int").length != 1) {
                throw new IOException("ERROR: Incorrect column name");
            } else if (s.contains("float") && s.split("float").length != 1) {
                throw new IOException("ERROR: Incorrect column name");
            } else if (s.contains("string") && s.split("string").length != 1) {
                String[] ssssss = s.split("string");
                throw new IOException("ERROR Incorrect column name");
            }
        }

        String[] pathName = name.split("/");
        String tableName = pathName[pathName.length - 1];
        createNewTable(tableName, colNames);
        for (Table x : parserDatabase.getTableArray()) {
            if (x.name.equals(tableName)) {
                while (sc.hasNext()) {
                    String[] row = sc.nextLine().split(",");
                    /*
                    for (int i = 0; i < row.length; i++) {
                        row[i] = row[i].replaceAll("\\s+", "");
                    }
                    */
                    ArrayList rowHelper = new ArrayList();
                    rowHelper.addAll(Arrays.asList(row));
                    for (int i = 0; i < row.length; i++) {
                        try {
                            int u = Integer.parseInt(row[i]);
                            rowHelper.set(i, u);
                        } catch (NumberFormatException e) {
                            try {
                                double u = Double.parseDouble(row[i]);
                                rowHelper.set(i, u);
                            } catch (NumberFormatException e1) {

                            }
                        }
                    }
                    try {
                        /*System.out.println(Arrays.toString(rowHelper.toArray()));
                        System.out.println(x.printTable());*/
                        x.addRow(rowHelper.toArray());

                    } catch (IllegalArgumentException e) {
                        throw new IOException("ERROR: Malformed table");
                    }
                }
                System.out.println(x);
            }
        }
       return ("");
    }

    private String storeTable(String name) throws IOException {
        File file = new File(name + ".tbl");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        String[] holder;
        for (Table x : parserDatabase.getTableArray()) {
            if (x.name.equals(name)) {
                writer.write(x.printTable());
                /**for (int i = -1; i < x.rowSize(); i++) {
                 holder = x.getRowString(i);
                 List holderPlus = Arrays.asList(holder);
                 String row = String.join(",", holderPlus);
                 if (i == -1) {
                 writer.write(row);
                 } else {
                 writer.write("\n" + row);
                 }
                 }*/
                writer.close();
                return ("");
            }
        }
        throw new IOException("ERROR: Table " + name + " not found");
    }

    private String dropTable(String name) throws IOException {
        if (!parserDatabase.contains(name)) {
            throw new IOException("ERROR: Table " + name + "does not exist in the database");
        }
        for (Table x : parserDatabase.getTableArray()) {
            if (x.name.equals(name)) {
                parserDatabase.getTableArray().remove(x);
                return ("");
            }
        }
        return ("ERROR: Table " + name + " not found");
    }

    private String insertRow(String expr) throws IOException {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new IOException("ERROR: Malformed insert");
        }
        Object[] helper = m.group(2).split(",");
        Object[] blankList = new Object[helper.length];
        for (int i = 0; i < helper.length; i++) {
            String x = (String) helper[i];
            try {
                int y = Integer.parseInt(x);
                blankList[i] = y;
            } catch (NumberFormatException e) {
                try {
                    double u = Double.parseDouble(x);
                    blankList[i] = u;
                } catch (NumberFormatException e1) {
                    blankList[i] = helper[i];
                }

            }
        }
        for (Table xx : parserDatabase.getTableArray()) {
            if (xx.name.equals(m.group(1))) {
                try {
                    xx.addRow(blankList);
                    return ("");
                } catch (IllegalArgumentException e) {
                    throw new IOException("ERROR: Wrong type inserted into column");
                }
            }
        }
        return ("ERROR: Table " + m.group(1) + " not found");

    }

    private String printTable(String name) throws IOException {
        if (!parserDatabase.contains(name)) {
            throw new IOException("ERROR: Table " + name + "does not exist in the database");
        }
        for (Table x : parserDatabase.getTableArray()) {
            if (x.name.equals(name)) {
                return x.printTable();
            }
        }
        return "";
    }

    private String selectchecker(String expr) throws IOException {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new IOException("ERROR: Malformed Select");
        }
        return select(m.group(1), m.group(2), m.group(3));
    }

    private String select(String exprs, String tables, String conds) throws IOException {
        String[] tablesArray = tables.split(",");
        for (String s : tablesArray) {
            if (!parserDatabase.contains(s)) {
                throw new IOException("ERROR: Table " + s + "does not exist in the database");
            }
        }
        exprs = exprs.replaceAll(" as ", "~");
        exprs = exprs.replaceAll("\\s", "");
        tables = tables.replaceAll("\\s", "");
        try {
            conds = conds.replaceAll("\\s", "");
            conds = conds.replaceAll("and", ",");
        } catch (Exception e) {
        }
        String[] listOperators = new String[]{"==", ">=", "<=", "<", ">", "!="};
        ArrayList<String> listOperatorsArrayList = new ArrayList<>();
        for (String s : listOperators) {
            listOperatorsArrayList.add(s);
        }
        LinkedHashMap<String, Table> tablesHashMap = new LinkedHashMap<>();
        for (Table t : parserDatabase.getTableArray()) {
            tablesHashMap.put(t.name, t);
        }
        String[] binaryOperators = new String[]{"+", "-", "*", "~", "/"};
        String[] listofTableNamesStrList = tables.split(",");
        ArrayList<Table> listofTables = new ArrayList<>();
        String[] listofColums = exprs.split(",");
        ArrayList<String> listofColumnsNew = new ArrayList();
        ArrayList<Integer> columIndexs = new ArrayList<>();
        int t1 = -1;
        int t2 = -1;
        int columIndex = -1;
        Table newHolder = null;
        if (listofTableNamesStrList.length == 2) {
            newHolder = tablesHashMap.get(listofTableNamesStrList[0]).join(tablesHashMap.get(listofTableNamesStrList[1]));
        } else if (listofTableNamesStrList.length == 1) {
            newHolder = tablesHashMap.get(listofTableNamesStrList[0]);
        } else {
            int a = listofTableNamesStrList.length;
            boolean bool = true;
            int i = 0;
            while (bool) {
                if (i == 0) {
                    newHolder = tablesHashMap.get(listofTableNamesStrList[0]).join(tablesHashMap.get(listofTableNamesStrList[1]));
                    i = 2;
                    if (i == a) {
                        bool = false;
                    }
                } else {
                    newHolder = newHolder.join(tablesHashMap.get(listofTableNamesStrList[i]));
                    i++;
                    if (i == a) {
                        bool = false;
                    }
                }
            }
        }
        if (!exprs.equals("*")) {
            for (String col : listofColums) {
                ArrayList<String> binaryHelper = new ArrayList();
                if (col.contains("~")) {
                    columIndex++;
                    columIndexs.add(columIndex);
                    ArrayList<String> newlistofcolumns = new ArrayList<>();
                    for (String x : binaryOperators) {
                        if (col.contains(x)) {
                            if (!x.equals("~")) {
                                binaryHelper.add(x);
                            }
                            col = col.replaceAll("\\" + x, " ");
                        }
                    }
                    if (binaryHelper.isEmpty()) {
                        throw new IOException("Please enter a valid operator");
                    }
                    String[] colNamesStrList = col.split(" ");
                    List<String> removeColumnName = Arrays.asList(colNamesStrList);
                    //String newColumnName = removeColumnName.get(removeColumnName.size() - 1);
                    for (int i = 0; i < removeColumnName.size() - 1; i++) {
                        newlistofcolumns.add(removeColumnName.get(i).toString());

                    }
                    for (String x : newlistofcolumns) {
                        listofColumnsNew.add(x);
                    }
                    newlistofcolumns.clear();
                } else {
                    listofColumnsNew.add(col);
                    columIndex++;
                }
            }
            String[] finalColumList = new String[listofColumnsNew.size()];
            for (int i = 0; i < listofColumnsNew.size(); i++) {
                finalColumList[i] = listofColumnsNew.get(i);
            }
            listofTables = filterFunction(listofTableNamesStrList, finalColumList, exprs);
        } else {
            listofTables = filterFunction(listofTableNamesStrList, listofColums, exprs);
        }
        ArrayList<String> filteredColumns = new ArrayList<>();
        for (Table t : listofTables) {
            for (String s : t.values.keySet()) {
                filteredColumns.add(s);
            }
        }
        Table filteredNewHolder = new Table("filteredcolumns");
        for (String s : newHolder.values.keySet()) {
            if (filteredColumns.contains(s)) {
                filteredNewHolder.addColumn(newHolder.values.get(s));
            }
        }
        if (!exprs.equals("*")) {
            Set<String> removeDuplicates = new LinkedHashSet<>(listofColumnsNew);
            List<String> columList = new ArrayList(removeDuplicates);
            String[] finalColumList = new String[columList.size()];
            for (int i = 0; i < columList.size(); i++) {
                finalColumList[i] = columList.get(i);
            }
            filteredNewHolder = filteredNewHolder.sort(finalColumList);
        }
        Table binary;
        if (columIndex != -1) {
            ArrayList<column> summedColumns = new ArrayList<>();
            for (int x = 0; x < listofColums.length; x++) {
                if (columIndexs.contains(x)) {
                    ArrayList binaryHelper = new ArrayList();
                    String toParse = listofColums[x];
                    for (String operator : binaryOperators) {
                        if (toParse.contains(operator)) {
                            if (!operator.equals("~")) {
                                binaryHelper.add("\\" + operator);
                            }
                            toParse = toParse.replaceAll("\\" + operator, " ");
                        }
                    }
                    String[] columNames = toParse.split(" ");
                    String newColName = columNames[columNames.length - 1];
                    ArrayList<String> coltoOperate = new ArrayList<>();
                    for (int i = 0; i < columNames.length - 1; i++) {
                        coltoOperate.add(columNames[i]);
                    }
                    column summed = new column(newColName, "int");

                    for (int i = 0; i < filteredNewHolder.rowSize; i++) {
                        ArrayList toStore = new ArrayList();
                        ArrayList<String> type = new ArrayList();
                        for (int j = 0; j < coltoOperate.size(); j++) {
                            for (column a : filteredNewHolder.values.values()) {
                                if (coltoOperate.get(j).equals(a.name)) {
                                    toStore.add(a.getItems().toArray()[i]);
                                    type.add(a.type);
                                }
                            }
                        }
                        if ((type.get(0).equals("string") && type.get(1).equals("int")) || (type.get(0).equals("string") && type.get(1).equals("float")) || (type.get(0).equals("float") && type.get(1).equals("string")) || (type.get(0).equals("int") && type.get(1).equals("string"))) {
                            throw new IOException("ERROR: Cannot operate on columns of different types");
                        }
                        summed.type = type.get(0);
                        if (summed.type.equals("int")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");
                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    if (type.get(1).equals("int")) {
                                        toStore.remove(0);
                                        toStore.add(0, 0);
                                    } else if (type.get(1).equals("float")) {
                                        toStore.remove(1);
                                        toStore.add(1, 0.0);
                                    }
                                }

                                else if (toStore.get(1).equals("NOVALUE")) {
                                    toStore.remove(1);
                                    toStore.add(1, 0);
                                }
                                if (type.get(1).equals("int")) {
                                    try {
                                        summed.addElement(binaryintOperator((Integer) toStore.get(0), (Integer) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException e) {
                                        summed.addElement("NaN");
                                    }
                                } else if (type.get(1).equals("float")) {
                                    summed.type = "float";
                                    try {
                                        summed.addElement(binaryfloatOperator((Integer) toStore.get(0) * 1.0, (Double) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException e) {
                                        summed.addElement("NaN");
                                    }
                                }
                            }
                        } else if (summed.type.equals("float")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");

                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    if (type.get(1).equals("float")){
                                        toStore.remove(0);
                                        toStore.add(0, 0.0);
                                    } else {
                                        toStore.remove(0);
                                        toStore.add(0,0);
                                    }

                                } else if (toStore.get(1).equals("NOVALUE")) {
                                    if (type.get(1).equals("float")){
                                        toStore.remove(1);
                                        toStore.add(0, 0.0);
                                    } else {
                                        toStore.remove(1);
                                        toStore.add(1,0);
                                    }
                                }
                                if (type.get(1).equals("float")) {
                                    try {
                                        summed.addElement(binaryfloatOperator((Double) toStore.get(0), (Double) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException ee) {
                                        summed.addElement("NaN");
                                    }
                                } else if (type.get(1).equals("int")) {
                                    summed.type = "float";
                                    try {
                                        summed.addElement(binaryfloatOperator((Double) toStore.get(0), (Integer) toStore.get(1) * 1.0, (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException ee) {
                                        summed.addElement("NaN");
                                    }
                                }
                            }
                        } else if (summed.type.equals("string")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");
                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    toStore.add(0, "");
                                } else if (toStore.get(1).equals("NOVALUE")) {
                                    toStore.add(1, "");
                                }
                                if (!binaryHelper.get(0).equals("\\+")) {
                                    throw new IOException("ERROR: Strings can only be added together");
                                }
                                summed.addElement(binarystringOperator((String) toStore.get(0), (String) toStore.get(1), (String) binaryHelper.get(0)));
                            }
                        }

                        summedColumns.add(summed);
                    }
                }else {
                    summedColumns.add(filteredNewHolder.values.get(listofColums[x]));
                }
            }
            LinkedHashMap<String, String> binaryCreator = new LinkedHashMap();
            for (column a : summedColumns) {
                binaryCreator.put(a.name, a.type);
            }
            binary = new Table("binaryTable", binaryCreator);
            for (column a : summedColumns) {
                binary.values.put(a.name, a);
                binary.rowSize = a.items.toArray().length;
            }
            filteredNewHolder = binary;
        }
        //System.out.println(filteredNewHolder.printTable());


        if (conds != null) {
            ArrayList<String> condParse = new ArrayList<>();
            String[] amountIteration = conds.split(",");
            Table condHolder;
            for (int i = 0; i < amountIteration.length; i++) {
                conds = amountIteration[i];
                for (String s : listOperators) {
                    if (conds.split(s).length != 1) {
                        String[] conditionArray = conds.split(s);
                        condParse.add(conditionArray[0]);
                        condParse.add(s);
                        condParse.add(conditionArray[1]);
                        break;
                    }
                }
                /**Parse conditional statement
                 for (String columnName : newHolder.getRowString(-1)) {
                 if (conds.contains(columnName)) {
                 condParse.add(columnName);
                 conds = conds.replace(columnName, "");
                 }
                 }
                 for (String opName : listOperators) {
                 if (conds.contains(">=")) {
                 condParse.add(">=");
                 conds = conds.replace(">=", "");
                 } else if (conds.contains("<=")) {
                 condParse.add("<=");
                 conds = conds.replace("<=", "");
                 } else if (conds.contains(opName)) {
                 condParse.add(opName);
                 conds = conds.replace(opName, "");
                 }
                 }
                 condParse.add(conds);*/

                String col1 = "";
                String col2 = "";
                for (Table t : parserDatabase.getTableArray()) {
                    if (t.contains(condParse.get(0))) {
                        col1 = condParse.get(0);
                    }
                    if (t.contains(condParse.get(2))) {
                        col2 = condParse.get(2);
                    }
                }
                if (col1.equals("") && col2.equals("")) {
                    if (filteredNewHolder.contains(condParse.get(0))) {
                        col1 = condParse.get(0);
                    }
                    if (filteredNewHolder.contains(condParse.get(2))) {
                        col2 = condParse.get(2);
                    }
                }
                //Creates new Table with Rows filtered
                LinkedHashMap condHelper = new LinkedHashMap();
                ArrayList<String> condobjectHelper = new ArrayList();
                //Initializes Columns
                for (column a : filteredNewHolder.values.values()) {
                    condHelper.put(a.name, a.type);
                }
                condHolder = new Table("Conditional", condHelper);
                //Check which column is concerned
                for (column a : filteredNewHolder.values.values()) {
                    int operatorIndex = -1;
                    int iiiii = 0;
                    for (String s : condParse) {
                        if (listOperatorsArrayList.contains(s)) {
                            operatorIndex = iiiii;
                            break;
                        }
                        iiiii++;
                    }
                    //Look for a name match
                    if (a.name.equals(condParse.get(0))) {
                        //Check all members of columns A to see that they pass condition
                        int counter = 0;
                        for (int iii = 0; iii < a.items.size(); iii++) {
                            if (a.getType() == "string") {
                                if (!col1.equals("") && String.valueOf(filteredNewHolder.values.get(col1).items.toArray()[iii]).equals("NaN")){
                                    String o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if (!col2.equals("") && String.valueOf(filteredNewHolder.values.get(col2).items.toArray()[iii]).equals("NaN")) {
                                    String o1 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o1)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        String o1 = (String) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        String o2;
                                        try {
                                            o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");

                                        }
                                        if (switchFunctionString(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        String o1 = (String) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionString(o1, condParse.get(operatorIndex), condParse.get(2))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        String o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionString(condParse.get(0), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionString(condParse.get(0), condParse.get(operatorIndex), condParse.get(2))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }
                                }
                            } else if (a.getType() == "int") {
                                if (!col1.equals("") && String.valueOf(filteredNewHolder.values.get(col1).items.toArray()[iii]).equals("NaN")) {

                                    Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if ( !col2.equals("") && String.valueOf(filteredNewHolder.values.get(col2).items.toArray()[iii]).equals("NaN")) {
                                    Integer o1 = (Integer) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                    if (switchFunctionNaN(o1, condParse.get(operatorIndex), "NaN")) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        Integer o1 = (Integer) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        Integer o2 = null;
                                        try {
                                            o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");

                                        }
                                        if (switchFunctionInt(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        Integer o1 = (Integer) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionInt(o1, condParse.get(operatorIndex), Integer.parseInt(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionInt(Integer.parseInt(condParse.get(0)), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionInt(Integer.parseInt(condParse.get(0)), condParse.get(operatorIndex), Integer.parseInt(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }

                                }
                            } else if (a.getType() == "float") {
                                if (!col1.equals("") && String.valueOf(filteredNewHolder.values.get(col1).items.toArray()[iii]).equals("NaN")) {
                                    Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if (!col2.equals("") && String.valueOf(filteredNewHolder.values.get(col2).items.toArray()[iii]).equals("NaN")) {
                                    Double o1 = (Double) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                    if (switchFunctionNaN(o1, condParse.get(operatorIndex),"NaN" )) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        Double o1 = (Double) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        Double o2 = null;
                                        try {
                                            o2 = (Double) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            try {
                                                o2 = 1.0 * (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                            } catch (Exception e1) {
                                                throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");
                                            }
                                        }
                                        if (switchFunctionDouble(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        Double o1 = (Double) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionDouble(o1, condParse.get(operatorIndex), Double.parseDouble(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        Double o2 = (Double) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionDouble(Double.parseDouble(condParse.get(0)), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionDouble(Double.parseDouble(condParse.get(0)), condParse.get(operatorIndex), Double.parseDouble(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }
                                }
                            }
                            counter++;
                        }
                    }
                }
                condParse.clear();
                filteredNewHolder = condHolder;
            }
        }
        return filteredNewHolder.printTable();
    }

    private Table selectTable(String exprs, String tables, String conds) throws IOException {
        String[] tablesArray = tables.split(",");
        for (String s : tablesArray) {
            if (!parserDatabase.contains(s)) {
                throw new IOException("ERROR: Table " + s + "does not exist in the database");
            }
        }
        exprs = exprs.replaceAll(" as ", "~");
        exprs = exprs.replaceAll("\\s", "");
        tables = tables.replaceAll("\\s", "");
        try {
            conds = conds.replaceAll("\\s", "");
            conds = conds.replaceAll("and", ",");
        } catch (Exception e) {
        }
        String[] listOperators = new String[]{"==", ">=", "<=", "<", ">", "!="};
        ArrayList<String> listOperatorsArrayList = new ArrayList<>();
        for (String s : listOperators) {
            listOperatorsArrayList.add(s);
        }
        LinkedHashMap<String, Table> tablesHashMap = new LinkedHashMap<>();
        for (Table t : parserDatabase.getTableArray()) {
            tablesHashMap.put(t.name, t);
        }
        String[] binaryOperators = new String[]{"+", "-", "*", "~", "/"};
        String[] listofTableNamesStrList = tables.split(",");
        ArrayList<Table> listofTables = new ArrayList<>();
        String[] listofColums = exprs.split(",");
        ArrayList<String> listofColumnsNew = new ArrayList();
        ArrayList<Integer> columIndexs = new ArrayList<>();
        int t1 = -1;
        int t2 = -1;
        int columIndex = -1;
        Table newHolder = null;
        if (listofTableNamesStrList.length == 2) {
            newHolder = tablesHashMap.get(listofTableNamesStrList[0]).join(tablesHashMap.get(listofTableNamesStrList[1]));
        } else if (listofTableNamesStrList.length == 1) {
            newHolder = tablesHashMap.get(listofTableNamesStrList[0]);
        } else {
            int a = listofTableNamesStrList.length;
            boolean bool = true;
            int i = 0;
            while (bool) {
                if (i == 0) {
                    newHolder = tablesHashMap.get(listofTableNamesStrList[0]).join(tablesHashMap.get(listofTableNamesStrList[1]));
                    i = 2;
                    if (i == a) {
                        bool = false;
                    }
                } else {
                    newHolder = newHolder.join(tablesHashMap.get(listofTableNamesStrList[i]));
                    i++;
                    if (i == a) {
                        bool = false;
                    }
                }
            }
        }
        if (!exprs.equals("*")) {
            for (String col : listofColums) {
                ArrayList<String> binaryHelper = new ArrayList();
                if (col.contains("~")) {
                    columIndex++;
                    columIndexs.add(columIndex);
                    ArrayList<String> newlistofcolumns = new ArrayList<>();
                    for (String x : binaryOperators) {
                        if (col.contains(x)) {
                            if (!x.equals("~")) {
                                binaryHelper.add(x);
                            }
                            col = col.replaceAll("\\" + x, " ");
                        }
                    }
                    if (binaryHelper.isEmpty()) {
                        throw new IOException("Please enter a valid operator");
                    }
                    String[] colNamesStrList = col.split(" ");
                    List<String> removeColumnName = Arrays.asList(colNamesStrList);
                    //String newColumnName = removeColumnName.get(removeColumnName.size() - 1);
                    for (int i = 0; i < removeColumnName.size() - 1; i++) {
                        newlistofcolumns.add(removeColumnName.get(i).toString());

                    }
                    for (String x : newlistofcolumns) {
                        listofColumnsNew.add(x);
                    }
                    newlistofcolumns.clear();
                } else {
                    listofColumnsNew.add(col);
                    columIndex++;
                }
            }
            String[] finalColumList = new String[listofColumnsNew.size()];
            for (int i = 0; i < listofColumnsNew.size(); i++) {
                finalColumList[i] = listofColumnsNew.get(i);
            }
            listofTables = filterFunction(listofTableNamesStrList, finalColumList, exprs);
        } else {
            listofTables = filterFunction(listofTableNamesStrList, listofColums, exprs);
        }
        ArrayList<String> filteredColumns = new ArrayList<>();
        for (Table t : listofTables) {
            for (String s : t.values.keySet()) {
                filteredColumns.add(s);
            }
        }
        Table filteredNewHolder = new Table("filteredcolumns");
        for (String s : newHolder.values.keySet()) {
            if (filteredColumns.contains(s)) {
                filteredNewHolder.addColumn(newHolder.values.get(s));
            }
        }
        if (!exprs.equals("*")) {
            Set<String> removeDuplicates = new LinkedHashSet<>(listofColumnsNew);
            List<String> columList = new ArrayList(removeDuplicates);
            String[] finalColumList = new String[columList.size()];
            for (int i = 0; i < columList.size(); i++) {
                finalColumList[i] = columList.get(i);
            }
            filteredNewHolder = filteredNewHolder.sort(finalColumList);
        }
        Table binary;
        if (columIndex != -1) {
            ArrayList<column> summedColumns = new ArrayList<>();
            for (int x = 0; x < listofColums.length; x++) {
                if (columIndexs.contains(x)) {
                    ArrayList binaryHelper = new ArrayList();
                    String toParse = listofColums[x];
                    for (String operator : binaryOperators) {
                        if (toParse.contains(operator)) {
                            if (!operator.equals("~")) {
                                binaryHelper.add("\\" + operator);
                            }
                            toParse = toParse.replaceAll("\\" + operator, " ");
                        }
                    }
                    String[] columNames = toParse.split(" ");
                    String newColName = columNames[columNames.length - 1];
                    ArrayList<String> coltoOperate = new ArrayList<>();
                    for (int i = 0; i < columNames.length - 1; i++) {
                        coltoOperate.add(columNames[i]);
                    }
                    column summed = new column(newColName, "int");

                    for (int i = 0; i < filteredNewHolder.rowSize; i++) {
                        ArrayList toStore = new ArrayList();
                        ArrayList<String> type = new ArrayList();
                        for (int j = 0; j < coltoOperate.size(); j++) {
                            for (column a : filteredNewHolder.values.values()) {
                                if (coltoOperate.get(j).equals(a.name)) {
                                    toStore.add(a.getItems().toArray()[i]);
                                    type.add(a.type);
                                }
                            }
                        }
                        if ((type.get(0).equals("string") && type.get(1).equals("int")) || (type.get(0).equals("string") && type.get(1).equals("float")) || (type.get(0).equals("float") && type.get(1).equals("string")) || (type.get(0).equals("int") && type.get(1).equals("string"))) {
                            throw new IOException("ERROR: Cannot operate on columns of different types");
                        }
                        summed.type = type.get(0);
                        if (summed.type.equals("int")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");
                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    if (type.get(1).equals("int")) {
                                        toStore.remove(0);
                                        toStore.add(0, 0);
                                    } else if (type.get(1).equals("float")) {
                                        toStore.remove(1);
                                        toStore.add(1, 0.0);
                                    }
                                }

                                else if (toStore.get(1).equals("NOVALUE")) {
                                    toStore.remove(1);
                                    toStore.add(1, 0);
                                }
                                if (type.get(1).equals("int")) {
                                    try {
                                        summed.addElement(binaryintOperator((Integer) toStore.get(0), (Integer) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException e) {
                                        summed.addElement("NaN");
                                    }
                                } else if (type.get(1).equals("float")) {
                                    summed.type = "float";
                                    try {
                                        summed.addElement(binaryfloatOperator((Integer) toStore.get(0) * 1.0, (Double) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException e) {
                                        summed.addElement("NaN");
                                    }
                                }
                            }
                        } else if (summed.type.equals("float")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");

                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    if (type.get(1).equals("float")){
                                        toStore.remove(0);
                                        toStore.add(0, 0.0);
                                    } else {
                                        toStore.remove(0);
                                        toStore.add(0,0);
                                    }

                                } else if (toStore.get(1).equals("NOVALUE")) {
                                    if (type.get(1).equals("float")){
                                        toStore.remove(1);
                                        toStore.add(0, 0.0);
                                    } else {
                                        toStore.remove(1);
                                        toStore.add(1,0);
                                    }
                                }
                                if (type.get(1).equals("float")) {
                                    try {
                                        summed.addElement(binaryfloatOperator((Double) toStore.get(0), (Double) toStore.get(1), (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException ee) {
                                        summed.addElement("NaN");
                                    }
                                } else if (type.get(1).equals("int")) {
                                    summed.type = "float";
                                    try {
                                        summed.addElement(binaryfloatOperator((Double) toStore.get(0), (Integer) toStore.get(1) * 1.0, (String) binaryHelper.get(0)));
                                    } catch (ArithmeticException ee) {
                                        summed.addElement("NaN");
                                    }
                                }
                            }
                        } else if (summed.type.equals("string")) {
                            if (toStore.get(0).equals("NaN") || toStore.get(1).equals("NaN")) {
                                summed.addElement("NaN");
                            } else {
                                if (toStore.get(0).equals("NOVALUE")) {
                                    toStore.add(0, "");
                                } else if (toStore.get(1).equals("NOVALUE")) {
                                    toStore.add(1, "");
                                }
                                if (!binaryHelper.get(0).equals("\\+")) {
                                    throw new IOException("ERROR: Strings can only be added together");
                                }
                                summed.addElement(binarystringOperator((String) toStore.get(0), (String) toStore.get(1), (String) binaryHelper.get(0)));
                            }
                        }

                        summedColumns.add(summed);
                    }
                }else {
                    summedColumns.add(filteredNewHolder.values.get(listofColums[x]));
                }
            }
            LinkedHashMap<String, String> binaryCreator = new LinkedHashMap();
            for (column a : summedColumns) {
                binaryCreator.put(a.name, a.type);
            }
            binary = new Table("binaryTable", binaryCreator);
            for (column a : summedColumns) {
                binary.values.put(a.name, a);
                binary.rowSize = a.items.toArray().length;
            }
            filteredNewHolder = binary;
        }
        //System.out.println(filteredNewHolder.printTable());


        if (conds != null) {
            ArrayList<String> condParse = new ArrayList<>();
            String[] amountIteration = conds.split(",");
            Table condHolder;
            for (int i = 0; i < amountIteration.length; i++) {
                conds = amountIteration[i];
                for (String s : listOperators) {
                    if (conds.split(s).length != 1) {
                        String[] conditionArray = conds.split(s);
                        condParse.add(conditionArray[0]);
                        condParse.add(s);
                        condParse.add(conditionArray[1]);
                        break;
                    }
                }
                /**Parse conditional statement
                 for (String columnName : newHolder.getRowString(-1)) {
                 if (conds.contains(columnName)) {
                 condParse.add(columnName);
                 conds = conds.replace(columnName, "");
                 }
                 }
                 for (String opName : listOperators) {
                 if (conds.contains(">=")) {
                 condParse.add(">=");
                 conds = conds.replace(">=", "");
                 } else if (conds.contains("<=")) {
                 condParse.add("<=");
                 conds = conds.replace("<=", "");
                 } else if (conds.contains(opName)) {
                 condParse.add(opName);
                 conds = conds.replace(opName, "");
                 }
                 }
                 condParse.add(conds);*/

                String col1 = "";
                String col2 = "";
                for (Table t : parserDatabase.getTableArray()) {
                    if (t.contains(condParse.get(0))) {
                        col1 = condParse.get(0);
                    }
                    if (t.contains(condParse.get(2))) {
                        col2 = condParse.get(2);
                    }
                }
                if (col1.equals("") && col2.equals("")) {
                    if (filteredNewHolder.contains(condParse.get(0))) {
                        col1 = condParse.get(0);
                    }
                    if (filteredNewHolder.contains(condParse.get(2))) {
                        col2 = condParse.get(2);
                    }
                }
                //Creates new Table with Rows filtered
                LinkedHashMap condHelper = new LinkedHashMap();
                ArrayList<String> condobjectHelper = new ArrayList();
                //Initializes Columns
                for (column a : filteredNewHolder.values.values()) {
                    condHelper.put(a.name, a.type);
                }
                condHolder = new Table("Conditional", condHelper);
                //Check which column is concerned
                for (column a : filteredNewHolder.values.values()) {
                    int operatorIndex = -1;
                    int iiiii = 0;
                    for (String s : condParse) {
                        if (listOperatorsArrayList.contains(s)) {
                            operatorIndex = iiiii;
                            break;
                        }
                        iiiii++;
                    }
                    //Look for a name match
                    if (a.name.equals(condParse.get(0))) {
                        //Check all members of columns A to see that they pass condition
                        int counter = 0;
                        for (int iii = 0; iii < a.items.size(); iii++) {

                            if (a.getType() == "string") {
                                if (!col1.equals("") && String.valueOf(filteredNewHolder.values.get(col1).items.toArray()[iii]).equals("NaN")){
                                    String o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if (!col2.equals("") && String.valueOf(filteredNewHolder.values.get(col2).items.toArray()[iii]).equals("NaN")) {
                                    String o1 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o1)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        String o1 = (String) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        String o2;
                                        try {
                                            o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");

                                        }
                                        if (switchFunctionString(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        String o1 = (String) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionString(o1, condParse.get(operatorIndex), condParse.get(2))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        String o2 = (String) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionString(condParse.get(0), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionString(condParse.get(0), condParse.get(operatorIndex), condParse.get(2))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }
                                }
                            } else if (a.getType() == "int") {
                                if (!col1.equals("") && String.valueOf(filteredNewHolder.values.get(col1).items.toArray()[iii]).equals("NaN")) {
                                    Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if (!col2.equals("") && String.valueOf(filteredNewHolder.values.get(col2).items.toArray()[iii]).equals("NaN")) {
                                    Integer o1 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o1)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        Integer o1 = (Integer) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        Integer o2 = null;
                                        try {
                                            o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");

                                        }
                                        if (switchFunctionInt(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        Integer o1 = (Integer) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionInt(o1, condParse.get(operatorIndex), Integer.parseInt(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionInt(Integer.parseInt(condParse.get(0)), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionInt(Integer.parseInt(condParse.get(0)), condParse.get(operatorIndex), Integer.parseInt(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }

                                }
                            } else if (a.getType() == "float") {
                                if (!col1.equals("") && filteredNewHolder.values.get(col1).items.toArray()[iii].equals("NaN")) {
                                    Integer o2 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o2)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }

                                } else if (!col2.equals("") && filteredNewHolder.values.get(col2).items.toArray()[iii].equals("NaN")) {
                                    Integer o1 = (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                    if (switchFunctionNaN("NaN", condParse.get(operatorIndex), o1)) {
                                        condHolder.addRow(filteredNewHolder.getRow(counter));
                                    }
                                } else {
                                    if (!col1.equals("") && !col2.equals("")) {
                                        Double o1 = (Double) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        Double o2 = null;
                                        try {
                                            o2 = (Double) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        } catch (Exception e) {
                                            try {
                                                o2 = 1.0 * (Integer) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                            } catch (Exception e1) {
                                                throw new IOException("ERROR: columns " + col1 + "and " + col2 + "cannot be compared");
                                            }
                                        }
                                        if (switchFunctionDouble(o1, condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col1.equals("")) {
                                        Double o1 = (Double) filteredNewHolder.values.get(col1).items.toArray()[iii];
                                        if (switchFunctionDouble(o1, condParse.get(operatorIndex), Double.parseDouble(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else if (!col2.equals("")) {
                                        Double o2 = (Double) filteredNewHolder.values.get(col2).items.toArray()[iii];
                                        if (switchFunctionDouble(Double.parseDouble(condParse.get(0)), condParse.get(operatorIndex), o2)) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    } else {
                                        if (switchFunctionDouble(Double.parseDouble(condParse.get(0)), condParse.get(operatorIndex), Double.parseDouble(condParse.get(2)))) {
                                            condHolder.addRow(filteredNewHolder.getRow(counter));
                                        }
                                    }
                                }
                            }
                            counter++;
                        }
                    }
                }
                condParse.clear();
                filteredNewHolder = condHolder;
            }
        }
        return filteredNewHolder;
    }

    private ArrayList<Table> filterFunction(String[] listofTableNamesStrList, String[] listofColums, String exprs) {
        ArrayList<Table> listofTables = new ArrayList<>();
        List<String> arrayHelper = Arrays.asList(listofColums);
        LinkedHashMap helper = new LinkedHashMap();
        for (int i = 0; i < listofTableNamesStrList.length; i++) {
            for (Table x : parserDatabase.getTableArray()) {
                if (x.name.equals(listofTableNamesStrList[i])) {
                    if (exprs.equals("*")) {
                        /*
                        for (column a : x.values.values()) {
                            helper.put(a.name, a.type);
                        }
                        holder = new Table("subtable", helper);
                        for (int k = 0; k < x.rowSize(); k++) {
                            holder.addRow(x.getRow(k));
                        }
                        */
                        listofTables.add(x);
                    } else if (!exprs.equals('*')) {
                        for (int k = 0; k < listofColums.length; k++) {
                            for (column a : x.values.values()) {
                                if (arrayHelper.get(k).equals(a.name)) {
                                    helper.put(a.name, a.type);
                                }
                            }
                        }
                        Table holder;
                        holder = new Table("subtable", helper);
                        ArrayList objectHelper = new ArrayList();
                        List keyOrder = new ArrayList();
                        keyOrder.addAll(holder.values.keySet());
                        for (int g = 0; g < x.rowSize(); g++) {
                            for (int key = 0; key < keyOrder.size(); key++) {
                                for (column a : x.values.values()) {
                                    if (keyOrder.get(key).equals(a.name)) {
                                        objectHelper.add(a.getItems().toArray()[g]);
                                    }
                                }
                            }
                            holder.addRow(objectHelper.toArray());
                            objectHelper.clear();
                        }
                        listofTables.add(holder);
                        helper.clear();
                    }
                }
            }
        }
        return listofTables;
    }
}