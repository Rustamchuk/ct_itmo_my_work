using System;
using System.Linq;

class Program
{
    static void Main()
    {
        int n = int.Parse(Console.ReadLine());
        int[] a = Console.ReadLine().Split().Select(int.Parse).ToArray();
        int[] b = Console.ReadLine().Split().Select(int.Parse).ToArray();

        var matches = new (int a, int b)[n];
        for (int i = 0; i < n; i++)
        {
            matches[i] = (a[i], b[i]);
        }

        // Сортируем по интересности матчей в Хогвартсе
        Array.Sort(matches, (x, y) => x.a.CompareTo(y.a));

        int[] maxB = new int[n];
        maxB[0] = matches[0].b;
        for (int i = 1; i < n; i++)
        {
            maxB[i] = Math.Max(maxB[i - 1], matches[i].b);
        }

        for (int k = 1; k <= n; k++)
        {
            Console.Write(maxB[k - 1] + " ");
        }
    }
}