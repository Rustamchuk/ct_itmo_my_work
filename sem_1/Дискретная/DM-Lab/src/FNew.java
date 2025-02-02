import java.util.Arrays;
import java.util.Scanner;

public class FNew {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int elements = sc.nextInt();
        int lines = sc.nextInt();
        int[][] func = new int[lines][elements];
        boolean[] exist = new boolean[elements];
        for (int i = 0; i < elements; i++) {
            exist[i] = true;
        }
        int numberOfNone;
        int cur = 0;
        int cntF = 0;
        int last = 0;
        for (int i = 0; i < lines; i++) {
            numberOfNone = 0;
            for (int j = 0; j < elements; j++) {
                func[i][j] = sc.nextInt();
                if (func[i][j] == -1) {
                    numberOfNone++;
                } else {
                    cur = j;
                }
            }
            if (numberOfNone == elements - 1 && func[i][cur] == 0) {
                exist[cur] = false;
                cntF++;
            } else {
                last = cur;
            }
        }
//        System.out.println(Arrays.toString(exist));
        boolean searchZer = true;
        boolean searchOne = true;

        check:
        if (cntF >= elements - 1) {
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < elements; j++) {
                    if (func[i][j] == 1 && searchOne) {
                        searchOne = false;
                    } else if (func[i][j] == 0 && searchZer) {
                        searchZer = false;
                    }
                    if (!searchZer && !searchOne) {
                        System.out.println("YES");
                        break check;
                    }
                }
            }
            System.out.println("NO");
        } else {
            System.out.println("NO");
        }
    }
}
