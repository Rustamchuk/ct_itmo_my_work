import numpy as np

# задаем параметры распределения
mean = 11.8827
variance = 56.8397
stddev = np.sqrt(variance)

# задаем допустимое отклонение
eps = 1e-4

# генерируем девять случайных чисел
random_values = np.zeros(9)
for i in range(9):
    while True:
        x = np.random.randint(1, 10)
        random_values[i] = x
        if abs(np.mean(random_values) - mean) < eps and abs(np.var(random_values) - variance) < eps:
            break

# выводим значения
print(random_values)