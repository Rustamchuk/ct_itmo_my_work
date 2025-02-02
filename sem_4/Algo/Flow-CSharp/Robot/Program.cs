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
    public int КоличествоСтрок
    {
        get;
        set;
    }

    public int КоличествоСтолбцов
    {
        get;
        set;
    }

    public char[][] Таблица
    {
        get;
        set;
    }

    public int[,] Топливо
    {
        get;
        set;
    }

    public Queue<(int, int)> Очередь
    {
        get; 
        set;
    }

    public void ВводДанных()
    {
        СчитатьРазмеры();
        СчитатьТаблицу();
        ИнициализироватьТопливо();
        ИнициализироватьОчередь();
    }

    private void СчитатьРазмеры()
    {
        var входныеДанные = Console.ReadLine().Split();
        КоличествоСтрок = Convert.ToInt32(входныеДанные[0]);
        КоличествоСтолбцов = Convert.ToInt32(входныеДанные[1]);
    }

    private void СчитатьТаблицу()
    {
        Таблица = new char[КоличествоСтрок][];
        for (var i = 0; i < КоличествоСтрок; i++)
        {
            Таблица[i] = Console.ReadLine().ToCharArray();
        }
    }

    private void ИнициализироватьТопливо()
    {
        Топливо = new int[КоличествоСтрок, КоличествоСтолбцов];
        for (var i = 0; i < КоличествоСтрок; i++)
        {
            for (var j = 0; j < КоличествоСтолбцов; j++)
            {
                Топливо[i, j] = int.MaxValue;
            }
        }
    }

    private void ИнициализироватьОчередь()
    {
        Очередь = new Queue<(int, int)>();
    }

    public void ВыводРезультата()
    {
        Console.WriteLine(Топливо[КоличествоСтрок - 1, КоличествоСтолбцов - 1]);
    }
}

class Program
{
    static void Main()
    {
        var контекст = new Контекст();
        контекст.ВводДанных();
        ИнициализацияТоплива(контекст);
        ОбработкаОчереди(контекст);
        контекст.ВыводРезультата();
    }

    static void ИнициализацияТоплива(Контекст контекст)
    {
        контекст.Топливо[0, 0] = 0;
        контекст.Очередь.Enqueue((0, 0));
    }

    static void ОбработкаОчереди(Контекст контекст)
    {
        int[] СмещениеПоСтрокам = { -1, 1, 0, 0 };
        int[] СмещениеПоСтолбцам = { 0, 0, -1, 1 };
        char[] Направления = { 'N', 'S', 'W', 'E' };

        while (контекст.Очередь.Count > 0)
        {
            var (текущаяСтрока, текущийСтолбец) = контекст.Очередь.Dequeue();
            for (var направление = 0; направление < 4; направление++)
            {
                ОбработкаСоседнейКлетки(контекст, текущаяСтрока, текущийСтолбец, СмещениеПоСтрокам, СмещениеПоСтолбцам, Направления, направление);
            }
        }
    }

    static void ОбработкаСоседнейКлетки(Контекст контекст, int текущаяСтрока, int текущийСтолбец, int[] СмещениеПоСтрокам, int[] СмещениеПоСтолбцам, char[] Направления, int направление)
    {
        var новаяСтрока = текущаяСтрока + СмещениеПоСтрокам[направление];
        var новыйСтолбец = текущийСтолбец + СмещениеПоСтолбцам[направление];
        if (ПроверкаГраниц(контекст, новаяСтрока, новыйСтолбец))
        {
            ОбновлениеТоплива(контекст, текущаяСтрока, текущийСтолбец, новаяСтрока, новыйСтолбец, Направления[направление]);
        }
    }

    static bool ПроверкаГраниц(Контекст контекст, int строка, int столбец)
    {
        return строка >= 0 && строка < контекст.КоличествоСтрок && столбец >= 0 && столбец < контекст.КоличествоСтолбцов;
    }

    static void ОбновлениеТоплива(Контекст контекст, int текущаяСтрока, int текущийСтолбец, int новаяСтрока, int новыйСтолбец, char направление)
    {
        var стоимость = (контекст.Таблица[текущаяСтрока][текущийСтолбец] == направление) ? 0 : 1;
        if (контекст.Топливо[текущаяСтрока, текущийСтолбец] + стоимость >=
            контекст.Топливо[новаяСтрока, новыйСтолбец]) return;
        контекст.Топливо[новаяСтрока, новыйСтолбец] = контекст.Топливо[текущаяСтрока, текущийСтолбец] + стоимость;
        контекст.Очередь.Enqueue((новаяСтрока, новыйСтолбец));
    }
}