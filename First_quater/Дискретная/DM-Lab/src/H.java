import java.util.Scanner;

public class H {
    public static char a = 'A';
    public static char b = 'B';

    public static StringBuilder answer = new StringBuilder();
    public static StringBuilder cur = new StringBuilder();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                answer.append("((");
                answer.append(cur.toString());
                answer.append("|");
                doubleRelation(answer, a, a, b, b, i);
                answer.append(")");
                answer.append("|");
                justRelation(answer, a, b, i);
                answer.append(")");
                cur.setLength(0);
                cur.append(answer.toString());
                answer.setLength(0);
            } else {
                doubleRelation(cur, a, b, a, b, i);
            }
        }
        System.out.println(cur.toString());
    }

    public static void doubleRelation(StringBuilder str, char a1, char b1, char a2, char b2, int i) {
        str.append("(");
        justRelation(str, a1, b1, i);
        str.append("|");
        justRelation(str, a2, b2, i);
        str.append(")");
    }

    public static void justRelation(StringBuilder str, char a, char b, int i) {
        str.append("(").append(a).append(i).append("|").append(b).append(i).append(")");
    }
}
