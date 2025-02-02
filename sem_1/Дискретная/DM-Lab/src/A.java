import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[][] elements = new int[n][n];
        int[] qualities = new int[] {1, 1, 1, 1, 1};
        getQualities(elements, qualities, n, sc);

        int[][] elements2 = new int[n][n];
        int[] qualities2 = new int[] {1, 1, 1, 1, 1};
        getQualities(elements2, qualities2, n, sc);

        for (int cur : qualities) {
            System.out.print(cur + " ");
        }
        System.out.println();

        for (int cur : qualities2) {
            System.out.print(cur + " ");
        }
        System.out.println();

        int[][] sums = new int[n][n];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements.length; j++) {
                if (elements[i][j] == 1) {
                    for (int k = 0; k < n; k++) {
                        if (elements2[j][k] == 1) {
                            sums[i][k] = 1;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i ++) {
            for (int el : sums[i]) {
                System.out.print(el + " ");
            }
            System.out.println();
        }
    }

    public static void getQualities(int[][] elements, int[] qualities, int n, Scanner sc) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                elements[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j && elements[i][j] == 0) {
                    qualities[0] = 0;
                }

                if (i == j && elements[i][j] == 1) {
                    qualities[1] = 0;
                }

                if (elements[i][j] != elements[j][i]) {
                    qualities[2] = 0;
                }

                if (elements[i][j] == elements[j][i] && elements[i][j] == 1 && i != j) {
                    qualities[3] = 0;
                }

                if (elements[i][j] == 1) {
                    for (int k = 0; k < n; k++) {
                        if (elements[j][k] == 1) {
                            if (elements[i][k] == 0) {
                                qualities[4] = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
