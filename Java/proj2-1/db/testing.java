package db;

/**
 * Created by arjunsrinivasan on 3/4/17.
 */
public class testing {
    private static boolean switchFunctionString(String x, String operation, String y) {
        switch (operation) {
            case ("=="):
                return x.equals(y);
            case ("!="):
                return (!x.equals(y));
            case (">"):
                int a = x.compareTo(y) ;

                return a > 0;
            case ("<"):
                int b =x.compareTo(y);
                return b < 0;
            case ("<="):
                return x.compareTo(y) <= 0;
            case (">="):
                return x.compareTo(y) >= 0;
        }
        return false;
    }
    public static void main(String[] args){
        System.out.println("Golden Bears".compareTo("Sport"));
        System.out.println(switchFunctionString("Golden Bears",">","Sport"));
        System.out.println(switchFunctionString("Steelers",">","Sport"));

    }
}
