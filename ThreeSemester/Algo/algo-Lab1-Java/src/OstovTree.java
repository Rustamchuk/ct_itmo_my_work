//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//public class OstovTree {
//    public static int[][] edges;
//    public static SortedSet<Integer> map;
//    public static int time = 0;
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int n = sc.nextInt();
//        int k = sc.nextInt();
//        edges = new int[n][3];
//        map = new TreeSet<>();
//        for (int j = 0; j < n; j++)
//        {
//            edges.add(new ArrayList<>());
//            edges.get(0).add(new int[1]);
//        }
//        for (int j = 1; j <= k; j++) {
//            int a = sc.nextInt();
//            int b = sc.nextInt();
//            edges.get(a).add(new int[]{b, sc.nextInt()});
//            edges.get(b).add(new int[]{a, sc.nextInt()});
//        }
//
//    }
//}
