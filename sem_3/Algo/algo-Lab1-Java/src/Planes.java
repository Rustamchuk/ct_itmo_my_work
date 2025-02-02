import java.util.*;


public class Planes {

    static int graph[][];

    static boolean g[][], used[];

    static int n;


    //type = 0 original edges

    //type = 1 reversed edges

    static void dfs(int v, int type) {

        used[v] = true;

        for (int i = 0; i < n; i++)

            if ((type == 1 ? g[i][v] : g[v][i]) && !used[i]) dfs(i, type);

    }


    static boolean AllVisited() {

        for (int i = 0; i < n; i++)

            if (!used[i]) return false;

        return true;

    }


    public static void main(String[] args) {

        Scanner con = new Scanner(System.in);

        n = con.nextInt();

        graph = new int[n][n];

        for (int i = 0; i < n; i++)

            for (int j = 0; j < n; j++)

                graph[i][j] = con.nextInt();


        g = new boolean[n][n];

        used = new boolean[n];

        int L = 0, R = (int) 1e9;

        while (L < R) {

            int Mid = (L + R) / 2;

            for (int i = 0; i < n; i++)

                for (int j = 0; j < n; j++)

                    g[i][j] = (graph[i][j] <= Mid);


            Arrays.fill(used, false);

            dfs(0, 0);


            boolean flag = false;

            if (AllVisited()) {

                Arrays.fill(used, false);

                dfs(0, 1);

                if (!AllVisited()) flag = true;

            } else flag = true;


            if (flag == false) R = Mid;
            else L = Mid + 1;

        }


        System.out.println(L);

        con.close();

    }

} 