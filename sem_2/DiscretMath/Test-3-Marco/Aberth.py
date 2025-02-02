import numpy as np

def aberth(poly, initial_guesses, tolerance=1e-6, max_iterations=100):
    # poly - полином, для которого требуется найти корни
    # initial_guesses - начальные приближения корней полинома
    # tolerance - требуемая точность
    # max_iterations - максимальное число итераций

    n = len(initial_guesses)
    x = initial_guesses

    for k in range(max_iterations):
        Ab = np.zeros((n, n), dtype=np.complex_)
        b = np.zeros(n, dtype=np.complex_)

        for i in range(n):
            Ab[i, i] = -1
            for j in range(n):
                if i != j:
                    Ab[i, j] = 1 / (x[i] - x[j])

            b[i] = poly(x[i]) / np.prod([x[i] - x[j] for j in range(n) if i != j])

        delta = np.linalg.solve(Ab, b)
        x = [x[i] + delta[i] for i in range(n)]

        if max(abs(delta)) < tolerance:
            break

    return x


def poly(x):
    return x**2 + 1


initial_guesses = [1 + 1j, 1 - 1j, -1 + 1j]
roots = aberth(poly, initial_guesses)

print(roots)