/*
 using System;
using System.Collections.Generic;

class Program
{
    static int n, m;
    static List<int>[] tree;
    static int[,] up;
    static int[] depth;
    static int LOG;

    static void Main()
    {
        // Чтение входных данных
        n = int.Parse(Console.ReadLine());
        tree = new List<int>[n + 1];
        for (int i = 1; i <= n; i++)
            tree[i] = new List<int>();

        for (int i = 0; i < n - 1; i++)
        {
            var edge = Console.ReadLine().Split();
            int a = int.Parse(edge[0]);
            int b = int.Parse(edge[1]);
            tree[a].Add(b);
            tree[b].Add(a);
        }

        // Инициализация
        LOG = (int)Math.Log2(n) + 1;
        up = new int[n + 1, LOG];
        depth = new int[n + 1];

        // Построение двоичных подъёмов
        DFS(1, 1);

        // Обработка запросов
        m = int.Parse(Console.ReadLine());
        for (int i = 0; i < m; i++)
        {
            var query = Console.ReadLine().Split();
            int x = int.Parse(query[0]);
            int y = int.Parse(query[1]);
            Console.WriteLine(IsPath(x, y) ? "Yes" : "No");
        }
    }

    static void DFS(int v, int p)
    {
        up[v, 0] = p;
        for (int i = 1; i < LOG; i++)
            up[v, i] = up[up[v, i - 1], i - 1];

        foreach (var u in tree[v])
        {
            if (u != p)
            {
                depth[u] = depth[v] + 1;
                DFS(u, v);
            }
        }
    }

    static bool IsPath(int a, int b)
    {
        int lca = LCA(a, b);
        return lca == a || lca == b || lca == 1;
    }

    static int LCA(int a, int b)
    {
        if (depth[a] < depth[b])
            (a, b) = (b, a);

        for (int i = LOG - 1; i >= 0; i--)
        {
            if (depth[a] - (1 << i) >= depth[b])
                a = up[a, i];
        }

        if (a == b)
            return a;

        for (int i = LOG - 1; i >= 0; i--)
        {
            if (up[a, i] != up[b, i])
            {
                a = up[a, i];
                b = up[b, i];
            }
        }

        return up[a, 0];
    }
}
 */
using System;
using System.Collections.Generic;
using System.Linq;

class Program
{
    static void Main()
    {
        int n = int.Parse(Console.ReadLine());
        string[] nicknames = new string[n];
        for (int i = 0; i < n; i++)
        {
            nicknames[i] = Console.ReadLine();
        }

        int maxPrefixLength = FindMaxCommonPrefixLength(nicknames);
        Console.WriteLine(maxPrefixLength);
    }

    static int FindMaxCommonPrefixLength(string[] nicknames)
    {
        string concatenated = string.Join("#", nicknames) + "$";
        int[] suffixArray = BuildSuffixArray(concatenated);
        int[] lcpArray = BuildLCPArray(concatenated, suffixArray);

        int maxLcp = 0;
        int maxPos = -1;
        int n = nicknames.Length;
        int[] id = new int[concatenated.Length];
        int idx = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < nicknames[i].Length; j++)
            {
                id[idx++] = i;
            }
            if (i < n - 1)
            {
                id[idx++] = -1;
            }
        }
        id[idx] = -1;

        for (int i = 0; i < lcpArray.Length; i++)
        {
            int minLcp = lcpArray[i];
            HashSet<int> unique = new HashSet<int>();
            for (int j = i; j < lcpArray.Length && minLcp > 0; j++)
            {
                unique.Add(id[suffixArray[j]]);
                minLcp = Math.Min(minLcp, lcpArray[j]);
                if (unique.Count == n && minLcp > maxLcp)
                {
                    maxLcp = minLcp;
                    maxPos = suffixArray[i];
                }
            }
        }

        return maxLcp;
    }

    static int[] BuildSuffixArray(string s)
    {
        int n = s.Length, alphabet = 256;
        int[] p = new int[n], c = new int[n], cnt = new int[Math.Max(alphabet, n)];
        int[] ptmp = new int[n], ctmp = new int[n];

        for (int i = 0; i < n; i++) cnt[s[i]]++;
        for (int i = 1; i < alphabet; i++) cnt[i] += cnt[i - 1];
        for (int i = 0; i < n; i++) p[--cnt[s[i]]] = i;

        c[p[0]] = 0;
        int classes = 1;
        for (int i = 1; i < n; i++)
        {
            if (s[p[i]] != s[p[i - 1]]) classes++;
            c[p[i]] = classes - 1;
        }

        for (int h = 0; (1 << h) < n; ++h)
        {
            for (int i = 0; i < n; i++)
            {
                ptmp[i] = p[i] - (1 << h);
                if (ptmp[i] < 0) ptmp[i] += n;
            }
            Array.Fill(cnt, 0, 0, classes);
            for (int i = 0; i < n; i++) cnt[c[ptmp[i]]]++;
            for (int i = 1; i < classes; i++) cnt[i] += cnt[i - 1];
            for (int i = n - 1; i >= 0; i--) p[--cnt[c[ptmp[i]]]] = ptmp[i];

            ctmp[p[0]] = 0;
            classes = 1;
            for (int i = 1; i < n; i++)
            {
                var cur = (c[p[i]], c[(p[i] + (1 << h)) % n]);
                var prev = (c[p[i - 1]], c[(p[i - 1] + (1 << h)) % n]);
                if (cur != prev) ++classes;
                ctmp[p[i]] = classes - 1;
            }
            Array.Copy(ctmp, c, n);
        }

        return p;
    }

    static int[] BuildLCPArray(string s, int[] suffixArray)
    {
        int n = s.Length;
        int[] rank = new int[n];
        int[] lcp = new int[n];
        for (int i = 0; i < n; i++) rank[suffixArray[i]] = i;

        int h = 0;
        for (int i = 0; i < n; i++)
        {
            if (rank[i] > 0)
            {
                int j = suffixArray[rank[i] - 1];
                while (i + h < n && j + h < n && s[i + h] == s[j + h])
                {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) h--;
            }
        }

        return lcp;
    }
}