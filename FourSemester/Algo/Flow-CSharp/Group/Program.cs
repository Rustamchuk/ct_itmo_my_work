﻿// // using System;
// // using System.Collections.Generic;
// //
// // // -----------------
// // // Nazarov Rustam
// // // Slime Lesson
// // // 4 sem Algo Flow
// // // -----------------
// //
// // public class Context
// // {
// //     public const long Inf = 1000100;
// //     public int N { get; private set; }
// //     public int M { get; private set; }
// //
// //     public void Init()
// //     {
// //         var input = Console.ReadLine().Split();
// //         N = Convert.ToInt32(input[0]);
// //         M = Convert.ToInt32(input[1]);
// //     }
// //
// //     public EdgeContext InitEdge()
// //     {
// //         var input = Console.ReadLine().Split();
// //         var ctx = new EdgeContext(
// //             Convert.ToInt32(input[0]) - 1,
// //             Convert.ToInt32(input[1]) - 1,
// //             Convert.ToInt32(input[2])
// //         );
// //         return ctx;
// //     }
// //     
// //     public void PrintResult(List<long> ans, long maxFlow)
// //     {
// //         Console.WriteLine($"{ans.Count} {maxFlow}");
// //         foreach (var it in ans)
// //         {
// //             Console.Write($"{it} ");
// //         }
// //     }
// // }
// //
// // public class EdgeContext
// // {
// //     public int U { get; private set; }
// //     public int V { get; private set; }
// //     public int C { get; private set; }
// //
// //     public EdgeContext(int u, int v, int c)
// //     {
// //         U = u;
// //         V = v;
// //         C = c;
// //     }
// // }
// //
// // public class Slice
// // {
// //     static List<long> minCut = new List<long>();
// //     
// //     static void AddEdge(List<Edge> edgesList, List<List<long>> g, List<Tuple<long, long>> edgeNums, EdgeContext data, int i)
// //     {
// //         var e = new Edge(data.C, 0, data.V, edgesList.Count)
// //         {
// //             RevNum = edgesList.Count + 1,
// //             From = data.U,
// //             Index = i
// //         };
// //         edgesList.Add(e);
// //         g[data.U].Add(e.Num);
// //
// //         var w = new Edge(data.C, 0, data.U, edgesList.Count)
// //         {
// //             RevNum = e.Num,
// //             From = data.V,
// //             Index = i
// //         };
// //         edgesList.Add(w);
// //         g[data.V].Add(w.Num);
// //
// //         edgeNums.Add(Tuple.Create((long)data.U, (long)g[data.U].Count - 1));
// //     }
// //     
// //     private static List<List<long>> InitializeGraph(int n)
// //     {
// //         var g = new List<List<long>>(n);
// //         for (var i = 0; i < n; i++)
// //         {
// //             g.Add(new List<long>());
// //         }
// //         return g;
// //     }
// //
// //     public static void Main()
// //     {
// //         var ctx = new Context();
// //         ctx.Init();
// //
// //         var edgeNums = new List<Tuple<long, long>>(ctx.M);
// //         var g = InitializeGraph(ctx.N);
// //
// //         var edgesList = new List<Edge>();
// //         for (var i = 0; i < ctx.M; i++)
// //         {
// //             var data = ctx.InitEdge();
// //             AddEdge(edgesList, g, edgeNums, data, i);
// //         }
// //
// //         const int s = 0;
// //         var t = ctx.N - 1;
// //         var used = new List<bool>(new bool[ctx.N]);
// //
// //         while (Dfs(s, Context.Inf, g, used, t, edgesList) > 0)
// //         {
// //             used = new List<bool>(new bool[ctx.N]);
// //         }
// //
// //         var maxFlow = CalculateMaxFlow(g, edgesList);
// //
// //         used = new List<bool>(new bool[ctx.N]);
// //         FindMinCut(s, g, used, edgesList);
// //
// //         var ans = GetMinCutEdges(edgesList, used);
// //
// //         ctx.PrintResult(ans, maxFlow);
// //     }
// //
// //     static long Dfs(
// //         long v,
// //         long minC,
// //         IReadOnlyList<List<long>> g,
// //         IList<bool> used, long t,
// //         IReadOnlyList<Edge> edgesList
// //     )
// //     {
// //         if (v == t)
// //         {
// //             return minC;
// //         }
// //
// //         used[(int)v] = true;
// //
// //         for (var i = 0; i < g[(int)v].Count; i++)
// //         {
// //             var e = edgesList[(int)g[(int)v][i]];
// //             if (used[(int)e.To] || e.F >= e.C) continue;
// //             var delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
// //             if (delta <= 0) continue;
// //             edgesList[(int)g[(int)v][i]].F += delta;
// //             edgesList[(int)edgesList[(int)g[(int)v][i]].RevNum].F -= delta;
// //             return delta;
// //         }
// //
// //         return 0;
// //     }
// //
// //     static void FindMinCut(long v, IReadOnlyList<List<long>> g, IList<bool> used, IReadOnlyList<Edge> edgesList)
// //     {
// //         if (used[(int)v])
// //         {
// //             return;
// //         }
// //
// //         used[(int)v] = true;
// //
// //         for (var i = 0; i < g[(int)v].Count; i++)
// //         {
// //             var e = edgesList[(int)g[(int)v][i]];
// //
// //             if (e.F == e.C)
// //             {
// //                 minCut.Add(e.Index);
// //             }
// //
// //             if (!used[(int)e.To] && e.F < e.C)
// //             {
// //                 FindMinCut(e.To, g, used, edgesList);
// //             }
// //         }
// //     }
// //     
// //     private static long CalculateMaxFlow(IReadOnlyList<List<long>> g, IReadOnlyList<Edge> edgesList)
// //     {
// //         return g[0].Select(it => edgesList[(int)it]).Select(e1 => Math.Abs(e1.F)).Sum();
// //     }
// //
// //     private static List<long> GetMinCutEdges(IReadOnlyList<Edge> edgesList, IReadOnlyList<bool> used)
// //     {
// //         var ans = new List<long>();
// //         for (var i = 0; i < edgesList.Count; i += 2)
// //         {
// //             if (used[(int)edgesList[i].From] ^ used[(int)edgesList[i].To])
// //             {
// //                 ans.Add(edgesList[i].Index + 1);
// //             }
// //         }
// //         return ans;
// //     }
// // }
// //
// // // public class Program
// // // {
// // //     public static void Main()
// // //     {
// // //         var ctx = new Context();
// // //         ctx.Init();
// // //
// // //         var g = new List<List<long>>(ctx.N);
// // //         for (int i = 0; i < ctx.N; i++) g.Add(new List<long>());
// // //         var edgesList = new List<Edge>();
// // //
// // //         for (int i = 0; i < ctx.M; i++)
// // //         {
// // //             var input = ctx.InitEdge();
// // //
// // //             var e = new Edge(input.C, 0, input.V, edgesList.Count)
// // //             {
// // //                 RevNum = edgesList.Count + 1,
// // //                 From = input.U
// // //             };
// // //             edgesList.Add(e);
// // //             g[input.U].Add(e.Num);
// // //
// // //             var w = new Edge(input.C, 0, input.U, edgesList.Count)
// // //             {
// // //                 RevNum = e.Num,
// // //                 From = input.V
// // //             };
// // //             edgesList.Add(w);
// // //             g[input.V].Add(w.Num);
// // //         }
// // //
// // //         const int s = 0;
// // //         var t = ctx.N - 1;
// // //         var used = new List<bool>(new bool[ctx.N]);
// // //
// // //         while (Dfs(s, Context.Inf, g, used, t, edgesList) > 0)
// // //         {
// // //             used.Clear();
// // //             used.AddRange(new bool[ctx.N]);
// // //         }
// // //
// // //         var maxFlow = g[0].Select(it => edgesList[(int)it]).Select(e1 => Math.Abs(e1.F)).Sum();
// // //
// // //         Console.WriteLine(maxFlow);
// // //
// // //         for (int i = 0; i < edgesList.Count; i += 2)
// // //         {
// // //             Console.WriteLine(edgesList[i].F == 0 ? -edgesList[i + 1].F : edgesList[i].F);
// // //         }
// // //     }
// // //
// // //     static long Dfs(long v, long minC, List<List<long>> g, List<bool> used, long t, List<Edge> edgesList)
// // //     {
// // //         if (v == t)
// // //         {
// // //             return minC;
// // //         }
// // //
// // //         used[(int)v] = true;
// // //
// // //         for (int i = 0; i < g[(int)v].Count; i++)
// // //         {
// // //             Edge e = edgesList[(int)g[(int)v][i]];
// // //             if (!used[(int)e.To] && e.F < e.C)
// // //             {
// // //                 long delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
// // //                 if (delta > 0)
// // //                 {
// // //                     edgesList[(int)g[(int)v][i]].F += delta;
// // //                     edgesList[(int)edgesList[(int)g[(int)v][i]].RevNum].F -= delta;
// // //                     return delta;
// // //                 }
// // //             }
// // //         }
// // //
// // //         return 0;
// // //     }
// // // }
// // //
// // // public class Context
// // // {
// // //     public const long Inf = 1000100;
// // //     public int N { get; private set; }
// // //     public int M { get; private set; }
// // //
// // //     public void Init()
// // //     {
// // //         N = Convert.ToInt32(Console.ReadLine());
// // //         M = Convert.ToInt32(Console.ReadLine());
// // //     }
// // //
// // //     public EdgeContext InitEdge()
// // //     {
// // //         var input = Console.ReadLine().Split();
// // //         var ctx = new EdgeContext(
// // //             Convert.ToInt32(input[0]) - 1,
// // //             Convert.ToInt32(input[1]) - 1,
// // //             Convert.ToInt32(input[2])
// // //         );
// // //         return ctx;
// // //     }
// // // }
// // //
// // // public class EdgeContext
// // // {
// // //     public int U { get; private set; }
// // //     public int V { get; private set; }
// // //     public int C { get; private set; }
// // //
// // //     public EdgeContext(int u, int v, int c)
// // //     {
// // //         U = u;
// // //         V = v;
// // //         C = c;
// // //     }
// // // }
// // //
// // // public class Edge
// // // {
// // //     public long C { get; private set; }
// // //     public long From { get; set; }
// // //     public long To { get; private set; }
// // //     public long Num { get; private set; }
// // //     public long RevNum { get; set; }
// // //     public long F { get; set; }
// // //
// // //     public Edge(long c, long f, long to, long num)
// // //     {
// // //         C = c;
// // //         F = f;
// // //         To = to;
// // //         Num = num;
// // //     }
// // // }
// //
// // public class Edge
// // {
// //     public long C { get; private set; }
// //     public long From { get; set; }
// //     public long Index { get; set; }
// //     public long To { get; private set; }
// //     public long Num { get; private set; }
// //     public long RevNum { get; set; }
// //     public long F { get; set; }
// //
// //     public Edge(long c, long f, long to, long num)
// //     {
// //         C = c;
// //         F = f;
// //         To = to;
// //         Num = num;
// //     }
// // }
//
// using System;
// using System.Collections.Generic;
//
// public class Context
// {
//     public const long Inf = 1000100;
//     public int N { get; private set; }
//     public int M { get; private set; }
//     public int S { get; private set; }
//     public int T { get; private set; }
//
//     public void Init()
//     {
//         var input = Console.ReadLine().Split();
//         N = Convert.ToInt32(input[0]);
//         M = Convert.ToInt32(input[1]);
//         S = Convert.ToInt32(input[2]) - 1;
//         T = Convert.ToInt32(input[3]) - 1;
//     }
//
//     public EdgeContext InitEdge()
//     {
//         var input = Console.ReadLine().Split();
//         var ctx = new EdgeContext(
//             Convert.ToInt32(input[0]) - 1,
//             Convert.ToInt32(input[1]) - 1
//         );
//         return ctx;
//     }
//
//     public void PrintResult(List<long> p1, List<long> p2)
//     {
//         if (p1[^1] != T || p2[^1] != T)
//         {
//             Console.WriteLine("NO");
//             return;
//         }
//         Console.WriteLine("YES");
//
//         PrintPath(p2);
//         PrintPath(p1);
//     }
//
//     private void PrintPath(List<long> path)
//     {
//         Console.WriteLine(string.Join(" ", path.Select(it => it + 1)));
//     }
// }
//
// public class EdgeContext
// {
//     public int U { get; private set; }
//     public int V { get; private set; }
//
//     public EdgeContext(int u, int v)
//     {
//         U = u;
//         V = v;
//     }
// }
//
// public class Program
// {
//     private static bool _pathFound = false;
//     
//     private static List<List<long>> InitializeGraph(int n)
//     {
//         var graph = new List<List<long>>(n);
//         for (var i = 0; i < n; i++)
//         {
//             graph.Add(new List<long>());
//         }
//         return graph;
//     }
//
//     private static List<Edge> InitializeEdges(Context ctx, List<List<long>> graph)
//     {
//         var edgesList = new List<Edge>();
//         for (var i = 0; i < ctx.M; i++)
//         {
//             var edgeInput = ctx.InitEdge();
//             if (edgeInput.U == edgeInput.V) continue;
//
//             var e = new Edge(1, 0, edgeInput.V, edgesList.Count)
//             {
//                 RevNum = edgesList.Count + 1,
//                 From = edgeInput.U,
//                 IsReal = true
//             };
//             edgesList.Add(e);
//             graph[edgeInput.U].Add(e.Num);
//
//             var w = new Edge(0, 0, edgeInput.U, edgesList.Count)
//             {
//                 RevNum = e.Num,
//                 From = edgeInput.V
//             };
//             edgesList.Add(w);
//             graph[edgeInput.V].Add(w.Num);
//         }
//         return edgesList;
//     }
//
//     public static void Main()
//     {
//         var ctx = new Context();
//         ctx.Init();
//
//         var g = InitializeGraph(ctx.N);
//
//         var edgesList = InitializeEdges(ctx, g);
//
//         var used = new List<bool>(new bool[ctx.N]);
//
//         while (Dfs(ctx.S, Context.Inf, g, used, ctx.T, edgesList) > 0)
//         {
//             used = new List<bool>(new bool[ctx.N]);
//         }
//
//         var maxFlow = edgesList.Where(it => it.IsReal && it.From == ctx.S).Sum(it => it.F);
//
//         if (maxFlow < 2)
//         {
//             Console.WriteLine("NO");
//             return;
//         }
//
//         var p1 = new List<long> { ctx.S };
//         var p2 = new List<long> { ctx.S };
//
//         FindPath(ctx.S, ctx.T, g, edgesList, p1);
//         _pathFound = false;
//         FindPath(ctx.S, ctx.T, g, edgesList, p2);
//
//         ctx.PrintResult(p1, p2);
//     }
//
//     static long Dfs(
//         long v,
//         long minC,
//         IReadOnlyList<List<long>> g,
//         IList<bool> used,
//         long t,
//         IReadOnlyList<Edge> edgesList
//     )
//     {
//         if (v == t)
//         {
//             return minC;
//         }
//
//         used[(int)v] = true;
//
//         foreach (var i in g[(int)v])
//         {
//             var e = edgesList[(int)i];
//             if (used[(int)e.To] || e.F >= e.C) continue;
//             var delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
//             if (delta <= 0) continue;
//             edgesList[(int)i].F += delta;
//             edgesList[(int)edgesList[(int)i].RevNum].F -= delta;
//             return delta;
//         }
//
//         return 0;
//     }
//
//     static void FindPath(
//         long v,
//         long t,
//         IReadOnlyList<List<long>> g,
//         IReadOnlyList<Edge> edgesList,
//         ICollection<long> path
//     )
//     {
//         if (v == t)
//         {
//             _pathFound = true;
//             return;
//         }
//
//         foreach (var i in g[(int)v])
//         {
//             var e = edgesList[(int)i];
//             if (!e.IsReal) continue;
//             if (_pathFound || e.F != 1) continue;
//             edgesList[(int)i].F = 0;
//             path.Add(e.To);
//             FindPath(e.To, t, g, edgesList, path);
//         }
//     }
// }
//
// public class Edge
// {
//     public long C { get; private set; }
//     public long From { get; init; }
//     public bool IsReal { get; init; }
//     public long Index { get; set; }
//     public long To { get; private set; }
//     public long Num { get; private set; }
//     public long RevNum { get; init; }
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

