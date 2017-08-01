package db;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by arjunsrinivasan on 2/24/17.
 */
public class tableTest {
    @Test
    public void testConstructor() {
        LinkedHashMap<String, String> a = new LinkedHashMap<>();
        a.put("Numbers", "int");
        a.put("Float", "float");
        a.put("Strings", "string");
        Table t = new Table("t1", a);
        Object[] b = {1, 2.1235, "A"};
        t.addRow(b);
        t.addRow(b);
        Object[] c = {1, 2.1235, "A"};
        String ss =t.printTable();
        Object[] d = {"1","2.1235","A"};
        Table t1 =t.clone();
        String[] s= {"Float","Numbers","Strings"};
        Table t1sorted = t1.sort(s);
        Object[] e = {2.1235,1,"A"};
        assertEquals(true,t1.contains("Float"));
        assertEquals(false,t1.contains("Ffloat"));
        assertEquals(true,t1.contains("Numbers"));
        assertArrayEquals(e,t1sorted.getRow(0));
        assertArrayEquals(e,t1sorted.getRow(1));
        assertEquals(3,t1sorted.columnSize());
        assertEquals(2,t1sorted.rowSize());
        assertArrayEquals(c, t.getRow(0));
        assertArrayEquals(c, t.getRow(1));
        assertArrayEquals(c,t1.getRow(0));
        assertArrayEquals(c,t1.getRow(1));
        assertEquals(t.printTable(),t1.printTable());
        assertEquals(3,t1.columnSize());
        assertEquals(2,t1.rowSize());
        assertArrayEquals(d,t.getRowString(0));
    }

