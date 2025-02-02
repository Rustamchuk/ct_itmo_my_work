// using System;
//
// class Context
// {
//     public int Rows
//     {
//         get; 
//         private set;
//     }
//
//     public int Columns
//     {
//         get; 
//         private set;
//     }
//
//     public char[,] Map
//     {
//         get;
//         private set;
//     }
//
//     public char[,] Result
//     {
//         get;
//         private set;
//     }
//
//     public void ReadInput()
//     {
//         var dimensions = Console.ReadLine().Split();
//         Rows = Convert.ToInt32(dimensions[0]);
//         Columns = Convert.ToInt32(dimensions[1]);
//
//         Map = new char[Rows, Columns];
//
//         for (var i = 0; i < Rows; i++)
//         {
//             var line = Console.ReadLine();
//             for (var j = 0; j < Columns; j++)
//             {
//                 Map[i, j] = line[j];
//             }
//         }
//
//         Result = (char[,])Map.Clone();
//     }
//
//     public void WriteOutput()
//     {
//         for (var i = 0; i < Rows; i++)
//         {
//             for (var j = 0; j < Columns; j++)
//             {
//                 Console.Write(Result[i, j]);
//             }
//             Console.WriteLine();
//         }
//     }
// }
//
// class Program
// {
//     static void Main()
//     {
//         var context = new Context();
//         context.ReadInput();
//
//         for (var i = 0; i < context.Rows; i++)
//         {
//             for (var j = 0; j < context.Columns; j++)
//             {
//                 if (context.Map[i, j] != '.') continue;
//                 if (Check(i, j, context))
//                 {
//                     context.Result[i, j] = 'X';
//                 }
//             }
//         }
//
//         context.WriteOutput();
//     }
//
//     static bool Check(int i, int j, Context ctx)
//     {
//         return ((i > 0 && ctx.Map[i - 1, j] == '#') ||
//                 (i < ctx.Rows - 1 && ctx.Map[i + 1, j] == '#') ||
//                 (j > 0 && ctx.Map[i, j - 1] == '#') ||
//                 (j < ctx.Columns - 1 && ctx.Map[i, j + 1] == '#'));
//     }
// }

using System;
using System.Linq;

class Контекст
{
    public int КоличествоСтоликов
    {
        get; 
        set;
    }

    public int[] ВместимостьСтоликов
    {
        get; 
        set;
    }

    public int КоличествоКомпаний
    {
        get;
        set;
    }

    public int[] РазмерКомпаний
    {
        get; 
        set;
    }

    public void ВводДанных()
    {
        КоличествоСтоликов = Convert.ToInt32(Console.ReadLine());
        ВместимостьСтоликов = Console.ReadLine().Split().Select(int.Parse).ToArray();
        КоличествоКомпаний = Convert.ToInt32(Console.ReadLine());
        РазмерКомпаний = Console.ReadLine().Split().Select(int.Parse).ToArray();
    }

    public void ВыводРезультата(long результат)
    {
        Console.WriteLine(результат);
    }
}

class Program
{
    static void Main()
    {
        var контекст = new Контекст();
        контекст.ВводДанных();

        // Сортировка массивов
        Array.Sort(контекст.ВместимостьСтоликов);
        Array.Sort(контекст.РазмерКомпаний);

        int i = контекст.КоличествоСтоликов - 1, j = контекст.КоличествоКомпаний - 1;
        long максимальноеКоличествоЛюдей = 0;

        // Алгоритм для нахождения максимального числа людей
        while (i >= 0 && j >= 0)
        {
            if (контекст.ВместимостьСтоликов[i] >= контекст.РазмерКомпаний[j])
            {
                максимальноеКоличествоЛюдей += контекст.РазмерКомпаний[j];
                i--;
                j--;
            }
            else
            {
                j--;
            }
        }

        // Вывод результата
        контекст.ВыводРезультата(максимальноеКоличествоЛюдей);
    }
}