using System;
using System.Collections.Generic;
using System.Linq;

public class Context
{
    public const long Inf = 1000100;
    public int N { get; private set; }
    public int M { get; private set; }
    public int T { get; private set; }

    public int S;
    private List<long> _ptScrDotMegaSuperPt;
    public List<List<long>> GGrGraGrap;
    public List<Edge> EdLiLIeD;
    private new Dictionary<char, int> _s2SiSiSi;
    private new Dictionary<int, char> _siSiSiS2S;
    private new Dictionary<int, (int, int)> _computerGamesDota2;
    private new List<List<char>> _deskScoolChair;
    private new List<long> _needMoreDotsGive;

    public void Init()
    {
        N = Convert.ToInt32(Console.ReadLine());
        
        S = 0;
        _ptScrDotMegaSuperPt = new List<long>(new long[N]);
        GGrGraGrap = new List<List<long>>(new List<long>[N + 1].Select(_ => new List<long>()).ToList());
        EdLiLIeD = new List<Edge>();
        _s2SiSiSi = new Dictionary<char, int>
        {
            { 'W', 3 },
            { 'w', 2 },
            { 'l', 1 },
            { 'L', 0 },
            { '.', 0 }
        };
        _siSiSiS2S = new Dictionary<int, char>
        {
            { 3, 'W' },
            { 2, 'w' },
            { 1, 'l' },
            { 0, 'L' }
        };
        _computerGamesDota2 = new Dictionary<int, (int, int)>();
        _deskScoolChair = new List<List<char>>(new List<char>[N].Select(_ => new List<char>(new char[N])).ToList());
    }

