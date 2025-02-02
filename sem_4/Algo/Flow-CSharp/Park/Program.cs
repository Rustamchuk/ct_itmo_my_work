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

class Program
{
    static void Main()
    {
        var context = new Контекст();
        context.ЧитатьВвод();

        var parking = new SortedSet<int>();
        var nextSpot = 1;

        foreach (var e in context.События)
        {
            var action = e[0];
            var carNumber = int.Parse(e.Substring(2));

            switch (action)
            {
                case '+':
                {
                    int spot;
                    if (parking.Count <= 0)
                    {
                        spot = nextSpot++;
                    }
                    else
                    {
                        spot = parking.Min;
                        parking.Remove(spot);
                    }

                    context.МашинаНаМесте[carNumber] = spot;
                    Console.WriteLine(spot);
                    break;
                }
                case '-':
                {
                    var spot = context.МашинаНаМесте[carNumber];
                    parking.Add(spot);
                    context.МашинаНаМесте.Remove(carNumber);
                    break;
                }
            }
        }
    }
}

class Контекст
{
    public int КоличествоСобытий
    {
        get; 
        private set;
    }

    public List<string> События
    {
        get;
        private set;
    }

    public Dictionary<int, int> МашинаНаМесте
    {
        get; 
        private set;
    }

    public Контекст()
    {
        События = new List<string>();
        МашинаНаМесте = new Dictionary<int, int>();
    }

    public void ЧитатьВвод()
    {
        КоличествоСобытий = int.Parse(Console.ReadLine());
        for (var i = 0; i < КоличествоСобытий; i++)
        {
            События.Add(Console.ReadLine());
        }
    }
}