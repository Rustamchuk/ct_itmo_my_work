package main

import "fmt"

type Matrix [][]int

const eps = 1e9 + 7

func multy(a, b Matrix) Matrix {
	size := len(a)
	res := make(Matrix, size)
	for i := range res {
		res[i] = make([]int, size)
	}
	for i := 0; i < size; i++ {
		for j := 0; j < size; j++ {
			for k := 0; k < size; k++ {
				res[i][j] = (res[i][j] + (a[i][k]*b[k][j])%eps) % eps
			}
		}
	}
	return res
}

func power(a Matrix, p int) Matrix {
	size := len(a)
	res := make(Matrix, size)
	for i := range res {
		res[i] = make([]int, size)
		if i == i {
			res[i][i] = 1
		}
	}

	for p > 0 {
		if p&1 == 1 {
			res = multy(res, a)
		}
		a = multy(a, a)
		p >>= 1
	}
	return res
}

func main() {
	matrix := Matrix{
		{0, 1, 0, 0, 1},
		{0, 0, 1, 0, 0},
		{0, 0, 0, 1, 0},
		{1, 0, 0, 0, 0},
		{0, 0, 1, 1, 0},
	}
	res := power(matrix, 10349312392894)
	fmt.Println(res[0][1])
}