    public void InitEdge()
    {
        _needMoreDotsGive = new List<long>(new long[N]);
        var input = Console.ReadLine().Split();
        for (var i = 0; i < N; i++)
        {
            var x = long.Parse(input[i]);
            _needMoreDotsGive[i] = x - _ptScrDotMegaSuperPt[i];
        }
    }

    public void PrintResult()
    {
        foreach (var row in _deskScoolChair)
        {
            foreach (var c in row)
            {
                Console.Write(c);
            }

            Console.WriteLine();
        }
    }

    private void PrintPath(List<long> path)
    {
        Console.WriteLine(string.Join(" ", path.Select(it => it + 1)));
    }
    
    public void ProcessInput()
    {
        for (var i = 0; i < N; i++)
        {
            var str = Console.ReadLine();
            for (var j = 0; j < str.Length; j++)
            {
                var result = str[j];
                _deskScoolChair[i][j] = str[j];

                if (i == j) continue;

                _ptScrDotMegaSuperPt[i] += _s2SiSiSi[result];

                if (j >= i || result != '.') continue;
                AddGameEdge(i, j);
            }
        }
    }

    private void AddGameEdge(int i, int j)
    {
        GGrGraGrap.Add(new List<long>());
        var gameIndex = GGrGraGrap.Count - 1;

        var e = CreateEdge(S, gameIndex, 3);
        var revE = e.RevEdge();

        EdLiLIeD.Add(e);
        EdLiLIeD.Add(revE);

        GGrGraGrap[(int)e.From].Add(e.Num);
        GGrGraGrap[(int)revE.From].Add(revE.Num);

        var opponents = (i + 1, j + 1);
        _computerGamesDota2[gameIndex] = opponents;

        AddOpponentEdge(gameIndex, opponents.Item1);
        AddOpponentEdge(gameIndex, opponents.Item2);
    }

