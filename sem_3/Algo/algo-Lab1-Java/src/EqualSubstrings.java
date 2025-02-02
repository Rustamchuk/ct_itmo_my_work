import java.util.Scanner;

public class EqualSubstrings {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        int n = sc.nextInt();
        int[] hash = new int[str.length() + 1];
        long[] mult = new long[str.length() + 1];
        int m = (int) (1e9 + 9);
        mult[0] = 1;
        mult[1] = 31;
        hash[1] = (int) (str.charAt(0) * mult[1] % m);
        for (int i = 2; i < str.length() + 1; i++) {
            mult[i] = 31 * mult[i - 1] % m;
            hash[i] = (int) ((str.charAt(i - 1) * mult[i] + hash[i - 1]) % m);
        }
        for (int i = 0; i < n; i++) {
            int l1 = sc.nextInt();
            int r1 = sc.nextInt();
            int l2 = sc.nextInt();
            int r2 = sc.nextInt();
            int lr1 = (hash[r1] - hash[l1 - 1] + m) % m;
            int lr2 = (hash[r2] - hash[l2 - 1] + m) % m;
            if (l1 > l2) {
                if (lr1 == lr2 * mult[l1 - l2] % m) {
                    System.out.println("Yes");
                    continue;
                }
            } else {
                if (lr2 == lr1 * mult[l2 - l1] % m) {
                    System.out.println("Yes");
                    continue;
                }
            }
            System.out.println("No");
        }
    }
}