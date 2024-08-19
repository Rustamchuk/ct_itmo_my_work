using System;

class Point
{
    public double X
    {
        get; 
        private set;
    }

    public double Y
    {
        get; 
        private set;
    }

    public Point(double x, double y)
    {
        X = x;
        Y = y;
    }
}

class Context
{
    public Point P1
    {
        get; 
        private set;
    }

    public Point P2
    {
        get; 
        private set;
    }

    public Point P3
    {
        get; 
        private set;
    }
    
    public Point P4
    {
        get; 
        private set;
    }
    
    public Point P5
    {
        get; 
        private set;
    }
    
    public Point P6
    {
        get; 
        private set;
    }
    
    public Point P7
    {
        get; 
        private set;
    }
    
    public Point P8
    {
        get; 
        private set;
    }

    public void Initialize()
    {
        // Чтение входных данных
        var input = Console.ReadLine().Split();
        var x1 = Convert.ToDouble(input[0]);
        var y1 = Convert.ToDouble(input[1]);
        var x2 = Convert.ToDouble(input[2]);
        var y2 = Convert.ToDouble(input[3]);

        // Инициализация точек
        P1 = new Point(x1, y1);
        P2 = new Point(x2, y2);
        P3 = new Point(0, 0); // P3 не используется, но инициализируем для полноты
        P4 = new Point(1, 6); // P3 не используется, но инициализируем для полноты
        P5 = new Point(3, 1); // P3 не используется, но инициализируем для полноты
        P6 = new Point(2, 0); // P3 не используется, но инициализируем для полноты
        P7 = new Point(5, 3); // P3 не используется, но инициализируем для полноты
        P8 = new Point(4, 9); // P3 не используется, но инициализируем для полноты
    }

    public void Output(double angle)
    {
        // Вывод результата с точностью до пятого знака после запятой
        Console.WriteLine(angle.ToString("F10"));
    }
}

class Program
{
    static void Main()
    {
        // Инициализация контекста и чтение входных данных
        var context = new Context();
        context.Initialize();

        // Получение точек из контекста
        var p1 = context.P1;
        var p2 = context.P2;

        // Вычисление скалярного произведения
        var dotProduct = p1.X * p2.X + p1.Y * p2.Y;

        // Вычисление длин векторов
        var magnitude1 = Math.Sqrt(p1.X * p1.X + p1.Y * p1.Y);
        var magnitude2 = Math.Sqrt(p2.X * p2.X + p2.Y * p2.Y);

        // Вычисление косинуса угла
        var cosTheta = dotProduct / (magnitude1 * magnitude2);

        // Вычисление угла в радианах
        var angle = Math.Acos(cosTheta);

        // Вывод результата с точностью до пятого знака после запятой
        context.Output(angle);
    }
}