    private void AddOpponentEdge(int gameIndex, int opponent)
    {
        var edge = CreateEdge(gameIndex, opponent, 3);
        var revEdge = edge.RevEdge();

        EdLiLIeD.Add(edge);
        EdLiLIeD.Add(revEdge);
        GGrGraGrap[(int)edge.From].Add(edge.Num);
        GGrGraGrap[(int)revEdge.From].Add(revEdge.Num);
    }

    private Edge CreateEdge(long from, long to, long capacity)
    {
        return new Edge
        {
            From = from,
            To = to,
            C = capacity,
            Num = EdLiLIeD.Count,
            RevNum = EdLiLIeD.Count + 1,
            F = 0
        };
    }

    public void AddFinalEdges()
    {
        var t = GGrGraGrap.Count;
        GGrGraGrap.Add(new List<long>());

        for (var i = 1; i <= N; i++)
        {
            var e = CreateEdge(i, t, _needMoreDotsGive[i - 1]);
            var revE = e.RevEdge();
            EdLiLIeD.Add(e);
            EdLiLIeD.Add(revE);
            GGrGraGrap[(int)e.From].Add(e.Num);
            GGrGraGrap[t].Add(revE.Num);
        }
    }

    public void UpdateTable()
    {
        foreach (var game in _computerGamesDota2)
        {
            var gameIndex = game.Key;
            var edgeNumbers = GGrGraGrap[gameIndex];
            var realEdges = edgeNumbers.Where(i => EdLiLIeD[(int)i].C == 3).ToList();

            var e = EdLiLIeD[(int)realEdges[0]];
            var w = EdLiLIeD[(int)realEdges[1]];
            var opp1 = e.To;
            var opp2 = w.To;

            _deskScoolChair[(int)opp1 - 1][(int)opp2 - 1] = _siSiSiS2S[(int)e.F];
            _deskScoolChair[(int)opp2 - 1][(int)opp1 - 1] = _siSiSiS2S[(int)w.F];
        }
    }
}

