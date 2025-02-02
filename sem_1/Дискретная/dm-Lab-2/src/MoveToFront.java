import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MoveToFront {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String start = sc.nextLine();
        int n = start.length();

        int maxSymbol = 0;
        for (int i = 0; i < n; i++) {
            if (maxSymbol < start.charAt(i) - 97) {
                maxSymbol = start.charAt(i) - 97;
            }
        }
        List<Character> word = new ArrayList<Character>();
        for (int i = 0; i <= maxSymbol; i++) {
            word.add((char) (i + 97));
        }
        char cur;
        for (int i = 0; i < n; i++) {
            cur = start.charAt(i);
            System.out.print((word.indexOf(cur) + 1) + " ");
            word.add(0, word.remove(word.indexOf(cur)));
        }
    }
}
