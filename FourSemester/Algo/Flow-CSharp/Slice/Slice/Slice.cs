using System;
using System.Collections.Generic;

// -----------------
// Nazarov Rustam
// Slice Lesson
// 4 sem Algo Flow
// -----------------

public class Context
{
    public const long Inf = 1000100;
    public int N { get; private set; }
    public int M { get; private set; }

    public void Init()
    {
        var input = Console.ReadLine().Split();
        N = Convert.ToInt32(input[0]);
        M = Convert.ToInt32(input[1]);
    }

    public EdgeContext InitEdge()
    {
        var input = Console.ReadLine().Split();
        var ctx = new EdgeContext(
            Convert.ToInt32(input[0]) - 1,
            Convert.ToInt32(input[1]) - 1,
            Convert.ToInt32(input[2])
        );
        return ctx;
    }
    
    public void PrintResult(List<long> ans, long maxFlow)
    {
        Console.WriteLine($"{ans.Count} {maxFlow}");
        foreach (var it in ans)
        {
            Console.Write($"{it} ");
        }
    }
}

public class EdgeContext
{
    public int U
    {
        get; 
        private set;
    }
    public int V { get; private set; }
    public int C { get; private set; }

    public EdgeContext(int u, int v, int c)
    {
        U = u;
        V = v;
        C = c;
    }
}

public class Slice
{
    static List<long> minCut = new List<long>();
    
    static void AddEdge(List<Edge> edgesList, List<List<long>> g, List<Tuple<long, long>> edgeNums, EdgeContext data, int i)
    {
        var e = new Edge(data.C, 0, data.V, edgesList.Count)
        {
            RevNum = edgesList.Count + 1,
            From = data.U,
            Index = i
        };
        edgesList.Add(e);
        g[data.U].Add(e.Num);

        var w = new Edge(data.C, 0, data.U, edgesList.Count)
        {
            RevNum = e.Num,
            From = data.V,
            Index = i
        };
        edgesList.Add(w);
        g[data.V].Add(w.Num);

        edgeNums.Add(Tuple.Create((long)data.U, (long)g[data.U].Count - 1));
    }
    
    private static List<List<long>> InitializeGraph(int n)
    {
        var g = new List<List<long>>(n);
        for (var i = 0; i < n; i++)
        {
            g.Add(new List<long>());
        }
        return g;
    }

    public static void Main()
    {
        var ctx = new Context();
        ctx.Init();

        var edgeNums = new List<Tuple<long, long>>(ctx.M);
        var g = InitializeGraph(ctx.N);

        var edgesList = new List<Edge>();
        for (var i = 0; i < ctx.M; i++)
        {
            var data = ctx.InitEdge();
            AddEdge(edgesList, g, edgeNums, data, i);
        }

        const int s = 0;
        var t = ctx.N - 1;
        var used = new List<bool>(new bool[ctx.N]);

        while (Dfs(s, Context.Inf, g, used, t, edgesList) > 0)
        {
            used = new List<bool>(new bool[ctx.N]);
        }

        var maxFlow = CalculateMaxFlow(g, edgesList);

        used = new List<bool>(new bool[ctx.N]);
        FindMinCut(s, g, used, edgesList);

        var ans = GetMinCutEdges(edgesList, used);

        ctx.PrintResult(ans, maxFlow);
    }

    static long Dfs(
        long v,
        long minC,
        IReadOnlyList<List<long>> g,
        IList<bool> used, long t,
        IReadOnlyList<Edge> edgesList
    )
    {
        if (v == t)
        {
            return minC;
        }

        used[(int)v] = true;

        for (var i = 0; i < g[(int)v].Count; i++)
        {
            var e = edgesList[(int)g[(int)v][i]];
            if (used[(int)e.To] || e.F >= e.C) continue;
            var delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
            if (delta <= 0) continue;
            edgesList[(int)g[(int)v][i]].F += delta;
            edgesList[(int)edgesList[(int)g[(int)v][i]].RevNum].F -= delta;
            return delta;
        }

        return 0;
    }

    static void FindMinCut(long v, IReadOnlyList<List<long>> g, IList<bool> used, IReadOnlyList<Edge> edgesList)
    {
        if (used[(int)v])
        {
            return;
        }

        used[(int)v] = true;

        for (var i = 0; i < g[(int)v].Count; i++)
        {
            var e = edgesList[(int)g[(int)v][i]];

            if (e.F == e.C)
            {
                minCut.Add(e.Index);
            }

            if (!used[(int)e.To] && e.F < e.C)
            {
                FindMinCut(e.To, g, used, edgesList);
            }
        }
    }
    
