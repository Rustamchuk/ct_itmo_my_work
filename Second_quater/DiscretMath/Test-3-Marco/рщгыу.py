import numpy as np

def generate_matrix(n):
    matrix = np.round(np.random.uniform(-100.0, 100.0, (n, n)), 4)
    print(np.linalg.eig(matrix))
    with open("matrix.txt", "w") as f:
        f.write(str(n) + "\n")
        for row in matrix:
            f.write(" ".join([str(float(x)) for x in row]) + "\n")


generate_matrix(100)

# matrix = np.array([
# [1, 0, 2, 2],
# [-1, 0, 5, 2],
# [2, -2, 0, 0],
# [2, -1, 2, 0]])
# print(np.linalg.eig(matrix))