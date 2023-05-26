# Тесты к курсу «Парадигмы программирования»

[Условия домашних заданий](https://www.kgeorgiy.info/courses/paradigms/homeworks.html)


## Домашнее задание 1. Обработка ошибок

Модификации
 * *Base*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [TripleParser](java/expression/exceptions/TripleParser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` должны реализовывать интерфейс
        [TripleExpression](java/expression/TripleExpression.java)
    * Нельзя использовать типы `long` и `double`
    * Нельзя использовать методы классов `Math` и `StrictMath`
    * [Исходный код тестов](java/expression/exceptions/ExceptionsTest.java)
        * Первый аргумент: `easy` или `hard`
        * Последующие аргументы: модификации
 * *SetClear* (36, 37)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `set` – установка бита, `2 set 3` равно 10;
        * `clear` – сброс бита, `10 clear 3` равно 2.
 * *Count* (36, 37)
    * Дополнительно реализуйте унарную операцию
      `count` – число установленных битов, `count -5` равно 31.
 * *GcdLcm* (38, 39)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `gcd` – НОД, `2 gcd -3` равно 1;
        * `lcm` – НОК, `2 lcm -3` равно -6.
 * *Reverse* (38, 39)
    * Дополнительно реализуйте унарную операцию
      `reverse` – число с переставленными цифрами, `reverse -12345` равно `-54321`.
 * *PowLog10* (36-39)
    * Дополнительно реализуйте унарные операции:
        * `log10` – логарифм по уснованию 10, `log10 1000` равно 3;
        * `pow10` – 10 в степени, `pow10 4` равно 10000.

