import java.util.Arrays;
import java.util.Scanner;

public class F {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int elements = sc.nextInt();
        int lines = sc.nextInt();
        sc.nextLine();
        int[][] disuncts = new int[lines][elements];
        int[][] conuncts = new int[(int) Math.pow(2, elements)][lines];
        int[][] tabl = new int[conuncts.length][elements];
        boolean cur;

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < elements; j++) {
                disuncts[i][j] = sc.nextInt();
            }
        }
//        for (int[] c : disuncts) {
//            System.out.println(Arrays.toString(c));
//        }

        String line;
        for (int i = 0; i < Math.pow(2, elements); i++) {
            line = Integer.toBinaryString(i);
            for (int j = 0; j < line.length(); j++) {
                tabl[i][j] = line.charAt(0) - '0';
            }
        }

        //        System.out.println("tab  " + Arrays.toString(tabl));

        boolean tagZero = true;
        loop1:
        for (int i = 0; i < tabl.length; i++) {
            for (int j = 0; j < tabl[i].length; j++) {
                int curOne = 0;
                for (int k = 0; k < disuncts.length; k++) {
                    if (conuncts[i][k] == 0) {
                        boolean c = tabl[i][j] == 1;

                        if (disuncts[k][j] == 0) {
                            c = !c;
                        } else if (disuncts[k][j] == -1) {
                            continue;
                        }

                        if (c) {
                            conuncts[i][k] = 1;
                            curOne++;
                        }
                    } else {
                        curOne++;
                    }
                }
                if (curOne == lines) {
                    tagZero = false;
                    break loop1;
                }
            }
        }





//        int cntOne = 0;
//        loop1:
//        for (int k = 0; k < disuncts.length; k++) {
//            int globalZero = 0;
//            for (int i = 0; i < tabl.length; i++) {
//                boolean becameOne = false;
//                int cntZero = 0;
//                for (int j = 0; j < tabl[i].length(); j++) {
//                    boolean c = tabl[i].charAt(j) == '1';
//
//                    if (disuncts[k][j] == 0) {
//                        c = !c;
//                    } else if (disuncts[k][j] == -1) {
//                        continue;
//                    }
//                    if (c) {
//                        conuncts[i][k] = 1;
//                        becameOne = true;
//                        break;
//                    }
//                }
//                if (!becameOne) {
//                    globalZero++;
//                } else {
//                    cntOne++;
//                }
//            }
//            if (globalZero == tabl.length) {
//                finished = true;
//                break;
//            }
//        }
//        for (int[] c : conuncts) {
//            System.out.println(Arrays.toString(c));
//        }

//        if (tagZero) {
//            for (int i = 0; i < conuncts.length; i++) {
//                boolean curZero = false;
//                Arrays.sort(conuncts[i]);
//                if (conuncts[i][0] == 1) {
//                    tagZero = false;
//                    break;
////                    for (int j = 0; j < conuncts[i].length; j++) {
////                        if (conuncts[i][j] == 0) {
////                            curZero = true;
////                            break;
////                        }
////                    }
////                    if (!curZero) {
////                        tagZero = false;
////                        break;
////                    }
//                }
//            }
//        }
        if (tagZero) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
