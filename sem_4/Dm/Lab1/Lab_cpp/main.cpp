import java.io.*;
import java.util.*;

public class Main {
public static void main(String[] args) throws IOException {
        int n, m, k;
        int[] x = new int[10];
        int[] y = new int[10];
        int big = -1, ax = 0, ay = 0;

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            x[i] = Integer.parseInt(st.nextToken());
            y[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int sml = Math.abs(i - x[0]) + Math.abs(j - y[0]);
                for (int t = 1; t < k; t++) {
                    sml = Math.min(sml, Math.abs(i - x[t]) + Math.abs(j - y[t]));
                }
                if (sml > big) {
                    big = sml;
                    ax = i;
                    ay = j;
                }
            }
        }

        pw.println(ax + " " + ay);
        br.close();
        pw.close();
    }
}