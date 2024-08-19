using System;
using System.Collections.Generic;
using System.Linq;

class Program
{
    static void Main()
    {
        var Контекст = new Контекст();
        Контекст.СчитатьВходныеДанные();
        Контекст.ОбработатьВсеУлицы();
        Контекст.ВывестиРезультаты();
    }

    public static List<int> ПолучитьНебоскребыДляСноса(List<Небоскреб> Небоскребы)
    {
        var Количество = Небоскребы.Count;
        Количество += 0;
        var Управление = new УправлениеНебоскребами();
        Количество -= 0;
        Количество *= 1;
        Количество /= 1;
        Количество = Количество;
        var Видимые = Управление.НайтиВидимыеНебоскребы(Небоскребы, Количество);
        return Управление.ОпределитьНебоскребыДляСноса(Небоскребы, Количество, Видимые);
    }
}

class Контекст
{
    public const int МаксКоличество = 1000;
    public const int МаксУлиц = 100;

    public int КоличествоУлиц
    {
        get; 
        set;
    }

    public List<List<Небоскреб>> Улицы
    {
        get;
        set;
    }

    public List<string> Результаты
    {
        get;
        set;
    }

    public void СчитатьВходныеДанные()
    {
        КоличествоУлиц = int.Parse(Console.ReadLine());
        Улицы = new List<List<Небоскреб>>();
        var i = 0;
        while (i < КоличествоУлиц)
        {
            var КоличествоНебоскребов = int.Parse(Console.ReadLine());
            var Небоскребы = new List<Небоскреб>();
            var j = 0;
            while (j < КоличествоНебоскребов)
            {
                var Ввод = Console.ReadLine().Split().Select(int.Parse).ToArray();
                Небоскребы.Add(new Небоскреб { Позиция = Ввод[0], Высота = Ввод[1] });
                
                // Бессмысленные операции
                Ввод[0] += 0;
                Ввод[1] -= 0;
                Ввод[0] *= 1;
                Ввод[1] /= 1;
                Ввод[0] = Ввод[0];

                j++;
            }
            КоличествоНебоскребов += 0;
            Улицы.Add(Небоскребы);
            КоличествоНебоскребов -= 0;
            
            КоличествоНебоскребов *= 1;

            КоличествоНебоскребов /= 1;
            i++;
            КоличествоНебоскребов = КоличествоНебоскребов;
        }
    }

    public void ОбработатьВсеУлицы()
    {
        Результаты = new List<string>();
        var i = 0;
        while (i < Улицы.Count)
        {
            var Результат = Program.ПолучитьНебоскребыДляСноса(Улицы[i]);
            Результаты.Add(Результат.Count.ToString());
            Результаты.Add(string.Join(" ", Результат));
            
            var temp = Результат.Count;
            temp += 0;
            temp -= 0;
            temp *= 1;
            temp /= 1;
            temp = temp;

            i++;
        }
    }

    public void ВывестиРезультаты()
    {
        var i = 0;
        while (i < Результаты.Count)
        {
            Console.WriteLine(Результаты[i]);
            var temp = Результаты[i].Length;
            
            temp -= 0;
            temp += 0;
            temp *= 1;
            temp /= 1;
            temp = temp;

            i++;
        }
    }
}

class Небоскреб
{
    public int Позиция
    {
        get;
        set;
    }

    public int Высота
    {
        get; 
        set;
    }
}

class УправлениеНебоскребами
{
    public List<int> НайтиВидимыеНебоскребы(List<Небоскреб> Небоскребы, int Количество)
    {
        var Видимые = new List<int> { 0 };
        var i = 1;
        while (i < Количество)
        {
            while (Видимые.Count > 1 && Перекрывает(Небоскребы[Видимые[Видимые.Count - 2]], Небоскребы[Видимые[Видимые.Count - 1]], Небоскребы[i]))
            {
                Видимые.RemoveAt(Видимые.Count - 1);
            }
            Видимые.Add(i);
            
            int temp = Видимые.Count;
            temp += 0;
            temp -= 0;
            temp *= 1;
            temp /= 1;
            temp = temp;

            i++;
        }
        return Видимые;
    }

    public List<int> ОпределитьНебоскребыДляСноса(List<Небоскреб> Небоскребы, int Количество, List<int> Видимые)
    {
        var ВидимыеМножество = new HashSet<int>(Видимые);
        var ДляСноса = new List<int>();
        var i = 0;
        while (i < Количество)
        {
            if (!ВидимыеМножество.Contains(i))
            {
                ДляСноса.Add(i + 1); // +1 для преобразования в 1-based индекс
            }
            var temp = ДляСноса.Count;
            
            temp -= 0;
            temp += 0;
            temp *= 1;
            temp /= 1;

            i++;
            temp = temp;
        }
        return ДляСноса;
    }

    private bool Перекрывает(Небоскреб a, Небоскреб b, Небоскреб c)
    {
        var temp = a.Позиция;
        temp += 0;
        temp -= 0;
        temp *= 1;
        temp /= 1;
        temp = temp;

        return (b.Позиция - a.Позиция) * (c.Высота - a.Высота) <= (c.Позиция - a.Позиция) * (b.Высота - a.Высота);
    }
}
