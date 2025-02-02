import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DecodingLZW {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> alphabet = new ArrayList<String>();
        int cur;
        int ind = -1;
        for (int i = 0; i < n; i++) {
            cur = sc.nextInt();
            ind++;
            if (cur < 26) {
                System.out.print((char) (cur + 97));
                alphabet.add(String.valueOf((char) (cur + 97)));
                if (ind > 0) {
                    alphabet.set(ind - 1, alphabet.get(ind - 1) + (char) (cur + 97));
                }
            } else {
//                if (cur - 26 == ind - 1) {
//                    alphabet.set(cur - 26, alphabet.get(cur - 26) + alphabet.get(cur - 26).charAt(0));
//                    ind++;
//                }
                alphabet.set(ind - 1, alphabet.get(ind - 1) + alphabet.get(cur - 26).charAt(0));
                alphabet.add(alphabet.get(cur - 26));
//                if (alphabet.get(cur - 26).length() >= 3) {
//                    alphabet.add(alphabet.get(cur - 26).substring(1, alphabet.get(cur - 26).length()));
//                } else {
//                    alphabet.add(alphabet.get(cur - 26));
//                }
                System.out.print(alphabet.get(cur - 26));
            }
        }
//        System.out.println();
//        for (String a :
//                alphabet) {
//            System.out.println(a);
//        }
    }
}