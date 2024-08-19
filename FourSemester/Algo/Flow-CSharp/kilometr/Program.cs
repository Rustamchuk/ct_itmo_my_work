// Уравнение Distance
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
    public double X
    {
        get; 
        private set;
    }

    public double Y
    {
        get; 
        private set;
    }

    public Point(double x, double y)
    {
        X = x;
        Y = y;
    }
}

class Context
{
    public Point Point0
    {
        get; 
        private set;
    }

    public Point Point1
    {
        get; 
        private set;
    }

    public Point Point2
    {
        get; 
        private set;
    }

    public void InitializeFromInput()
    {
        var input = Console.ReadLine().Split();
        Point0 = new Point(double.Parse(input[0], CultureInfo.InvariantCulture), double.Parse(input[1], CultureInfo.InvariantCulture));
        Point1 = new Point(double.Parse(input[2], CultureInfo.InvariantCulture), double.Parse(input[3], CultureInfo.InvariantCulture));
        Point2 = new Point(double.Parse(input[4], CultureInfo.InvariantCulture), double.Parse(input[5], CultureInfo.InvariantCulture));
    }

    public void OutputResult(double distance)
    {
        Console.WriteLine(distance.ToString("F10", CultureInfo.InvariantCulture));
    }
}

class Program
{
    static void Main()
    {
        var context = new Context();
        context.InitializeFromInput();
        
        var distance = DistanceFromPointToSegment(
            context.Point0.X, context.Point0.Y,
            context.Point1.X, context.Point1.Y,
            context.Point2.X, context.Point2.Y
        );
        
        context.OutputResult(distance);
    }

    static double DistanceFromPointToSegment(double x0, double y0, double x1, double y1, double x2, double y2)
    {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0)
        {
            // The segment is a point
            return Distance(x0, y0, x1, y1);
        }

        double t = ((x0 - x1) * dx + (y0 - y1) * dy) / (dx * dx + dy * dy);
        if (t < 0)
        {
            // Closest point is (x1, y1)
            return Distance(x0, y0, x1, y1);
        }
        else if (t > 1)
        {
            // Closest point is (x2, y2)
            return Distance(x0, y0, x2, y2);
        }
        else
        {
            // Closest point is on the segment
            return Distance(x0, y0, x1 + t * dx, y1 + t * dy);
        }
    }

    static double Distance(double x1, double y1, double x2, double y2)
    {
        var dx = x2 - x1;
        var dy = y2 - y1;
        return Math.Sqrt(dx * dx + dy * dy);
    }
}
