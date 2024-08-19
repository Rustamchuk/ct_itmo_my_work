// Уравнение Distance 2
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
    public Point Point
    {
        get; 
        private set;
    }

    public int A
    {
        get; 
        private set;
    }

    public int B
    {
        get; 
        private set;
    }

    public int C
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        // Чтение входных данных
        string[] input = Console.ReadLine().Split();
        var x = Convert.ToInt32(input[0]);
        var y = Convert.ToInt32(input[1]);
        A = Convert.ToInt32(input[2]);
        B = Convert.ToInt32(input[3]);
        C = Convert.ToInt32(input[4]);

        Point = new Point(x, y);
    }

    public void OutputResult(double distance)
    {
        Console.WriteLine(distance.ToString("F6", CultureInfo.InvariantCulture));
    }
}

class Program
{
    static void Main()
    {
        // Инициализация контекста
        var ctx = new Context();
        ctx.Initialize();

        // Вычисление расстояния от точки до прямой
        var point = ctx.Point;
        var linePoint1 = ctx.A;
        var linePoint2 = ctx.B;

        
        var A = ctx.A;
        var B = ctx.B;
        var C = ctx.C;

        var distance = Math.Abs(A * ctx.Point.X + B * ctx.Point.Y + C) / Math.Sqrt(A * A + B * B);

        // Вывод результата с точностью до 10^-6
        ctx.OutputResult(distance);
    }
}
