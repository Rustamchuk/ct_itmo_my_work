import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WordDKA {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new FileReader("problem1.in"));
        String line = sc.nextLine();
        int vars = sc.nextInt();
        int n = sc.nextInt();
        int end = sc.nextInt();
        boolean[] ends = new boolean[vars];
        int[][] stats = new int[vars][26];

        for (int i = 0; i < end; i++) {
            ends[sc.nextInt() - 1] = true;
        }
        for (int i = 0; i < n; i++) {
            int p = sc.nextInt() - 1;
            int t = sc.nextInt();
            int l = sc.next().charAt(0) - 'a';
            stats[p][l] = t;
        }
        int ind = 0;
        for (int i = 0; i < line.length(); i++) {
            ind = stats[ind][line.charAt(i) - 'a'] - 1;
            if (ind == -1) {
                break;
            }
        }
        FileWriter out = new FileWriter("problem1.out");
        if (ind != -1 && ends[ind]) {
            out.write("Accepts");
        } else {
            out.write("Rejects");
        }
        out.close();
    }
}
