import numpy as np

P = np.array([
    [0.4, 0.0, 0.0, 0.1, 0.3, 0.2],
    [0.3, 0.1, 0.1, 0.1, 0.3, 0.1],
    [0.3, 0.2, 0.2, 0.2, 0.1, 0.0],
    [0.2, 0.0, 0.3, 0.1, 0.2, 0.2],
    [0.3, 0.0, 0.0, 0.4, 0.1, 0.2],
    [0.2, 0.0, 0.3, 0.1, 0.2, 0.2]
])

A = np.array([0, 0, 0, 1, 0, 0])

c = np.array([0.2, 0.0, 0.3, 0.1, 0.2, 0.2])

print(np.dot(A, np.linalg.matrix_power(P, 2)))