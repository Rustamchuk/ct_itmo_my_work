import java.util.Scanner;

public class SortStack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] input = new int[n];
        int[] sortStack = new int[n];
        int target = 1;
        int pos = -1;
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int cur = sc.nextInt();
            if (cur == target) {
                a.append("push").append(System.lineSeparator());
                a.append("pop").append(System.lineSeparator());
                target++;
                if (pos >= 0) {
                    while (pos >= 0 && sortStack[pos] == target) {
                        pos--;
                        a.append("pop").append(System.lineSeparator());
                        target++;
                    }
                }
            } else {
                a.append("push").append(System.lineSeparator());
                sortStack[++pos] = cur;
            }
        }
        if (pos == -1) {
            System.out.println(a.toString());
        } else {
            System.out.println("impossible");
        }
    }
}
