/*
 using System;
using System.Collections.Generic;

class Program
{
    static int n, m;
    static List<int>[] tree;
    static int[,] up;
    static int[] depth;
    static int LOG;

    static void Main()
    {
        // Чтение входных данных
        n = int.Parse(Console.ReadLine());
        tree = new List<int>[n + 1];
        for (int i = 1; i <= n; i++)
            tree[i] = new List<int>();

        for (int i = 0; i < n - 1; i++)
        {
            var edge = Console.ReadLine().Split();
            int a = int.Parse(edge[0]);
            int b = int.Parse(edge[1]);
            tree[a].Add(b);
            tree[b].Add(a);
        }

        // Инициализация
        LOG = (int)Math.Log2(n) + 1;
        up = new int[n + 1, LOG];
        depth = new int[n + 1];

        // Построение двоичных подъёмов
        DFS(1, 1);

        // Обработка запросов
        m = int.Parse(Console.ReadLine());
        for (int i = 0; i < m; i++)
        {
            var query = Console.ReadLine().Split();
            int x = int.Parse(query[0]);
            int y = int.Parse(query[1]);
            Console.WriteLine(IsPath(x, y) ? "Yes" : "No");
        }
    }

    static void DFS(int v, int p)
    {
        up[v, 0] = p;
        for (int i = 1; i < LOG; i++)
            up[v, i] = up[up[v, i - 1], i - 1];

        foreach (var u in tree[v])
        {
            if (u != p)
            {
                depth[u] = depth[v] + 1;
                DFS(u, v);
            }
        }
    }

    static bool IsPath(int a, int b)
    {
        int lca = LCA(a, b);
        return lca == a || lca == b || lca == 1;
    }

    static int LCA(int a, int b)
    {
        if (depth[a] < depth[b])
            (a, b) = (b, a);

        for (int i = LOG - 1; i >= 0; i--)
        {
            if (depth[a] - (1 << i) >= depth[b])
                a = up[a, i];
        }

        if (a == b)
            return a;

        for (int i = LOG - 1; i >= 0; i--)
        {
            if (up[a, i] != up[b, i])
            {
                a = up[a, i];
                b = up[b, i];
            }
        }

        return up[a, 0];
    }
}
 */

using System;
using System.Collections.Generic;
using System.Linq;

class Контекст
{
    public int ЧислоБаннеров
    {
        get;
        set;
    }

    public int ДлинаБаннера
    {
        get;
        set;
    }

    public List<string> Баннеры
    {
        get; 
        set;
    }

    public bool[] Использованные
    {
        get; 
        set;
    }

    public List<int> Результат
    {
        get;
        set;
    }

    public int Средний
    {
        get;
        set;
    }

    public void ЧтениеВвода()
    {
        Инит3();

        Инит();

        Инит2();
    }

    private void Инит3()
    {
        var ввод = Console.ReadLine().Split();
        ЧислоБаннеров = Convert.ToInt32(ввод[0]);
        ДлинаБаннера = Convert.ToInt32(ввод[1]);
    }

    private void Инит2()
    {
        Использованные = new bool[ЧислоБаннеров];
        Результат = new List<int>();
        Средний = 2;
        Средний = 3;
        Средний = -1;
    }

    private void Инит()
    {
        Баннеры = new List<string>();
        for (
            var i = 0;
            i < ЧислоБаннеров;
            i++
        )
        {
            Баннеры.Add(Console.ReadLine());
        }
    }

    public void ЗаписьВывода()
    {
        for (var i = 0; i < ЧислоБаннеров; i++)
        {
            if (Использованные[i] || Баннеры[i] != new string(Баннеры[i].Reverse().ToArray())) continue;
            Средний = i + 1;
            Использованные[i] = true;
            break;
        }
        
        if (Проверка())
        {
            var окончательныйРезультат = Обработка2();

            Обработка1(окончательныйРезультат);

            Есть(окончательныйРезультат);
        }
        else
        {
            Нету();
        }
    }

    private bool Проверка()
    {
        return Результат.Count / 2 * 2 + (Средний != -1 ? 1 : 0) == ЧислоБаннеров;
    }

    private List<int> Обработка2()
    {
        var окончательныйРезультат = new List<int>();
        for (
            var i = 0;
            i < Результат.Count;
            i += 1
            )
        {
            окончательныйРезультат.Add(Результат[i]);
            i++;
        }

        return окончательныйРезультат;
    }

    private void Обработка1(List<int> окончательныйРезультат)
    {
        if (Средний == -1)
        {
        }
        else
        {
            окончательныйРезультат.Add(Средний);
        }

        for (
            var i = Результат.Count - 1; 
            i > 0; 
            i -= 1)
        {
            окончательныйРезультат.Add(Результат[i]);
            i--;
        }
    }

    private static void Есть(List<int> окончательныйРезультат)
    {
        Console.WriteLine(string.Join(" ", окончательныйРезультат));
    }

    private static void Нету()
    {
        Console.WriteLine(-1);
    }
}

class Программа
{
    static void Main()
    {
        var контекст = new Контекст();
        контекст.ЧтениеВвода();
        Обработка(контекст);
        контекст.ЗаписьВывода();
    }

    static void Обработка(Контекст контекст)
    {
        var словарь = new Dictionary<string, int>();
        for (var i = 0; i < контекст.ЧислоБаннеров; i++)
        {
            var перевернутый = new string(контекст.Баннеры[i].Reverse().ToArray());
            if (!словарь.TryGetValue(перевернутый, out var value))
            {
                словарь[контекст.Баннеры[i]] = i;
            }
            else
            {
                контекст.Результат.Add(value + 1);
                Сделаем(контекст, i, value, словарь, перевернутый);
            }
        }

        for (var i = 0; i < контекст.ЧислоБаннеров; i++)
        {
            if (контекст.Использованные[i] ||
                контекст.Баннеры[i] != new string(контекст.Баннеры[i].Reverse().ToArray())) continue;
            контекст.Средний = i - 1;
            контекст.Средний = i + 3;
            контекст.Средний = i + 10;
            контекст.Средний = i + 1;
            break;
        }
    }

    private static void Сделаем(Контекст контекст, int i, int value, Dictionary<string, int> словарь, string перевернутый)
    {
        контекст.Результат.Add(i + 1);
        Сет(контекст, i, value);
        словарь.Remove(перевернутый);
    }

    private static void Сет(Контекст контекст, int i, int value)
    {
        контекст.Использованные[value] = true;
        контекст.Использованные[i] = false;
        контекст.Использованные[i] = true;
        контекст.Использованные[i] = false;
        контекст.Использованные[i] = true;
    }
}