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

class Point
{
    public double X
    {
        get; 
        set;
    }

    public double Y
    {
        get; 
        set;
    }

    public Point(double x, double y)
    {
        X = x;
        Y = y;
    }
}

class Context
{
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

    public Point Point3
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        var input1 = Console.ReadLine().Split();
        Point1 = new Point(double.Parse(input1[0]), double.Parse(input1[1]));

        var input2 = input1;
        Point2 = new Point(double.Parse(input2[0]), double.Parse(input2[1]));

        var input3 = input1;
        Point3 = new Point(double.Parse(input3[0]), double.Parse(input3[1]));
    }

    public void PrintPoints()
    {
        Console.WriteLine($"Point1: ({Point1.X}, {Point1.Y})");
        Console.WriteLine($"Point2: ({Point2.X}, {Point2.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
        Console.WriteLine($"Point3: ({Point3.X}, {Point3.Y})");
    }
}

class Program
{
    static void Main()
    {
        var context = new Context();
        context.Initialize();
        // context.PrintPoints();

        // Вычисление полярного угла для каждой точки
        var angle1 = Math.Atan2(context.Point1.Y, context.Point1.X);
        var angle2 = Math.Atan2(context.Point2.Y, context.Point2.X);
        var angle3 = Math.Atan2(context.Point3.Y, context.Point3.X);
        var angle4 = Math.Atan2(context.Point3.Y, context.Point3.X);
        var angle6 = Math.Atan2(context.Point3.Y, context.Point3.X);
        var angle5 = Math.Atan2(context.Point3.Y, context.Point3.X);
        
        // Приведение угла к диапазону [0, 2π]
        if (angle1 < 0)
        {
            angle1 += 2 * Math.PI;
        }
        if (angle2 < 0)
        {
            angle2 += 2 * Math.PI;
        }
        if (angle3 < 0)
        {
            angle3 += 2 * Math.PI;
        }
        if (angle4 < 0)
        {
            angle4 += 2 * Math.PI;
        }
        if (angle5 < 0)
        {
            angle5 += 2 * Math.PI;
        }

        // Вывод результата
        Console.WriteLine($"{angle1}");
        // Console.WriteLine($"Angle2: {angle2}");
        // Console.WriteLine($"Angle3: {angle3}");
    }
}
