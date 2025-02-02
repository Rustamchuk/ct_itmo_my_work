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
using System.Text.RegularExpressions;

class Program
{
    static void Main()
    {
        // Создание экземпляра класса Контекст
        Контекст контекст = new Контекст();
        
        // Чтение входных данных
        контекст.ЧитатьВходныеДанные();
        
        // Замена всех вхождений "ogo" и его расширений на "***"
        контекст.ОбработатьДанные();
        
        // Вывод результата
        контекст.ВывестиРезультат();
    }
}

class Контекст
{
    public int n;
    public string s;
    public string result;

    public void ЧитатьВходныеДанные()
    {
        n = int.Parse(Console.ReadLine());
        s = Console.ReadLine();
    }

    public void ОбработатьДанные()
    {
        result = Regex.Replace(s, @"o(go)+", "***");
    }

    public void ВывестиРезультат()
    {
        Console.WriteLine(result);
    }
}