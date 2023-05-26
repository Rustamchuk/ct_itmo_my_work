import java.io.*;
import java.util.*;

public class Isomorphism {
    static class Vertex {
        int id;
        boolean terminal;
        Map<Character, Vertex> transitions = new HashMap<>();

        Vertex(int id) {
            this.id = id;
        }
    }

    private static boolean dfs(Vertex u, Vertex v, Map<Vertex, Vertex> associations, Set<Vertex> visited) {
        visited.add(u);

        if (v.terminal != u.terminal) {
            return false;
        }

        associations.put(u, v);
        boolean result = true;

        for (Map.Entry<Character, Vertex> entry : u.transitions.entrySet()) {
            char c = entry.getKey();
            Vertex t1 = entry.getValue();
            Vertex t2 = v.transitions.get(c);

            if (t2 == null || t1.terminal != t2.terminal) {
                return false;
            }

            if (visited.contains(t1)) {
                result = result && t2 == associations.get(t1);
            } else {
                result = result && dfs(t1, t2, associations, visited);
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("isomorphism.in"));
        PrintWriter out = new PrintWriter(new File("isomorphism.out"));

        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        int a, b, terminalState;
        char c;

        Vertex[] vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i + 1);
        }

        for (int i = 0; i < k; i++) {
            terminalState = in.nextInt();
            vertices[terminalState - 1].terminal = true;
        }

        for (int i = 0; i < m; i++) {
            a = in.nextInt();
            b = in.nextInt();
            c = in.next().charAt(0);
            vertices[a - 1].transitions.put(c, vertices[b - 1]);
        }

        Vertex automaton1 = vertices[0];

        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();

        vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i + 1);
        }

        for (int i = 0; i < k; i++) {
            terminalState = in.nextInt();
            vertices[terminalState - 1].terminal = true;
        }

        for (int i = 0; i < m; i++) {
            a = in.nextInt();
            b = in.nextInt();
            c = in.next().charAt(0);
            vertices[a - 1].transitions.put(c, vertices[b - 1]);
        }

        Vertex automaton2 = vertices[0];

        boolean isomorphism = dfs(automaton1, automaton2, new HashMap<>(), new HashSet<>());
        if (isomorphism) {
            out.println("YES");
        } else {
            out.println("NO");
        }

        in.close();
        out.close();
    }
}