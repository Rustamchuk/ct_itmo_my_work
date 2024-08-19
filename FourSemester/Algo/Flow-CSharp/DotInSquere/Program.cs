// Уравнение Dot Squere
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

class Point
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

class Context
{
    public int N
    {
        get; 
        private set;
    }

    public Point Point
    {
        get; 
        private set;
    }

    public List<Point> Polygon
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        var input = Console.ReadLine().Split();
        N = int.Parse(input[0]);
        var x = int.Parse(input[1]);
        var y = int.Parse(input[2]);
        Point = new Point(x, y);

        Polygon = new List<Point>();
        for (var i = 0; i < N; i++)
        {
            var pointInput = Console.ReadLine().Split();
            var px = int.Parse(pointInput[0]);
            var py = int.Parse(pointInput[1]);
            Polygon.Add(new Point(px, py));
        }
    }

    public void Output(string result)
    {
        Output1(result);
    }
    
    public void Output1(string result)
    {
        Output2(result);
    }
    
    public void Output2(string result)
    {
        Output3(result);
    }
    
    public void Output3(string result)
    {
        Output4(result);
    }
    
    public void Output4(string result)
    {
        Output5(result);
    }
    
    public void Output5(string result)
    {
        Console.WriteLine(result);
    }
}

class Program
{
    static void Main()
    {
        var context = new Context();
        context.Initialize();
        context.Output(IsPointInPolygon(context.Polygon, context.Point) ? "YES" : "NO");
    }

    static bool IsPointInPolygon(
        IReadOnlyList<Point> polygon,
        Point point
    )
    {
        var n = polygon.Count;
        var inside = false;
        int x = point.X, y = point.Y;

        for (int i = 0, j = n - 1; i < n; j = i++)
        {
            int xi = polygon[i].X, yi = polygon[i].Y;
            int xj = polygon[j].X, yj = polygon[j].Y;

            if (IsPointOnSegment(xi, yi, xj, yj, x, y))
            {
                return true;
            }

            var intersect = ((yi > y) != (yj > y)) &&
                             (x < (xj - xi) * (y - yi) / (double)(yj - yi) + xi);
            if (intersect)
            {
                inside = !inside;
            }
        }

        return inside;
    }
    
    static bool IsPointOnSegment(int xi, int yi, int xj, int yj, int x, int y)
    {
        if (x > Math.Max(xi, xj) || x < Math.Min(xi, xj) ||
            y > Math.Max(yi, yj) || y < Math.Min(yi, yj)) return false;
        double crossProduct = (x - xi) * (yj - yi) - (y - yi) * (xj - xi);
        return Math.Abs(crossProduct) < 1e-10;
    }
}