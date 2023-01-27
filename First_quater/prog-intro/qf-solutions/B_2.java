//package ChempionShip;
import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double minn = 2;
        double curDel;
        int k=0;
        int number = 50000;
        double mx = (Math.pow(2, 31) - 1) / number;

        for(int i = 1; i <= mx; i++){
            curDel = i % (2 * Math.PI);
            if(curDel < minn){
                minn = curDel;
                k = i;
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println(k * (-1 * number / 2 + i));
        }
    }
}
