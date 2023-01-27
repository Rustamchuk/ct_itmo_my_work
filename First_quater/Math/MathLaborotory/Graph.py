# Импортируем необходимые библиотеки
import numpy as np
import matplotlib.pyplot as plt
import math
# Фунция подсчета значений исходной последовательности
def func(X):
    Y = np.arctan((2 + (-1) ** X) ** 0.5) * X / (2 * X + 5)
    return Y
# Фунция подсчета значений подпоследовательности нечетных чисел (нижней)
def func_1(X):
 Y = (math.pi * (X)) / (4 * (2 * X + 5))
 return Y
# Фунция подсчета значений подпоследовательности четных чисел (верхней)
def func_2(X):
 Y = (math.pi * (X)) / (3 * (2 * X + 5))
 return Y
# Первые 100 точек последовательности
n = np.arange(1, 101, 1)
# Нечетные точки последовательности (Для нижней подпоследовательности)
n_1 = np.arange(1, 101, 2)
# Четные точки последовательности (Для верхней подпоследовательности)
n_2 = np.arange(2, 101, 2)
# Получаем значения последовательности
x_n = func(n)
# Получаем значения последовательности
x_n1 = func_1(n_1)
# Получаем значения верхней подпоследовательности
x_n2 = func_2(n_2)
sup = math.pi / 6
# Задаем значение Супремума (и Верхнего предела, так как они равны)
inf = math.pi / 28
# Задаем значение Инфинума
lim_down = math.pi / 8
# Задаем значение Нижнего предела
# Проверим Точную границу
m = 0
x_m = 0
for i in range(100, 501):
    if (func(i) > sup - 0.01):
        m = i
        # Запомним точку, где смогли выйти за границу
        x_m = func(i)
        break
fig = plt.figure(figsize=(60,30))   # Размер графика

# Заголовок
ax = fig.add_subplot()
fig.subplots_adjust(top=0.85)
fig.suptitle('График последовательности и её подпоследовательностей\n' 
             'Супремум, Инфинум, Верхний и нижний пределы\n' 
             'Точка из проверки точной границы', fontsize=60, fontweight='bold')

# Размер координат осей абсцисс и ординат
plt.xticks(fontsize = 50)
plt.yticks(fontsize = 50)

# Чертим последовательность
plt.plot(n, x_n, color='orange', linewidth=7,
         label='ИСХОДНАЯ ПОСЛЕДОВАТЕЛЬНОСТЬ')

# Чертим Верхнюю подпоследовательность
plt.plot(n_2, x_n2, 'ro', markersize=35,
         label='Подпоследовательность четных чисел (Верхняя)')

# Чертим Нижнюю подпоследовательность
plt.plot(n_1, x_n1, 'go', markersize=35,
         label='Подпоследовательность нечетных чисел (Нижняя)')

# Покажем точку, вышедшую за границу И ее точную координату
plt.plot(100, x_m + 0.01, 'bo', markersize=35,
         label='Точка, полученная при проверке точной границы')
plt.text(90, x_m + 0.02, '({}, {})'.format
    (f"m = {m}", f"x_m = {round(x_m + 0.01, 2)}"), fontsize=50)

# Укажем уровень Супремума и Верхнего предела
plt.hlines(y = sup, xmin=0, xmax=100, linewidth=10,
           linestyle='dashed', colors='green',
           label='Супремум и Верхний предел')

# Укажем уровень Нижнего придела
plt.hlines(y = lim_down, xmin=0, xmax=100, linewidth=10,
           linestyle='dashdot', colors='blue',
           label='Нижний предел')

# Укажем уровень Инфинума
plt.hlines(y = inf, xmin=0, xmax=100, linewidth=10,
           linestyle='dashed', colors='purple', label='Инфинум')

# Выведем Легенду
plt.legend(loc=4, prop={'size': 60})

# Вывод полученного графика
plt.show()

# Функция подсчета значений Подпоследовательности
# нечетных чисел (Нижней) точно по решению из Тетради
def func_N_0(X):
    Y = (math.pi * (2 * X - 1))/(4 * (4 * X + 3))
    return Y
n_0 = (5 * math.pi) / (0.01 * 32)
# Задаем значение N_0, считая Эпсилон = 0.01
X_0 = np.arange(n_0, n_0 + 101, 1)
# 100 точек подпоследовательности начиная с N_0
Y_0 = func_N_0(X_0)
# Получим значения
fig = plt.figure(figsize=(30,10))   # Размер графика

# Заголовок
ax = fig.add_subplot()
fig.subplots_adjust(top=0.85)
fig.suptitle('(Нижняя) Подпоследовательность нечетных чисел от N_0',
             fontsize=60, fontweight='bold')

# Размер координат осей
plt.xticks(fontsize = 25)
plt.yticks(fontsize = 25)

# Чертим подпоследовательность
plt.plot(X_0, Y_0, color='green', linestyle='dashdot',
         linewidth=14, label='Подпоследовательность')

# Задаем и чертим её предел
lim_0 = math.pi / 8
plt.hlines(y = lim_0, xmin=0, xmax=n_0 + 100, linewidth=10,
           linestyle='dashdot', colors='blue', label='Предел (нижний)')

# Вывод легенды
plt.legend(loc=4, prop={'size': 30})

# Вывод полученного графика
plt.show()
