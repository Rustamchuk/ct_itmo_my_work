import java.util.Arrays;
import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        int funLen;
        String cur;
        StringBuilder currentAnswer = new StringBuilder();
        int[] oneNotClass = new int[] {0, 0, 0, 0, 0};
        int[] results;
        String ln;
        int pow;
        int[] newLine;
        int[] finish;
        String[] elements;
        int isFull = 0;

        for (int i = 0; i < n; i++) {
            ln = sc.nextLine();
            funLen = ln.charAt(0) - '0';
            pow = (int) Math.pow(2, funLen);
            results = new int[pow];
            elements = new String[pow];
            for (int j = 0; j < pow; j++) {
                results[j] = ln.charAt(j + 2) - '0';

                // Check not saving 0 and 1 of Post
                if (oneNotClass[0] == 0) {
                    if (j == 0 && results[j] != 0) {
                        oneNotClass[0] = 1;
                        isFull++;
                    }
                }

                if (oneNotClass[1] == 0) {
                    if (j == pow - 1 && results[j] != 1) {
                        oneNotClass[1] = 1;
                        isFull++;
                    }
                }
                cur = Integer.toBinaryString(j);
                if (funLen -cur.length() >= 0) {
                    currentAnswer.append("0".repeat(funLen - cur.length()));
                }
                currentAnswer.append(cur);
                elements[j] = currentAnswer.toString();
                currentAnswer.setLength(0);
            }

            //Check self dual
            if (oneNotClass[3] == 0) {
                boolean selfDual = true;
                for (int j = pow - 1; j >= 0; j--) {
                    int res = results[j];
                    if (res == 1) {
                        res = 0;
                    } else {
                        res = 1;
                    }

                    if (res != results[pow - j - 1]) {
                        selfDual = false;
                        break;
                    }
                }
                if (!selfDual) {
                    oneNotClass[3] = 1;
                    isFull++;
                }
            }

            if (oneNotClass[2] == 0) {
                boolean monoton = true;
                for (int j = pow - 1; j >= 0; j--) {
                    if (results[j] == 0) {
                        for (int p = j - 1; p >= 0; p--) {
                            if (results[p] == 1) {
                                boolean bigger = true;
                                for (int k = 0; k < elements[j].length(); k++) {
                                    if (elements[p].charAt(k) - '0' > elements[j].charAt(k) - '0') {
                                        bigger = false;
                                        break;
                                    }
                                }
                                if (bigger) {
                                    monoton = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!monoton) {
                    oneNotClass[2] = 1;
                    isFull++;
                }
            }

            if (oneNotClass[4] == 0) {
                //Gegalkyn
                boolean polinom = true;

                int curLen = pow;
                finish = new int[curLen];
                int summ = 0;
                int counter = 0;

                while (curLen > 0) {
                    curLen--;
                    newLine = new int[curLen];
                    for (int j = 0; j < curLen; j++) {
                        summ = results[j] + results[j + 1];
                        if (summ == 2) {
                            summ = 0;
                        }
                        newLine[j] = summ;
                    }
                    finish[counter] = results[0];
                    results = newLine;
                    counter++;
                }

                for (int j = pow - 1; j >= 0; j--) {
                    if (finish[j] == 1) {
                        int cntOnes = 0;
                        for (int k = 0; k < elements[j].length(); k++) {
                            if (elements[j].charAt(k) == '1') {
                                cntOnes++;
                                if (cntOnes >= 2) {
                                    break;
                                }
                            }
                        }
                        if (cntOnes >= 2) {
                            polinom = false;
                            break;
                        }
                    }
                }
                if (!polinom) {
                    oneNotClass[4] = 1;
                    isFull++;
                }
            }
            if (isFull == 5) {
                break;
            }
        }

        if (isFull == 5) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