public class EdgeContext
{
    public int U { get; private set; }
    public int V { get; private set; }

    public EdgeContext(int u, int v)
    {
        U = u;
        V = v;
    }
}

public class Program
{
    public static void Main()
    {
        var ctx = new Context();
        ctx.Init();
        ctx.ProcessInput();
        ctx.InitEdge();
        ctx.AddFinalEdges();
        
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST
        // LAST

        var used = new List<bool>(new bool[ctx.GGrGraGrap.Count]);
        while (Dfs(ctx.S, Context.Inf, ctx.GGrGraGrap, used, ctx.GGrGraGrap.Count - 1, ctx.EdLiLIeD) > 0)
        {
            used = new List<bool>(new bool[ctx.GGrGraGrap.Count]);
        }

        ctx.UpdateTable();
        ctx.PrintResult();
    }
    
    public static long Dfs(long v, long minC, List<List<long>> g, List<bool> used, long t, List<Edge> edgesList)
    {
        if (v == t)
        {
            return minC;
        }

        used[(int)v] = true;

        foreach (var i in g[(int)v])
        {
            var e = edgesList[(int)i];
            if (used[(int)e.To] || e.F >= e.C) continue;
            var delta = Dfs(e.To, Math.Min(minC, e.C - e.F), g, used, t, edgesList);
            if (delta <= 0) continue;
            edgesList[(int)i].F += delta;
            edgesList[(int)edgesList[(int)i].RevNum].F -= delta;
            return delta;
        }

        return 0;
    }
}

public class Edge
{
    public long C { get; init; }
    public long From { get; init; }
    public bool IsReal { get; set; }
    public long Index { get; set; }
    public long To { get; init; }
    public long Num { get; init; }
    public long RevNum { get; init; }
    public long F { get; set; }

    public Edge RevEdge()
    {
        return new Edge
        {
            F = 0,
            RevNum = Num,
            Num = RevNum,
            C = 0,
            From = To,
            To = From
        };
    }
}