import java.util.Scanner;

public class NumberToCloseOpen {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        int num = 0;
        int depth = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '(') {
                depth++;
            } else {
                depth--;
                num += depth;
            }
        }
        System.out.println(num + 1);
    }
}
