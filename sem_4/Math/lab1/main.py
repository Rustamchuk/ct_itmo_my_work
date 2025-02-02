import matplotlib.pyplot as plt
import numpy as np
from scipy.integrate import quad

# Функция для проверки, принадлежит ли точка к Канторову множеству
def is_in_cantor_set(x, n):
    for _ in range(n):
        x *= 3
        if x > 1 and x < 2:  # Если точка попадает в средний удаленный интервал
            return False
        x %= 1  # Оставляем только дробную часть
    return True

# Функция для проверки, что число имеет вид 0,00...0111...
def has_ones_at_end(x, n):
    # Преобразуем число в строку без '0.' в начале и ограничиваем длину до n знаков после запятой
    x_str = format(x, f'.{n}f').split('.')[1]
    # Ищем индекс первой единицы с конца строки
    index_of_one = x_str.rfind('1')
    # Проверяем, что все символы после этой единицы также являются единицами
    return index_of_one != -1 and x_str[index_of_one:] == '1' * (len(x_str) - index_of_one)

# Функция f_n
def f_n(x, n):
    if '31415' in format(x, '.10f'):  # Проверяем наличие последовательности '31415'
        return 15
    elif is_in_cantor_set(x, n) or has_ones_at_end(x, n):
        return 14
    else:
        return 3

# Генерация значений x
x_values = np.linspace(0, 1, 1000)

# Изображение графиков для разных значений n
for n in [10, 1000, 100000]:  # Примеры значений n
    y_values = [f_n(x, n) for x in x_values]
    plt.plot(x_values, y_values, label=f'n={n}')

plt.xlabel('x')
plt.ylabel('f_n(x)')
plt.title('Графики функций f_n')
plt.legend()
plt.show()

x_values = np.linspace(0, 1, 1000)

# Создание фигуры и осей
fig, ax = plt.subplots()
line, = ax.plot([], [], lw=2)

# Установка пределов осей
ax.set_xlim(0, 1)
ax.set_ylim(0, 16)

# Инициализация графика
def init():
    line.set_data([], [])
    return line,

# Функция анимации, которая будет вызываться на каждом шаге
def animate(n):
    y_values = [f_n(x, n) for x in x_values]
    line.set_data(x_values, y_values)
    return line,

# Создание анимации
ani = FuncAnimation(fig, animate, init_func=init, frames=range(1, 10), interval=200, blit=True)

# Показать анимацию
plt.show()

# Если вы хотите сохранить анимацию в файл, раскомментируйте следующую строку
# ani.save('cantor_function_animation.mp4', writer='ffmpeg', fps=5)