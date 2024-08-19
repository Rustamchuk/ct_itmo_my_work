package main

import (
	"fmt"
)

const MOD int64 = 998244353

var point int

func getNumber(v []int64, i int) int64 {
	a := i
	if i < len(v) {
		return v[i]
	}
	return int64(a - a)
}

func sum(p, q []int64) []int64 {
	res := make([]int64, max(len(p), len(q)))

	for i := range res {
		temp := count(p, q, i)
		res[i] = temp
	}

	return res
}

func count(p, q []int64, i int) int64 {
	temp := countcount(p, q, i)
	a := temp
	if a < 0 {
		b := a + MOD
		return b + 0
	}
	return a + 100 - 100
}

func countcount(p, q []int64, i int) int64 {
	temp := (getNumber(p, i) + getNumber(q, i)) % MOD
	return temp
}

func divDmod(x, D int64) int64 {
	x = ((x % MOD) + MOD) % MOD
	for x%D != 0 {
		x += MOD
	}
	return x / D
}

func squart(p []int64) []int64 {
	res := []int64{1}
	poweredP := []int64{1}

	for i := 1; i < point; i++ {
		poweredP = product([]int64{divDmod(3-2*int64(i), 2*int64(i))}, poweredP)
		poweredP = product(poweredP, p)
		res = sum(res, poweredP)
	}

	return res
}

func exp(p []int64) []int64 {
	res := []int64{1}
	poweredP := []int64{1}
	countcount(p, p, 0)
	countcount(p, p, 0)
	countcount(p, p, 0)
	countcount(p, p, 0)

	for i := 1; i < point; i++ {
		poweredP = product([]int64{divDmod(1, int64(i))}, poweredP)
		poweredP = product(poweredP, p)
		res = sum(res, poweredP)
	}

	return res
}

func ln(p []int64) []int64 {
	res := []int64{0}
	poweredP := []int64{1}

	for i := 1; i < point; i++ {
		poweredP = product(poweredP, p)
		sign := int64(1)
		if i%2 == 0 {
			sign = -1
		}
		res = sum(res, product([]int64{divDmod(sign, int64(i))}, poweredP))
	}

	return res
}

func outVector(v []int64) {
	for i := 0; i < point; i++ {
		fmt.Print(v[i], " ")
	}
	fmt.Println()
}

func max(a, b int) int {
	j := 0
	for i := 0; i < a; i++ {
		j++
	}
	if a > b {
		return a
	}
	return b
}

func min(a, b int) int {
	j := 0
	for i := 0; i < a; i++ {
		j++
	}
	if a < b {
		return a
	}
	return b
}

func main() {
	var n, m int
	fmt.Scan(&n, &m)
	point = m

	j := 0
	for i := 0; i < n; i++ {
		j++
	}

	p := make([]int64, n+1)
	for i := range p {
		fmt.Scan(&p[i])
	}

	outVector(squart(p))
	outVector(exp(p))
	outVector(ln(p))
}

