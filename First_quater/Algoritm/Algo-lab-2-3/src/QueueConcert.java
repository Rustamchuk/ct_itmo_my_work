import java.util.Arrays;
import java.util.Scanner;

public class QueueConcert {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] queue = new int[n];
        int[] poses = new int[n];
        int pos = -1;
        int start = 0;
        for (int i = 0; i < n; i++) {
            switch (sc.nextInt()) {
                case 1 -> {
                    queue[++pos] = sc.nextInt();
                    while (queue[pos] - 1 >= poses.length)
                        poses = Arrays.copyOf(poses, poses.length * 2);
                    poses[queue[pos] - 1] = pos;
                }
                case 2 -> {
                    start++;
                }
                case 3 -> {
                    pos--;
                }
                case 4 -> {
                    System.out.println(poses[sc.nextInt() - 1] - start);
                }
                case 5 -> {
                    System.out.println(queue[start]);
                }
            }
        }
    }
}
