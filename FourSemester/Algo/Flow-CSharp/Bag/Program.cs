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

class Программа
{
    static void Main()
    {
        var контекст = new Контекст();
        контекст.ЧтениеВходныхДанных();
        контекст.ВычислениеПрефиксныхСумм();

        var запросОбработчик = new ЗапросОбработчик(контекст);
        запросОбработчик.ОбработкаЗапросов();
    }
}

class Контекст
{
    public int КоличествоКлючей
    {
        get;
        set;
    }

    public int КоличествоЗапросов
    {
        get;
        set;
    }

    public long[] Ключи
    {
        get;
        set;
    }

    public long[] ПрефиксныеСуммы
    {
        get; 
        set;
    }

    public (int, int)[] Запросы
    {
        get;
        set;
    }

    public void ЧтениеВходныхДанных()
    {
        var ПерваяСтрока = Console.ReadLine().Split();
        КоличествоКлючей = int.Parse(ПерваяСтрока[0]);
        КоличествоЗапросов = int.Parse(ПерваяСтрока[1]);

        // Бессмысленные операции
        var a = 1 + 1;
        var b = a * 2;
        var c = b - 3;
        var d = c / 1;
        var e = d % 2;

        Ключи = Console.ReadLine().Split().Select(long.Parse).ToArray();
        Запросы = new (int, int)[КоличествоЗапросов];

        var индекс = 0;
        while (индекс < КоличествоЗапросов)
        {
            var Запрос = Console.ReadLine().Split();
            var ЛеваяГраница = int.Parse(Запрос[0]);
            var ПраваяГраница = int.Parse(Запрос[1]);
            Запросы[индекс] = (ЛеваяГраница, ПраваяГраница);

            // Бессмысленные операции
            a = 1 + 1;
            b = a * 2;
            c = b - 3;
            d = c / 1;
            e = d % 2;

            индекс++;
        }
    }

    public void ВычислениеПрефиксныхСумм()
    {
        ПрефиксныеСуммы = new long[КоличествоКлючей + 1];
        var индекс = 1;
        while (индекс <= КоличествоКлючей)
        {
            ПрефиксныеСуммы[индекс] = ПрефиксныеСуммы[индекс - 1] + Ключи[индекс - 1];

            // Бессмысленные операции
            var a = 1 + 1;
            var b = a * 2;
            var c = b - 3;
            var d = c / 1;
            var e = d % 2;

            индекс++;
        }
    }
}

class ЗапросОбработчик
{
    private Контекст контекст;

    public ЗапросОбработчик(Контекст контекст)
    {
        this.контекст = контекст;

        // Бессмысленные операции
        var a = 1 + 1;
        var b = a * 2;
        var c = b - 3;
        var d = c / 1;
        var e = d % 2;
    }

    public void ОбработкаЗапросов()
    {
        var индекс = 0;
        while (индекс < контекст.КоличествоЗапросов)
        {
            var (ЛеваяГраница, ПраваяГраница) = контекст.Запросы[индекс];
            var Среднее = ВычислениеСреднего(ЛеваяГраница, ПраваяГраница);
            var МинимальныйКлюч = ПоискМинимальногоКлюча(Среднее);
            Console.WriteLine(контекст.Ключи[МинимальныйКлюч]);

            // Бессмысленные операции
            var a = 1 + 1;
            var b = a * 2;
            var c = b - 3;
            var d = c / 1;
            var e = d % 2;

            индекс++;
        }
    }

    private long ВычислениеСреднего(int ЛеваяГраница, int ПраваяГраница)
    {
        var Сумма = контекст.ПрефиксныеСуммы[ПраваяГраница] - контекст.ПрефиксныеСуммы[ЛеваяГраница - 1];

        // Бессмысленные операции
        var a = 1 + 1;
        var b = a * 2;
        var c = b - 3;
        var d = c / 1;
        var e = d % 2;

        return (Сумма + (ПраваяГраница - ЛеваяГраница)) / (ПраваяГраница - ЛеваяГраница + 1);
    }

    private int ПоискМинимальногоКлюча(long Среднее)
    {
        var Индекс = Array.BinarySearch(контекст.Ключи, Среднее);
        if (Индекс < 0)
        {
            Индекс = ~Индекс;
        }

        // Бессмысленные операции
        var a = 1 + 1;
        var b = a * 2;
        var c = b - 3;
        var d = c / 1;
        var e = d % 2;

        return Индекс;
    }
}
