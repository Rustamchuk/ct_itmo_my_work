import java.util.Arrays;
import java.util.Scanner;

public class DecodingBarrous {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String codedWord = sc.nextLine();
        int n = codedWord.length();
        int[] symbolsW = new int[n];
        int[] sortedW = new int[n];

        for (int i = 0; i < n; i++) {
           symbolsW[i] = codedWord.charAt(i);
        }

        int[] possibleNumbers = new int[26];
        for (int i = 0; i < n; i++) {
            possibleNumbers[codedWord.charAt(i) - 97]++;
        }

        int curPos = 0;
        for (int i = 0; i < possibleNumbers.length; i++) {
            for (int j = 0; j < possibleNumbers[i]; j++) {
                sortedW[curPos++] = i + 97;
            }
        }

        int[] poses = new int[n];
        int[] used = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (sortedW[i] == symbolsW[j] && used[j] != 1) {
                    poses[j] = i;
                    used[j] = 1;
                    break;
                }
            }
        }

        int[] answer = new int[n];
        int j = n - 1;
        curPos = 0;
        while (curPos < n) {
            answer[curPos++] = symbolsW[poses[j]];
            for (int i = 0; i < n; i++) {
                if (poses[i] == j) {
                    j = i;
                    break;
                }
            }
        }

        System.arraycopy(answer, 0, used, 0, n);
        int cur = 0;
        for (int i = 1; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (k + i >= n) {
                    cur = k + i - n;
                } else {
                    cur = k + i;
                }
                if (used[k] < answer[cur]) {
                    break;
                } else if (used[k] > answer[cur]) {
                    for (int l = 0; l < n; l++) {
                        if (l + i >= n) {
                            cur = l + i - n;
                        } else {
                            cur = l + i;
                        }
                        used[l] = answer[cur];
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.print((char) used[i]);
        }
    }
}
