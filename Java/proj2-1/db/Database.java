package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Database {
    Parse p = new Parse();
    private ArrayList<Table> tableArray;

    public Database() {

        tableArray = new ArrayList<>();
        p.parserDatabase = this;
    }
    public ArrayList<Table> getTableArray() {
        return tableArray;
    }
    public boolean contains(String s) {
        for (int i = 0; i < tableArray.size(); i++) {
            if (tableArray.get(i).name.equals(s)) {
                if (tableArray.get(i).name.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void selectLarge() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("create table left (a string, b int)");
        commands.add("create table right (c float)");
        for (int i = 0; i < 100; i++) {
            commands.add("insert into left values 'str" + i + "'," + i);
        }
        for (int i = 0; i < 100; i++) {
            double d = i;
            commands.add("insert into right values " + String.format("%.03f", d));
        }
        commands.add("create table t3 as select * from left,right");
        commands.add("print t3");
        commands.add("select * from left,right where c<b");
        runner(Arrays.copyOf(commands.toArray(), commands.toArray().length, String[].class));
    }

    public static void main(String[] args) {
        String[] commands = {
                "create table a (x float,y float)", "insert into a values 87657480192.0,0.0",
                "insert into a values 4147483648.0,0.0",
                "insert into a values 340282346638528860000000000000000000000.0,0.0",
                "create table b as select x,y/y as y from a",
                "print b",
                "select * from b where x < y",
                "create table t (x int,y float)",
                "insert into t values 1000,0.0",
                "insert into t values 2147483647,0.0",
                "insert into t values -2147483648,0.0",
                "create table s as select x,y/y as y from t",
                "print s",
                "select * from s where x < y",
                "load selectNaN",
                "select x/y as a, y/z as b from selectNaN",
                "create table n as select x/y as a, y/z as b from selectNaN",
                "print n",
                "select a+b as c from n",
                "create table test2  (x float, y int)",
                "insert into test2 values 1.0,2",
                "insert into test2 values 3.7,2",
                "select x*y as z from test2",
                "load selectNoValue",
                "select a-c as d, b+c as e from selectNoValue",
                "load fans",
                "create table test  (x int, y float)",
                "insert into test values 1,2.0",
                "insert into test values 2,3.7",
                "select x*y as z from test",
                "load teams",
                "load records",
                "load fans",
                "select City,Season,Wins/Losses as Ratio from teams,records where Ratio < 1",
                "select Firstname,Lastname,Mascot from fans,teams",
                "create table t8 as select Firstname,Lastname,TeamName,Mascot from fans,teams where Mascot <= 'Pat Patriot' and Lastname < 'Ray'",
                "print t8"};
        runner(commands);
        //selectLarge();
    }







    public String transact(String query) {
        String[] helper = new String[]{query};
        try {
            String s = p.main(helper);
             return s;
        } catch (IOException e) {
            return (e.getMessage());
        }
    }
    public static void runner(String[] args) {
        Database x = new Database();
        for (String s : args) {
            String ss = x.transact(s);
            if (!ss.equals("")) {
                System.out.println(ss);
            }
        }
    }
}