    @Test
    public void testOrderedKeys() {
        String[] a = {"X", "Y", "Z"};
        String[] b = {"X", "F", "Y"};
        String[] c = {"X", "Y", "CC", "Z", "T1", "F", "T2"};
        String[] z = {"X", "Y", "Z"};
        String[] x = {"X", "Y", "F"};
        assertArrayEquals(c, Table.columnsInOrder(a, b));
        assertArrayEquals(z, Table.t1Columns(c));
        assertArrayEquals(x, Table.t2columns(c));
        String[] d = {"X", "Y", "Z"};
        String[] e = {"A", "B", "C"};
        String[] f = {"CC", "X", "Y", "Z", "T1", "A", "B", "C", "T2"};
        String[] y = {"X", "Y", "Z"};
        String[] n = {"A", "B", "C"};
        assertArrayEquals(f, Table.columnsInOrder(d, e));
        assertArrayEquals(y, Table.t1Columns(f));
        assertArrayEquals(n, Table.t2columns(f));
        String[] g = {"X", "Y", "Z", "W"};
        String[] h = {"W", "B", "Z"};
        String[] i = {"Z", "W", "CC", "X", "Y", "T1", "B", "T2"};
        assertArrayEquals(i, Table.columnsInOrder(g, h));
    }
    @Test
    public void printTableTest(){
        LinkedHashMap<String, String> aa = new LinkedHashMap<>();
        aa.put("X", "int");
        aa.put("Y", "int");
        LinkedHashMap<String, String> bb = new LinkedHashMap<>();
        bb.put("X", "int");
        bb.put("Z", "int");
        Object[][] a = {{2, 5}, {8, 3}, {13, 7}};
        Object[][] c = {{2, 4}, {8, 9}, {10, 1}, {11, 1}};
        Table t1 = new Table("a", aa);
        Table t2 = new Table("b", bb);
        t1.addRows(a);
        String s = t1.printTable();
        System.out.println(s);
    }
    @Test
    public void testGetRow() {
        Object[][] o = {{1, 7, 2, 10}, {7, 7, 4, 1}, {1, 9, 9, 1}};
        LinkedHashMap<String, String> a = new LinkedHashMap<>();
        a.put("X", "int");
        a.put("Y", "int");
        a.put("Z", "int");
        a.put("W", "int");
        Table t7 = new Table("t7", a);
        t7.addRows(o);
        t7.printTable();
        ArrayList<Integer> aa = new ArrayList<>();
        String[] b = {"Y", "Z", "X", "W"};
        Object[] c = t7.getRow(7, "Y", aa, b).values();
        Object[] d = {7, 2, 1, 10};
        assertArrayEquals(d, c);
        aa.add(0);
        Object[] e = {7, 4, 7, 1};
        Object[] f = t7.getRow(7, "Y", aa, b).values();
        assertArrayEquals(e, f);

    }
    @Test
    public void testJoin() {
        LinkedHashMap<String, String> aa = new LinkedHashMap<>();
        aa.put("X", "int");
        aa.put("Y", "int");
        LinkedHashMap<String, String> bb = new LinkedHashMap<>();
        bb.put("X", "int");
        bb.put("Z", "int");
        Object[][] a = {{2, 5}, {8, 3}, {13, 7}};
        Object[][] c = {{2, 4}, {8, 9}, {10, 1}, {11, 1}};
        Table t1 = new Table("a", aa);
        Table t2 = new Table("b", bb);
        t1.addRows(a);
        Table t15 = t1.join(t1);
        t15.printTable();
        t2.addRows(c);
        Table t3 = t1.join(t2);
        t3.printTable();
        assertEquals(3, t3.columnSize());
        Object[] e = {2, 5, 4};
        Object[] f = {8, 3, 9};
        assertArrayEquals(e, t3.getRow(0));
        assertArrayEquals(f, t3.getRow(1));
        Object[][] g = {{1, 4}, {2, 5}, {3, 6}};
        Object[][] h = {{1, 7}, {7, 7}, {1, 9}, {1, 11}};
        Table t4 = new Table("c", aa);
        t4.addRows(g);
        Table t5 = new Table("d", bb);
        t5.addRows(h);
        Table t6 = t4.join(t5);
        t6.printTable();
        Object[] l = {1, 4, 7};
        Object[] m = {1, 4, 9};
        Object[] n = {1, 4, 11};
        assertEquals(3, t6.columnSize());
        assertArrayEquals(t6.getRow(0), l);
        assertArrayEquals(t6.getRow(1), m);
        assertArrayEquals(t6.getRow(2), n);
        Object[][] o = {{1, 7, 2, 10}, {7, 7, 4, 1}, {1, 9, 9, 1}};
        Object[][] p = {{1, 7, 4}, {7, 7, 3}, {1, 9, 6}, {1, 11, 9}};
        bb.clear();
        aa.put("Z", "int");
        aa.put("W", "int");
        bb.put("W", "int");
        bb.put("B", "int");
        bb.put("Z", "int");
        Table t7 = new Table("t7", aa);
        Table t8 = new Table("t8", bb);
        t7.addRows(o);
        t8.addRows(p);
        Table t9 = t7.join(t8);
        Object[] s = {4, 1, 7, 7, 7};
        Object[] t = {9, 1, 1, 9, 11};
        assertEquals(t9.columnSize(), 5);
        assertArrayEquals(s, t9.getRow(0));
        assertArrayEquals(t, t9.getRow(1));
        t9.printTable();
        Object[][] u = {{1, 7}, {7, 7}, {1, 9}};
        Object[][] v = {{3, 8}, {4, 9}, {5, 10}};
        aa.remove("Z");
        aa.remove("W");
        bb.clear();
        bb.put("X", "int");
        bb.put("Z", "int");
        Table t10 = new Table("t10", aa);
        t10.addRows(u);
        Table t11 = new Table("t11", bb);
        t11.addRows(v);
        Table t12 = t10.join(t11);
        t12.printTable();
        assertEquals(3, t12.columnSize());
        assertEquals(0, t12.rowSize());
        Object[][] z = {{7, 0}, {2, 8}};
        bb.clear();
        bb.put("A", "int");
        bb.put("B", "int");
        Table t13 = new Table("t13", bb);
        t13.addRows(z);
        Table t14 = t3.join(t13);
        t14.printTable();
    }
}