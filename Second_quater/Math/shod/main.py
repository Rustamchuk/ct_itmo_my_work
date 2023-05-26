from sympy import *

def raabe_test(sequence_function, n):
    n1 = symbols('n1', integer=True)
    limit_diff = Limit(n1 * (sequence_function(n1) / sequence_function(n1 + 1) - 1), n1, oo)
    limit_value = limit_diff.doit()

    if (limit_value - 1).is_positive:
        return "Сходится"
    elif (limit_value - 1).is_negative:
        return "Расходится"
    else:
        return "Тест не является окончательным"


def gauss_test(sequence_function, n):
    n1 = symbols('n1', integer=True)
    limit_ratio = Limit(sequence_function(n1) / sequence_function(n1 + 1), n1, oo)
    limit_value = limit_ratio.doit()

    if (limit_value - n1).is_zero:
        return "Сходится"
    else:
        return "Тест не является окончательным"


# Полный тест Лейбница
def leibniz_test_full(sequence_function, n):
    a_n = sequence_function(n)
    a_n_abs = Abs(a_n)

    # Условие 1: последовательность a_n_abs монотонно убывает
    a_n_abs_diff = diff(a_n_abs, n)
    a_n_abs_decreasing = simplify(a_n_abs_diff).is_negative

    # Условие 2: предел a_n стремится к нулю
    a_n_limit = Limit(a_n, n, oo).is_zero

    if a_n_abs_decreasing and a_n_limit:
        return "Сходится"
    else:
        return "Тест не является окончательным"

def leibniz_test(sequence_function, n):
    n1 = symbols('n1', integer=True)
    limit_abs = Limit(abs(sequence_function(n1)), n1, oo)
    limit_value = limit_abs.doit()

    if limit_value.is_zero:
        return "Сходится"
    else:
        return "Тест не является окончательным"


# def leibniz_test(sequence_function, n):
#     a_n = abs(sequence_function(n))
#
#     if a_n.is_monotonic_decreasing and Limit(a_n, n, oo).is_zero:
#         return "Сходится"
#     else:
#         return "Тест не является окончательным"

# def leibniz_test(sequence_function, n):
#     a_n = sequence_function(n)
#     da_n = diff(a_n, n)
#     simplified_da_n = simplify(da_n)
#
#     if simplified_da_n.is_negative and Limit(a_n, n, oo).is_zero:
#         return "Сходится"
#     else:
#         return "Тест не является окончательным"


def abel_test(sequence_function, n):
    # Проверка на абсолютную сходимость
    absolute_sum = Sum(abs(sequence_function(n)), (n, 1, oo))
    if absolute_sum.is_absolutely_convergent():
        return "Сходится (абсолютно сходится)"

    # Проверка на сходимость производной ряда
    n1 = symbols('n1', integer=True)
    limit_diff = Limit(n1 * (sequence_function(n1) - sequence_function(n1 + 1)), n1, oo)
    limit_value = limit_diff.doit()

    if limit_value.is_finite:
        return "Сходится (сходится производная ряда)"
    else:
        return "Тест не является окончательным"

# Критерий Коши (интегральный критерий)
def cauchy_integral_test(f, n):
    integral_result = Integral(f(x), (x, 1, oo)).doit()

    if integral_result.is_finite:
        return "Сходится"
    else:
        return "Тест не является окончательным"


# Радикальный критерий Коши (корневой критерий)
def cauchy_radical_test(sequence_function, n):
    limit_value = Limit(Pow(abs(sequence_function(n)), 1 / n), n, oo).doit()

    if limit_value.is_positive and (limit_value - 1).is_negative:
        return "Сходится"
    elif (limit_value - 1).is_positive:
        return "Расходится"
    else:
        return "Тест не является окончательным"


def abel_test_full(sequence_function, n):
    a_n = sequence_function(n)
    a_n_abs = Abs(a_n)
    partial_sum = Sum(a_n, (n, 1, n))
    limit_partial_sum = Limit(partial_sum, n, oo)

    # Условие 1: последовательность a_n_abs монотонно убывает
    a_n_abs_diff = diff(a_n_abs, n)
    a_n_abs_decreasing = simplify(a_n_abs_diff).is_negative

    # Условие 2: произведение частичных сумм ряда и ограниченной монотонной последовательности сходится
    b_n = 1  # Пример ограниченной монотонной последовательности
    product_convergence = Limit(partial_sum * b_n, n, oo)

    if a_n_abs_decreasing and limit_partial_sum.is_bounded and product_convergence.is_bounded:
        return "Сходится"
    else:
        return "Тест не является окончательным"



n, x = symbols('n x', integer=True, real=True)
a = Function('a')(n)

# Пример: ряд 1/n**2
#sequence_function = lambda n: cos(n**2)
#sequence_function = lambda n: ((cos(n))**2 * (-1)**n) / (n * (ln(n + 1))**2)
# sequence_function = lambda n: (cos(n) * (-1)**n) / (n * ln(n + 1))
# sequence_function = lambda n: (-1)**n * (1/n)
# sequence_function = lambda n: abs(sin(n) / sqrt(n))
# sequence_function = lambda n: abs((-1)**n / sqrt(n))
# sequence_function = lambda n: ((n - 1) / n)**n
sequence_function = lambda n: abs(cos(n) / ln(n))
# sequence_function = lambda n: (-1)**n * cos((3 * n) / (5 * n + 2))


# Проверка критерием Раабе
print("Критерий Раабе:", raabe_test(sequence_function, n))

# Проверка признаком Гаусса
print("Признак Гаусса:", gauss_test(sequence_function, n))

# Проверка признаком Лейбница
print("Признак Лейбница:", leibniz_test(sequence_function, n))

# Проверка признаком Лейбница
print("Полный Признак Лейбница:", leibniz_test(sequence_function, n))


# Проверка критерием Абеля
print("Полный Критерий Абеля:", abel_test_full(sequence_function, n))


print("Интегральный критерий Коши:", cauchy_integral_test(sequence_function, n))

print("Радикальный критерий Коши:", cauchy_radical_test(sequence_function, n))