    private static long CalculateMaxFlow(IReadOnlyList<List<long>> g, IReadOnlyList<Edge> edgesList)
    {
        return g[0].Select(it => edgesList[(int)it]).Select(e1 => Math.Abs(e1.F)).Sum();
    }

    private static List<long> GetMinCutEdges(IReadOnlyList<Edge> edgesList, IReadOnlyList<bool> used)
    {
        var ans = new List<long>();
        for (var i = 0; i < edgesList.Count; i += 2)
        {
            if (used[(int)edgesList[i].From] ^ used[(int)edgesList[i].To])
            {
                ans.Add(edgesList[i].Index + 1);
            }
        }
        return ans;
    }
}

// public class Program
// {
//     public static void Main()
//     {
//         var ctx = new Context();
//         ctx.Init();
//
//         var g = new List<List<long>>(ctx.N);
//         for (int i = 0; i < ctx.N; i++) g.Add(new List<long>());
//         var edgesList = new List<Edge>();
//
//         for (int i = 0; i < ctx.M; i++)
//         {
//             var input = ctx.InitEdge();
//
//             var e = new Edge(input.C, 0, input.V, edgesList.Count)
//             {
//                 RevNum = edgesList.Count + 1,
//                 From = input.U
//             };
//             edgesList.Add(e);
//             g[input.U].Add(e.Num);
//
//             var w = new Edge(input.C, 0, input.U, edgesList.Count)
//             {
//                 RevNum = e.Num,
//                 From = input.V
//             };
//             edgesList.Add(w);
//             g[input.V].Add(w.Num);
//         }
//
//         const int s = 0;
//         var t = ctx.N - 1;
//         var used = new List<bool>(new bool[ctx.N]);
//
//         while (Dfs(s, Context.Inf, g, used, t, edgesList) > 0)
//         {
//             used.Clear();
//             used.AddRange(new bool[ctx.N]);
//         }
//
//         var maxFlow = g[0].Select(it => edgesList[(int)it]).Select(e1 => Math.Abs(e1.F)).Sum();
//
//         Console.WriteLine(maxFlow);
//
//         for (int i = 0; i < edgesList.Count; i += 2)
//         {
//             Console.WriteLine(edgesList[i].F == 0 ? -edgesList[i + 1].F : edgesList[i].F);
//         }
//     }
//
//     static long Dfs(long v, long minC, List<List<long>> g, List<bool> used, long t, List<Edge> edgesList)
//     {
//         if (v == t)
//         {
//             return minC;
//         }
//
//         used[(int)v] = true;
//
//         for (int i = 0; i < g[(int)v].Count; i++)
//         {
//             Edge e = edgesList[(int)g[(int)v][i]];
//             if (!used[(int)e.To] && e.F < e.C)
//             {
//                 long delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
//                 if (delta > 0)
//                 {
//                     edgesList[(int)g[(int)v][i]].F += delta;
//                     edgesList[(int)edgesList[(int)g[(int)v][i]].RevNum].F -= delta;
//                     return delta;
//                 }
//             }
//         }
//
//         return 0;
//     }
// }
//
// public class Context
// {
//     public const long Inf = 1000100;
//     public int N { get; private set; }
//     public int M { get; private set; }
//
//     public void Init()
//     {
//         N = Convert.ToInt32(Console.ReadLine());
//         M = Convert.ToInt32(Console.ReadLine());
//     }
//
//     public EdgeContext InitEdge()
//     {
//         var input = Console.ReadLine().Split();
//         var ctx = new EdgeContext(
//             Convert.ToInt32(input[0]) - 1,
//             Convert.ToInt32(input[1]) - 1,
//             Convert.ToInt32(input[2])
//         );
//         return ctx;
//     }
// }
//
// public class EdgeContext
// {
//     public int U { get; private set; }
//     public int V { get; private set; }
//     public int C { get; private set; }
//
//     public EdgeContext(int u, int v, int c)
//     {
//         U = u;
//         V = v;
//         C = c;
//     }
// }
//
// public class Edge
// {
//     public long C { get; private set; }
//     public long From { get; set; }
//     public long To { get; private set; }
//     public long Num { get; private set; }
//     public long RevNum { get; set; }
//     public long F { get; set; }
//
//     public Edge(long c, long f, long to, long num)
//     {
//         C = c;
//         F = f;
//         To = to;
//         Num = num;
//     }
// }

public class Edge
{
    public long C { get; private set; }
    public long From { get; set; }
    public long Index { get; set; }
    public long To { get; private set; }
    public long Num { get; private set; }
    public long RevNum { get; set; }
    public long F { get; set; }

    public Edge(long c, long f, long to, long num)
    {
        C = c;
        F = f;
        To = to;
        Num = num;
    }
}