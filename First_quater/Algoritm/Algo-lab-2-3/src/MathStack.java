import java.util.Arrays;
import java.util.Scanner;

public class MathStack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine() + " ";
        int[] mathStack = new int[input.length()];
        int pos = -1;
        String cur;
        int last = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                cur = input.substring(last, i);
                last = i + 1;
//                System.out.println(Arrays.toString(mathStack));
                switch (cur) {
                    case "+" -> {
                        mathStack[pos - 1] = mathStack[pos] + mathStack[pos - 1];
                        pos--;
                    }
                    case "-" -> {
                        mathStack[pos - 1] = mathStack[pos - 1] - mathStack[pos];
                        pos--;
                    }
                    case "*" -> {
                        mathStack[pos - 1] = mathStack[pos] * mathStack[pos - 1];
                        pos--;
                    }
                    default -> {
                        pos++;
                        mathStack[pos] = Integer.parseInt(cur);
                    }
                }
            }
        }
        System.out.println(mathStack[pos]);
    }
}
