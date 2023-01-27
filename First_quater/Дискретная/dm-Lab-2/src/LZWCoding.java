import java.util.Arrays;
import java.util.Scanner;

public class LZWCoding {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word = sc.nextLine();
        int i = 0;
        int j;
        int n = word.length();
        String[] alphabet = new String[n];
        int last = 0;
        int code;
        StringBuilder cur = new StringBuilder();
        while (i < n) {
            code = word.charAt(i) - 97;
            cur.append(word.charAt(i));
            j = i + 1;
            while (j < n) {
                cur.append(word.charAt(j));
                boolean added = false;
                for (int k = 0; k < n; k++) {
                    if (alphabet[k] != null) {
                        if (alphabet[k].equals(cur.toString())) {
                            code = k + 26;
                            j++;
                            added = true;
                        }
                    } else {
                        break;
                    }
                }
                if (!added) {
                    alphabet[last++] = cur.toString();
                    cur.setLength(0);
                    break;
                }
            }
            i = j;
            System.out.print(code + " ");
        }
    }
}
