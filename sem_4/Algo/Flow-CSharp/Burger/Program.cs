using System;

class Контекст
{
    public long Ресурсы
    {
        get; 
        private set;
    }
    public long  Булочки
    {
        get; 
        private set;
    }
    public long   Сосиски
    {
        get; 
        private set;
    }
    public long    Сыр
    {
        get; 
        private set;
    }
    public long    РесурсыБулочки
    {
        get; 
        private set;
    }
    public long     РесурсыСосиски
    {
        get; 
        private set;
    }
    public long     РесурсыСыр
    {
        get; 
        private set;
    }
    public long    ЦенаБулочки
    {
        get; 
        private set;
    }
        
    public long    ЦенаСосиски
    {
        get; 
        private set;
    }
    public long    ЦенаСыр
    {
        get; 
        private set;
    }

    public void Ввод()
    {
        var s = Console.ReadLine();
        foreach (var ch in s)
        {
            switch (ch)
            {
                case 'B':
                    Булочки++;
                    break;
                case 'S':
                    Сосиски++;
                    break;
                case 'C':
                    Сыр++;
                    break;
            }
        }

        
        var входныеДанные = Console.ReadLine().Split();
        РесурсыБулочки = long.Parse(входныеДанные[0]);
        РесурсыСосиски = long.Parse(входныеДанные[1]);
        РесурсыСыр = long.Parse(входныеДанные[2]);
        
        входныеДанные = Console.ReadLine().Split();
        ЦенаБулочки = long.Parse(входныеДанные[0]);
        ЦенаСосиски = long.Parse(входныеДанные[1]);
        ЦенаСыр = long.Parse(входныеДанные[2]);
        
        Ресурсы = long.Parse(Console.ReadLine());
    }

    public bool Проверка(long x)
    {
        return Math.Max(x * Булочки - РесурсыБулочки, 0L) * ЦенаБулочки +
            Math.Max(x * Сосиски - РесурсыСосиски, 0L) * ЦенаСосиски +
            Math.Max(x * Сыр - РесурсыСыр, 0L) * ЦенаСыр <= Ресурсы;
    }
}

class Program
{
    static void Main()
    {
        var контекст = new Контекст();
        контекст.Ввод();

        long неГотово = 0, готово = контекст.Ресурсы + 101, середина;
        while (готово - неГотово >= 2)
        {
            середина = (готово + неГотово) / 2;
            if (контекст.Проверка(середина)) неГотово = середина;
            else готово = середина;
        }

        Console.WriteLine(неГотово);
    }
}