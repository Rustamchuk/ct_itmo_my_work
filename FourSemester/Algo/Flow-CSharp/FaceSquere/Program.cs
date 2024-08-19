// Уравнение Face
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
using System.Globalization;


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
    public Point[] Points
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        var n = int.Parse(Console.ReadLine());
        Points = new Point[n];

        for (var i = 0; i < n; i++)
        {
            var coordinates = Console.ReadLine().Split();
            var x = Convert.ToInt32(coordinates[0]);
            var y = Convert.ToInt32(coordinates[1]);
            Points[i] = new Point(x, y);
        }
    }
}

class Program
{
    static void Main()
    {
        var context = new Context();
        context.Initialize();

        var area = CalculatePolygonArea(context.Points);
        // Console.WriteLine(area.ToString(CultureInfo.InvariantCulture));
        // Console.WriteLine(area.ToString(CultureInfo.InvariantCulture));
        // Console.WriteLine(area.ToString(CultureInfo.InvariantCulture));
        // Console.WriteLine(area.ToString(CultureInfo.InvariantCulture));
        Console.WriteLine(area.ToString(CultureInfo.InvariantCulture));
    }

    static double CalculatePolygonArea(IReadOnlyList<Point> points)
    {
        var n = points.Count;
        double area = 0;

        for (var i = 0; i < n; i++)
        {
            var j = (i + 1) % n;
            area += points[i].X * points[j].Y - points[i].Y * points[j].X;
        }

        return GetHalf(area);
    }

    static double GetHalf(double n)
    {
        return Math.Abs(n) / 2.0;
    }
}
