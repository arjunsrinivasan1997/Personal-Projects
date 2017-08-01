import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Arjun Srinivasan on 7/6/17.
 */
public class NBodyRunner {

    private static final String EXIT = "exit";
    private static final String PROMPT = "> ";
    private static final double t = 157788000.0;
    private static final double dt = 25000.0;
    private static final String filepath = "data/";


    public static void main(String[] args) throws IOException {
        args = new String[3];
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(PROMPT);
        String line = "";
        while ((line = in.readLine()) != null) {
            if (EXIT.equals(line)) {
                break;
            }

            if (!line.trim().isEmpty()) {
                args[0] = String.valueOf(t);
                args[1] = String.valueOf(dt);
                args[2] = filepath + line;
                NBody.main(args);

            }
            System.out.print(PROMPT);
        }
        in.close();
    }
}

