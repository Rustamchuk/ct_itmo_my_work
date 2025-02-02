package expression.generic;

import expression.generic.exceptions.ParsingException;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Tabulator tabulator = new GenericTabulator();
        Object[][][] out = tabulator.tabulate(args[0], args[1], 0, 8, -7, 9, -6, 3);
        for (Object[][] out1 :
                out) {
            for (Object[] out2 :
                    out1) {
                System.out.println(Arrays.toString(out2));
            }
            System.out.println();
        }
    }
}