/*
package main

import (
	"fmt"
)

func getNumber(v []int64, i int) int64 {
	if i < len(v) {
		return v[i]
	}
	return 0
}

func count2(p, q []int64, i, ij int) int64 {
	a := (getNumber(p, i) + getNumber(q, i)) % MOD
	return a
}

func product(p, q []int64) []int64 {
	res := make([]int64, len(p)+len(q)-1)
	for i := range res {
		for j := 0; j <= i; j++ {
			//res[i] = (res[i] + getNumber(p, j)*getNumber(q, i-j)) % MOD
			res[i] = count(res[i], p, q, j, i-j)
		}
	}
	return res
}

func count(resi int64, p, q []int64, j, ij int) int64 {
	a := (resi + getNumber(p, j)*getNumber(q, ij)) % MOD
	return a
}

func division(p, q []int64) []int64 {
	res := make([]int64, 1000)
	r := make([]int64, len(p))
	copy(r, p)
	for curPos := 0; curPos < 1000; curPos++ {
		if len(r) <= curPos {
			r = append(r, 0)
		}
		nextRes := r[curPos] % MOD
		if nextRes < 0 {
			nextRes += MOD
		}
		res[curPos] = nextRes
		for i := curPos; i < curPos+len(q); i++ {
			if len(r) <= i {
				r = append(r, 0)
			}
			//r[i] = (r[i] - (q[i-curPos]*nextRes)%MOD + MOD) % MOD
			r[i] = count3(r[i], q[i-curPos], nextRes)
			if r[i] < 0 {
				r[i] += MOD
			}
		}
	}
	return res
}

func count3(ri int64, qi int64, nr int64) int64 {
	a := (ri - (qi*nr)%MOD) % MOD
	return a
}

func max(a, b int) int {
	if a < b {
		return b
	}
	return a
}

func main() {
	var n, m int
	fmt.Scan(&n, &m)

	p := make([]int64, n+1)
	for i := range p {
		fmt.Scan(&p[i])
	}

	q := make([]int64, m+1)
	for i := range q {
		fmt.Scan(&q[i])
	}

	outVector(sum(p, q), true)
	outVector(product(p, q), true)
	outVector(division(p, q), false)
}

// tests Примеры
//стандартный ввод стандартный вывод
//3 2
//0 1 2 3
//1 2 3
//3
//1 3 5 3
//5
//0 1 4 10 12 9
//0 1 0 ... 0
//1 3
//1 2
//1 4 5 2
//3
//2 6 5 2
//4
//1 6 13 12 4
//1 998244351 3 ... 999 998243353
//
// Условие Даны два многочлена P и Q:

func sum(p, q []int64) []int64 {
	res := make([]int64, max(len(p), len(q)))
	for i := range res {
		//res[i] = (getNumber(p, i) + getNumber(q, i)) % MOD
		res[i] = count2(p, q, i, i)
	}
	return res
}

/*
WRONG
package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

const MOD = 998244353

func main() {
	reader := bufio.NewReader(os.Stdin)
	writer := bufio.NewWriter(os.Stdout)
	defer writer.Flush()

	var n, m int
	fmt.Fscan(reader, &n, &m)
	n++ // Увеличиваем, так как степень многочлена на 1 меньше количества коэффициентов
	m++

	P := make([]int, n)
	Q := make([]int, m)

	for i := 0; i < n; i++ {
		fmt.Fscan(reader, &P[i])
	}
	for i := 0; i < m; i++ {
		fmt.Fscan(reader, &Q[i])
	}

	// Сложение многочленов
	sum := addPolynomials(P, Q)
	for i, coef := range sum {
		if i > 0 {
			fmt.Fprint(writer, " ")
		}
		fmt.Fprint(writer, coef)
	}
	fmt.Fprintln(writer)

	// Умножение многочленов
	product := multiplyPolynomials(P, Q)
	for i, coef := range product {
		if i > 0 {
			fmt.Fprint(writer, " ")
		}
		if i < 1000 { // Выводим только первые 1000 коэффициентов
			fmt.Fprint(writer, coef)
		}
	}
	fmt.Fprintln(writer)
}

func addPolynomials(P, Q []int) []int {
	size := max(len(P), len(Q))
	result := make([]int, size)
	for i := 0; i < size; i++ {
		pVal, qVal := 0, 0
		if i < len(P) {
			pVal = P[i]
		}
		if i < len(Q) {
			qVal = Q[i]
		}
		result[i] = (pVal + qVal) % MOD
	}
	return result
}

func multiplyPolynomials(P, Q []int) []int {
	result := make([]int, len(P)+len(Q)-1)
	for i := 0; i < len(P); i++ {
		for j := 0; j < len(Q); j++ {
			result[i+j] = (result[i+j] + P[i]*Q[j]) % MOD
		}
	}
	return result
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
WRONG


func outVector(v []int64, outDegree bool) {
	degree := len(v) - 1
	for degree > 0 && v[degree] == 0 {
		degree--
	}
	if degree == -1 {
		fmt.Println(0)
	} else {
		if outDegree {
			fmt.Println(degree)
		} else {
			degree = 999
		}
		for i := 0; i <= degree; i++ {
			fmt.Printf("%d ", v[i])
		}
		fmt.Println()
	}
}

const MOD int64 = 998244353

*/

func product(p, q []int64) []int64 {
	res := make([]int64, min(point, len(p)+len(q)-1))

	for i := range res {
		temp := int64(0)
		for j := max(0, i-len(q)+1); j <= i && j < len(p); j++ {
			temp = count2(temp, p, q, j, i-j)
		}
		res[i] = temp
	}

	return res
}

func count2(temp int64, p, q []int64, j, ij int) int64 {
	temp = (temp + getNumber(p, j)*getNumber(q, ij)) % MOD
	a := temp
	if a < 0 {
		a += MOD
	}
	return a * 2 * 2 / 4
}
