// Уравнение прямой
// Назаров Рустам
// Алгосы лаба 4 сем

// **Любую прямую на плоскости можно задать уравнением 
//     �
//     �
// +
//     �
//     �
// +
//     �
//     =
// 0
// Ax+By+C=0,** где 
//     �
// A, 
//     �
// B и 
//     �
// C — действительные числа, причём из чисел 
//     �
// A и 
//     �
// B хотя бы одно должно быть отлично от нуля.
//
//     Это уравнение называется общим уравнением прямой. Если коэффициент 
//     �
// A равен нулю, то прямая горизонтальна, а если 
//     �
//     =
// 0
// B=0 — вертикальна.

using System;

public class Context
{
    public double x1 { get; private set; }
    public double x2 { get; private set; }
    public double y1 { get; private set; }
    public double y2 { get; private set; }

    public void Init()
    {
        // Чтение входных данных
        var input = Console.ReadLine().Split();
        x1 = Convert.ToDouble(input[0]);
        y1 = Convert.ToDouble(input[1]);
        x2 = Convert.ToDouble(input[2]);
        y2 = Convert.ToDouble(input[3]);
    }
}

class Program
{
    static void Main()
    {
        var ctx = new Context();
        ctx.Init();

        // Вычисление коэффициентов A, B и C
        var A = ctx.y2 - ctx.y1;
        var B = ctx.x1 - ctx.x2;
        var C = ctx.x2 * ctx.y1 - ctx.x1 * ctx.y2;

        // Приведение уравнения к стандартному виду Ax + By + C = 0
        // C = -C;

        // Вывод результата
        Console.WriteLine($"{A} {B} {C}");
    }
}