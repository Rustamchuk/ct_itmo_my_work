def f(number, target):
    p = []

    while number >= target:
        a = number // target
        b = number - target * a
        p.append(b)
        number = a
    p.append(number)
    return p[::-1]


n = int(input())
for i in range(2**n):
    