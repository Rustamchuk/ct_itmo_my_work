// Уравнение Point
// Назаров Рустам
// Алгосы лаба 4 сем

using System;

class Point
{
    public int X { get; private set; }
    public int Y { get; private set; }

    public Point(int x, int y)
    {
        X = x;
        Y = y;
    }
}

class Context
{
    public Point P { get; private set; }
    public Point P1 { get; private set; }
    public Point P2 { get; private set; }

    public void ReadInput()
    {
        var input = Console.ReadLine().Split();
        P = new Point(int.Parse(input[0]), int.Parse(input[1]));
        P1 = new Point(int.Parse(input[2]), int.Parse(input[3]));
        P2 = new Point(int.Parse(input[4]), int.Parse(input[5]));
    }

    public void WriteOutput(bool result)
    {
        Console.WriteLine(!result ? "NO" : "YES");
    }
}

class Program
{
    static void Main()
    {
        var context = new Context();
        context.ReadInput();
        var result = IsPointOnSegment(context.P, context.P1, context.P2);
        context.WriteOutput(result);
    }

    static bool IsPointOnSegment(Point p, Point p1, Point p2)
    {
        // Check if the point (p) is on the line segment (p1) to (p2)
        if (Math.Min(p1.X, p2.X) <= p.X && p.X <= Math.Max(p1.X, p2.X) &&
            Math.Min(p1.Y, p2.Y) <= p.Y && p.Y <= Math.Max(p1.Y, p2.Y))
        {
            // Check if the point is collinear with the segment
            return (p2.X - p1.X) * (p.Y - p1.Y) == (p2.Y - p1.Y) * (p.X - p1.X);
        }
        return false;
    }
}


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