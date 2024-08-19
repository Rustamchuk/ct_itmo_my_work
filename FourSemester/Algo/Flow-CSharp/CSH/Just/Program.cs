using System;
using System.Collections.Generic;

public class Program
{
    public static void Main()
    {
        var ctx = new Context();
        ctx.Init();

        var g = new List<List<long>>(ctx.N);
        for (int i = 0; i < ctx.N; i++) g.Add(new List<long>());
        var edgesList = new List<Edge>();

        for (int i = 0; i < ctx.M; i++)
        {
            var input = ctx.InitEdge();

            var e = new Edge(input.C, 0, input.V, edgesList.Count)
            {
                RevNum = edgesList.Count + 1,
                From = input.U
            };
            edgesList.Add(e);
            g[input.U].Add(e.Num);

            var w = new Edge(input.C, 0, input.U, edgesList.Count)
            {
                RevNum = e.Num,
                From = input.V
            };
            edgesList.Add(w);
            g[input.V].Add(w.Num);
        }

        const int s = 0;
        var t = ctx.N - 1;
        var used = new List<bool>(new bool[ctx.N]);

        while (Dfs(s, Context.Inf, g, used, t, edgesList) > 0)
        {
            used.Clear();
            used.AddRange(new bool[ctx.N]);
        }

        var maxFlow = g[0].Select(it => edgesList[(int)it]).Select(e1 => Math.Abs(e1.F)).Sum();

        Console.WriteLine(maxFlow);

        for (int i = 0; i < edgesList.Count; i += 2)
        {
            Console.WriteLine(edgesList[i].F == 0 ? -edgesList[i + 1].F : edgesList[i].F);
        }
    }

    static long Dfs(long v, long minC, List<List<long>> g, List<bool> used, long t, List<Edge> edgesList)
    {
        if (v == t)
        {
            return minC;
        }

        used[(int)v] = true;

        for (int i = 0; i < g[(int)v].Count; i++)
        {
            Edge e = edgesList[(int)g[(int)v][i]];
            if (!used[(int)e.To] && e.F < e.C)
            {
                long delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
                if (delta > 0)
                {
                    edgesList[(int)g[(int)v][i]].F += delta;
                    edgesList[(int)edgesList[(int)g[(int)v][i]].RevNum].F -= delta;
                    return delta;
                }
            }
        }

        return 0;
    }
}

public class Context
{
    public const long Inf = 1000100;
    public int N { get; private set; }
    public int M { get; private set; }

    public void Init()
    {
        N = Convert.ToInt32(Console.ReadLine());
        M = Convert.ToInt32(Console.ReadLine());
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
}

public class EdgeContext
{
    public int U { get; private set; }
    public int V { get; private set; }
    public int C { get; private set; }

    public EdgeContext(int u, int v, int c)
    {
        U = u;
        V = v;
        C = c;
    }
}

public class Edge
{
    public long C { get; private set; }
    public long From { get; set; }
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
