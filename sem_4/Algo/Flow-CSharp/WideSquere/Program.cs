// Уравнение Wide 
// Назаров Рустам
// Алгосы лаба 4 сем


// using System;
//
// public class Context
// {
//     public double x1 { get; private set; }
//     public double x2 { get; private set; }
//     public double y1 { get; private set; }

//     public double y2 { get; private set; }
//
//     public void Init()
//     {
//         // Чтение входных данных
//         var input = Console.ReadLine().Split();
//         x1 = Convert.ToDouble(input[0]);
//         y1 = Convert.ToDouble(input[1]);
//         x2 = Convert.ToDouble(input[2]);
//         y2 = Convert.ToDouble(input[3]);
//     }
// }
//
// class Program
// {
//     static void Main()
//     {
//         var ctx = new Context();
//         ctx.Init();
//
//         // Вычисление коэффициентов A, B и C
//         var A = ctx.y2 - ctx.y1;
//         var B = ctx.x1 - ctx.x2;
//         var C = ctx.x2 * ctx.y1 - ctx.x1 * ctx.y2;
//
//         // Приведение уравнения к стандартному виду Ax + By + C = 0
//         // C = -C;
//
//         // Вывод результата
//         Console.WriteLine($"{A} {B} {C}");
//     }
// }

using System;
using System.Collections.Generic;

public class Context
{
    public List<Point> Points
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        var n = int.Parse(Console.ReadLine());
        Points = new List<Point>();

        for (var i = 0; i < n; i++)
        {
            var input = Console.ReadLine().Split();
            var x = int.Parse(input[0]);
            var y = int.Parse(input[1]);
            Points.Add(new Point(x, y));
        }
    }

    public void Output(string result)
    {
        Console.WriteLine(result);
    }
}

class Program
{
    static void Main()
    {
        var ctx = new Context();
        ctx.Initialize();

        ctx.Output(IsConvexPolygon(ctx.Points) ? "YES" : "NO");
    }

    public static bool IsConvexPolygon(List<Point> points)
    {
        var n = points.Count;
        var gotNegative = false;
        var gotPositive = false;

        for (var i = 0; i < n; i++)
        {
            var crossProduct = CalculateCrossProduct(points, i, n);

            switch (crossProduct)
            {
                case < 0:
                    gotNegative = true;
                    break;
                case > 0:
                    gotPositive = true;
                    break;
            }

            if (gotNegative && gotPositive)
            {
                return false;
            }
        }

        return true;
    }

    private static int CalculateCrossProduct(IReadOnlyList<Point> points, int i, int n)
    {
        var dx1 = points[(i + 2) % n].X - points[(i + 1) % n].X;
        var dy1 = points[(i + 2) % n].Y - points[(i + 1) % n].Y;
        var dx2 = points[i].X - points[(i + 1) % n].X;
        var dy2 = points[i].Y - points[(i + 1) % n].Y;
        return dx1 * dy2 - dy1 * dx2;
    }
}

public class Point
{
    public int X
    {
        get;
        private set;
    }

    public int Y
    {
        get;
        private set;
    }

    public Point(int x, int y)
    {
        X = x;
        Y = y;
    }
}
