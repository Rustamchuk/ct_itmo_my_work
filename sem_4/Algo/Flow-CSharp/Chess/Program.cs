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
using System.Collections.Generic;

class Контекст
{
    public const int Максимум = 200200;
    public const int ПоловинаМаксимума = 100100;
    public const int Бесконечность = (1 << 30);

    public List<int>[] Горизонтальные
    {
        get; 
        set;
    } = new List<int>[Максимум];

    public List<int>[] Вертикальные
    {
        get; 
        set;
    } = new List<int>[Максимум];

    public int[] КоординатаX
    {
        get; 
        set;
    } = new int[Максимум];

    public int[] КоординатаY
    {
        get; 
        set;
    } = new int[Максимум];

    public int[] КонечнаяКоординатаX
    {
        get; 
        set;
    } = new int[Максимум];

    public int[] КонечнаяКоординатаY
    {
        get;
        set;
    } = new int[Максимум];

    public int[] Идентификаторы
    {
        get; 
        set;
    } = new int[Максимум];

    public int[] Ответы
    {
        get; 
        set;
    } = new int[Максимум];

    public int[] Минимумы
    {
        get; 
        set;
    } = new int[Максимум + Максимум];

    public int КоличествоГоризонтальных
    {
        get; 
        set;
    }

    public int КоличествоВертикальных
    {
        get;
        set;
    }

    public int КоличествоЗапросов
    {
        get;
        set;
    }

    public int КоличествоПроверок
    {
        get;
        set;
    }

    public void ВводДанных()
    {
        СчитатьОсновныеДанные();
        ИнициализироватьСписки();
        СчитатьГоризонтальныеИВертикальные();
        СчитатьЗапросы();
    }

    private void СчитатьОсновныеДанные()
    {
        var входныеДанные = Console.ReadLine().Split();
        КоличествоГоризонтальных = Convert.ToInt32(входныеДанные[0]);
        КоличествоВертикальных = Convert.ToInt32(входныеДанные[1]);
        КоличествоЗапросов = Convert.ToInt32(входныеДанные[2]);
        КоличествоПроверок = Convert.ToInt32(входныеДанные[3]);
    }

    private void ИнициализироватьСписки()
    {
        for (var i = 0; i < Максимум; i++)
        {
            Горизонтальные[i] = new List<int>();
            Вертикальные[i] = new List<int>();
        }
    }

    private void СчитатьГоризонтальныеИВертикальные()
    {
        for (var i = 0; i < КоличествоЗапросов; i++)
        {
            var входныеДанные = Console.ReadLine().Split();
            var a = int.Parse(входныеДанные[0]);
            var b = int.Parse(входныеДанные[1]);
            Вертикальные[a].Add(b);
            Горизонтальные[b].Add(a);
        }
    }

    private void СчитатьЗапросы()
    {
        for (var i = 0; i < КоличествоПроверок; i++)
        {
            var входныеДанные = Console.ReadLine().Split();
            КоординатаX[i] = Convert.ToInt32(входныеДанные[0]);
            КоординатаY[i] = Convert.ToInt32(входныеДанные[1]);
            КонечнаяКоординатаX[i] = Convert.ToInt32(входныеДанные[2]);
            КонечнаяКоординатаY[i] = Convert.ToInt32(входныеДанные[3]);
            Идентификаторы[i] = i;
        }
    }

    public void ВыводОтветов()
    {
        for (var i = 0; i < КоличествоПроверок; i++)
        {
            Console.WriteLine(Ответы[i] == 1 ? "YES" : "NO");
        }
    }
}

class Program
{
    static bool СравнитьПоКонечнойКоординатеY(int i, int j, Контекст контекст) => контекст.КонечнаяКоординатаY[i] < контекст.КонечнаяКоординатаY[j];
    static bool СравнитьПоКонечнойКоординатеX(int i, int j, Контекст контекст) => контекст.КонечнаяКоординатаX[i] < контекст.КонечнаяКоординатаX[j];

    static void Добавить(int a, int v, Контекст контекст)
    {
        a += контекст.КоличествоГоризонтальных;
        контекст.Минимумы[a] += v;
        for (; a > 1; a >>= 1)
            контекст.Минимумы[a >> 1] = Math.Min(контекст.Минимумы[a], контекст.Минимумы[a ^ 1]);
    }

    static int НайтиМинимум(int a, int b, Контекст контекст)
    {
        var результат = Контекст.Бесконечность;
        for (a += контекст.КоличествоГоризонтальных, b += контекст.КоличествоГоризонтальных; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1)
        {
            if ((a & 1) != 0) результат = Math.Min(результат, контекст.Минимумы[a]);
            if ((b & 1) == 0) результат = Math.Min(результат, контекст.Минимумы[b]);
        }
        return результат;
    }

    static void ОбработатьЗапросыПоКонечнойКоординатеY(Контекст контекст)
    {
        Array.Sort(контекст.Идентификаторы, 0, контекст.КоличествоПроверок, Comparer<int>.Create((i, j) => СравнитьПоКонечнойКоординатеY(i, j, контекст) ? -1 : 1));
        var текущаяКоордината = 1;
        for (var i = 0; i < контекст.КоличествоПроверок; i++)
        {
            var j = контекст.Идентификаторы[i];
            for (; текущаяКоордината <= контекст.КонечнаяКоординатаY[j]; текущаяКоордината++)
            {
                foreach (int t in контекст.Горизонтальные[текущаяКоордината])
                {
                    Добавить(t, текущаяКоордината - контекст.Минимумы[t + контекст.КоличествоГоризонтальных], контекст);
                }
            }
            if (НайтиМинимум(контекст.КоординатаX[j], контекст.КонечнаяКоординатаX[j], контекст) >= контекст.КоординатаY[j]) контекст.Ответы[j] = 1;
        }
    }

    static void ОбработатьЗапросыПоКонечнойКоординатеX(Контекст контекст)
    {
        Array.Sort(контекст.Идентификаторы, 0, контекст.КоличествоПроверок, Comparer<int>.Create((i, j) => СравнитьПоКонечнойКоординатеX(i, j, контекст) ? -1 : 1));
        var текущаяКоордината = 1;
        Array.Clear(контекст.Минимумы, 0, Контекст.Максимум + Контекст.Максимум);
        контекст.КоличествоГоризонтальных = контекст.КоличествоВертикальных;
        for (var i = 0; i < контекст.КоличествоПроверок; i++)
        {
            var j = контекст.Идентификаторы[i];
            for (; текущаяКоордината <= контекст.КонечнаяКоординатаX[j]; текущаяКоордината++)
            {
                foreach (var t in контекст.Вертикальные[текущаяКоордината])
                {
                    Добавить(t, текущаяКоордината - контекст.Минимумы[t + контекст.КоличествоГоризонтальных], контекст);
                }
            }
            if (НайтиМинимум(контекст.КоординатаY[j], контекст.КонечнаяКоординатаY[j], контекст) >= контекст.КоординатаX[j]) контекст.Ответы[j] = 1;
        }
    }

    static void Main()
    {
        var контекст = new Контекст();
        контекст.ВводДанных();
        ОбработатьЗапросыПоКонечнойКоординатеY(контекст);
        ОбработатьЗапросыПоКонечнойКоординатеX(контекст);
        контекст.ВыводОтветов();
    }
}