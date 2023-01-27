import java.util.Arrays;
import java.util.Scanner;

public class SolveProblemHemming {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n == 1) {
            hemmingCoding(sc);
        } else {
            hemmingDecoding(sc);
        }
    }

    private static void hemmingCoding(Scanner in) {
        int i = 0;
        int j = 1;
        int mult = 1;
        StringBuilder code = new StringBuilder(in.next());
        int[] poses = new int[code.length()];
        int c = 0;
        int controlBits = 0;
        int insertInd = 1;
        while (true) {
            code.insert(insertInd - 1, '0');
            insertInd *= 2;
            controlBits++;
            if (code.length() - c < c) {
                break;
            }
            c = insertInd;
        }


        for (int k = 0; k < controlBits; k++) {
            int cur = 0;
            int pos = 0;
            int l = mult - 1;
            pos = l;
            int m = 0;
            while (l < code.length()) {
                if (code.charAt(l) == '1') {
                    cur++;
                }
                m++;
                if (m == mult) {
                    l += (mult + 1);
                    m = 0;
                } else {
                    l++;
                }
            }
            mult *= 2;
            if (cur % 2 != 0) {
                code.setCharAt(pos, '1');
            }
        }
        System.out.println(code.toString());
    }

    private static void hemmingDecoding(Scanner in) {
        String a = in.next();
        StringBuilder codeWordIN = new StringBuilder(a);
        StringBuilder codeWordMY = new StringBuilder(a);
        int mult;

        int c = 0;
        int controlBits = 0;
        int insertInd = 1;
        while (true) {
            insertInd *= 2;
            controlBits++;
            if (codeWordIN.length() - c < c) {
                break;
            }
            c = insertInd;
        }


        int finalPos = 0;
        mult = 1;
        for (int k = 0; k < controlBits; k++) {
            int cur = 0;
            int pos = 0;
            int l = mult - 1;
            pos = l;
            int m = 0;
            while (l < codeWordIN.length()) {
                if (codeWordIN.charAt(l) == '1') {
                    cur++;
                }
                m++;
                if (m == mult) {
                    l += (mult + 1);
                    m = 0;
                } else {
                    l++;
                }
            }
            mult *= 2;
            if (codeWordIN.charAt(pos) == '1') {
                cur--;
            }
            if (cur % 2 != 0) {
                codeWordMY.setCharAt(pos, '1');
            } else {
                codeWordMY.setCharAt(pos, '0');
            }
            if (codeWordMY.charAt(pos) != codeWordIN.charAt(pos)) {
                finalPos += pos + 1;
            }
        }

        if (finalPos != 0) {
            finalPos--;
            if (codeWordMY.charAt(finalPos) == '1')
                codeWordMY.setCharAt(finalPos, '0');
            else
                codeWordMY.setCharAt(finalPos, '1');
        }
        mult = 1;
        int o = 0;
        for (int j = 0; j < controlBits; j++) {
            codeWordMY.deleteCharAt(mult - 1 - o);
            mult *= 2;
            o++;
        }
//        System.out.println(Arrays.toString(codeWordMY));
        System.out.println(codeWordMY.toString());
    }
}