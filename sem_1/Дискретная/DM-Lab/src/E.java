import java.util.Arrays;
import java.util.Scanner;

public class E {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cnt = Integer.parseInt(sc.nextLine());
        int[] results = new int[(int) Math.pow(2, cnt)];
        String cur = "";

        //Треугольник Паскаля
        for (int i = 0; i < Math.pow(2, cnt); i++) {
            cur = sc.nextLine();
            results[i] = cur.charAt(cur.length() - 1) - '0';
        }

        int[] newLine;
        int curLen = (int) Math.pow(2, cnt);
        int[] finish = new int[curLen];
        int summ = 0;
        int counter = 0;

        while (curLen > 0) {
            curLen--;
            newLine = new int[curLen];
            for (int i = 0; i < curLen; i++) {
                summ = results[i] + results[i + 1];
                if (summ == 2) {
                    summ = 0;
                }
                newLine[i] = summ;
            }
            finish[counter] = results[0];
            results = newLine;
            counter++;
        }

        StringBuilder maxLen = new StringBuilder();
        for (int i = 0; i < Math.pow(2, cnt); i++) {
            cur = Integer.toBinaryString(i);
            maxLen.append("0".repeat(cnt - cur.length()));
            maxLen.append(cur);
            System.out.println(maxLen.toString() + " " + finish[i]);
            maxLen.setLength(0);
        }
    }
}
