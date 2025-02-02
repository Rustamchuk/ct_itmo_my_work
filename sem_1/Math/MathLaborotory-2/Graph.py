# Импортируем необходимые библиотеки
import numpy as np
import matplotlib.pyplot as plt
import math


# Данная нам функция
def func(X):
    f_x = np.sin(math.pi / 3 + X)
    return f_x


x = np.arange(-10, 10, 0.1)       # Беру точки x от -10 до 10 с шагом 0.1
f_x = func(x)               # Получаем значения данной функции при разных x

# Значения порядков
n_1 = 3
n_2 = 5

fig = plt.figure(figsize=(100,60))   # Размер графика
# Цвет окантовки
fig.patch.set_facecolor('#DDA0DD')

# Заголовок
ax = fig.add_subplot()
fig.subplots_adjust(top=0.93)
fig.suptitle('Данная функция f(x)', fontsize=150, fontweight='bold')

# Размер координат осей абсцисс и ординат
plt.xticks(fontsize = 120)
plt.yticks(fontsize = 120)
# Разметка на графике
plt.grid(axis = 'both', linewidth = 4)

# Выводим график функции
plt.plot(x, f_x, color='#000080', linewidth=60, label='Функция')

# Выведем Легенду
plt.legend(loc=4, prop={'size': 100})

# Вывод полученного графика
plt.show()

# Многочлен Тейлора n-ого порядка по степеням x
def polinom_Taylor_n_order(X, n):
    Y = 0;
    # Считаем сумму
    for k in range(n + 1):
        Y += math.sin(math.pi / 3 + k * math.pi / 2) * X**k / math.factorial(k)
    return Y


# Библиотека цветов
colors = ["red", 'green', '#FF0000', 'darkred', '#FAC205', '#F97306', "yellow"]
colors_back = ['lime', 'coral', 'tan', 'gold', '#FFA500']

# Беру значения x от -6 до 6 с шагом 0.1,
# Чтобы вблизи наложить график функции на многочлен
x = np.arange(-6, 6, 0.1)
f_x = func(x) # Получаю значения на Ординат

# Беру значения по оси Абсцисс от -6 до 6 с шагом 0.1 для многочлена Тейлора разных порядков
x_n = np.arange(-6, 6, 0.1)

# Циклом перебираю необходимые порядки до n_2
for n in range(1, n_2 + 1):
    fig = plt.figure(figsize=(15, 10))  # Размер графика
    # Цвет окантовки
    fig.patch.set_facecolor(colors_back[n - 1])

    # Размер координат
    plt.xticks(fontsize=20)
    plt.yticks(fontsize=20)

    # Считаю значения для текущего порядка по Ординат
    f_x_n = polinom_Taylor_n_order(x_n, n)

    # Вывожу график функции на текущее окно
    plt.plot(x, f_x, color='#000080', linewidth=10, label='Функция')

    # Вывожу многочлен Тейлора текущего порядка на его окно
    plt.plot(x_n, f_x_n, color=colors[n], linewidth=10, label=f'Многочлен Тейлора порядка {n}')
    fig.subplots_adjust(top=0.93)
    fig.suptitle(f'Многочлен Тейлора порядка {n}', fontsize=20)

    # Разметка
    plt.grid(axis='both', linewidth=4)

    # Легенда
    plt.legend(bbox_to_anchor=(0., -0.2, 1., -0.2), loc='lower left',
               ncol=2, mode="expand", borderaxespad=0., prop={'size': 20})

    # Название осей
    plt.xlabel("x", fontsize=30)
    plt.ylabel("f(x)", fontsize=30)

    # Ограничение для оси Ординат
    plt.ylim([-1.5, 1.5])

    # Вывод полученного графика
    plt.show()


# Задаю значение точки a для f(a)
a = 0.2

# Задаю значения n_1, n_2
n_1 = 3
n_2 = 5

# Вывожу приблеженное значение f(a),
# Заменяя функцию многочленами Тейлора порядока n_1 = 3
print(polinom_Taylor_n_order(a, n_1))

# Вывожу приблеженное значение f(a),
# Заменяя функцию многочленами Тейлора порядокаn_2 = 5
print(polinom_Taylor_n_order(a, n_2))

# Задаю значения для требуемой точности
exactly_1 = 10**(-3)
exactly_2 = 10**(-6)

# Задаю значения n_1, n_2 (Повторно, так как мы в новой части)
n_1 = 3
n_2 = 5

# Точное значение
exactly_func = func(a)

# Приближенные значения
polinom_T_1 = polinom_Taylor_n_order(a, n_1)
polinom_T_2 = polinom_Taylor_n_order(a, n_2)

# Вывожу значения и разность между ними
print(f"Точное значение = {exactly_func}")

print(f"Приближенное значение от n_1 = {polinom_T_1}; "
      f"Отличие с точным значение = {abs(exactly_func - polinom_T_1)}")

print(f"Приближенное значение от n_2 = {polinom_T_2}; "
      f"Отличие с точным значение = {abs(exactly_func - polinom_T_2)}")

# Проверяю достигнута ли требуемая точность для n_1
if (abs(exactly_func - polinom_T_1) < exactly_1):
    print("Требуемая точность_1 (10^-3) для n_1 выполняется", True)
else:
    print("Требуемая точность_1 (10^-6) для n_1 не выполняется", False)

# Проверяю достигнута ли требуемая точность для n_2
if (abs(exactly_func - polinom_T_2) < exactly_2):
    print("Требуемая точность_2 (10^-3) для n_2 выполняется", True)
else:
    print("Требуемая точность_2 (10^-6) для n_2 не выполняется", False)