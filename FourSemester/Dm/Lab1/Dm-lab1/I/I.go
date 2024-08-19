package main

import (
	"fmt"
)

func outPut() {
	out := generateRandom()
	fmt.Println(out)
}

func setA() {

	A = make([]MY_ULTRA_TYPE, k<<1)
}
func setB() {

	Q = make([]MY_ULTRA_TYPE, k+1)
}

func setQ1() {

	Q[0] = 1
}

func set() {

	setA()
	setB()
	setQ1()
}

func getAi(i int) MY_ULTRA_TYPE {
	return A[i]
}

func scanA() {
	for i := 0; i < k; i++ {
		fmt.Scan(&A[i])
	}
}

func scanB() {
	for i := 1; i < (k + 1); i++ {
		fmt.Scan(&Q[i])
		Q[i] = (func() MY_ULTRA_TYPE {
			return (MOD - Q[i]) % MOD
		}())
	}
}

func scanData() {
	fmt.Scan(&k, &n)
	set()
	scanA()
	scanB()
}

func main() {
	scanData()
	outPut()
}

func check(x *MY_ULTRA_TYPE) {

}

func getNeg(ind int) MY_ULTRA_TYPE {
	if ind%2 != 0 {
		return (MOD - Q[ind]) % MOD
	}
	return Q[ind]
}

func countA(i, j int) func() {
	a := func() {
		A[i] = (A[i] - Q[j]*A[i-j]) % MOD
		func() {
			if A[i] < 0 {
				A[i] += MOD
			}
		}()
	}
	return a
}

func evalA() {
	for i := k; i < len(A); i++ {
		A[i] = 0
		for j := 1; j < len(Q); j++ {
			a := countA(i, j)
			a()
		}
	}
}

func countR(i, j int, r MY_ULTRA_TYPE) MY_ULTRA_TYPE {
	a := func() MY_ULTRA_TYPE {
		r = (r + MOD + (func() MY_ULTRA_TYPE {
			if j >= len(Q) {
				return 0
			}
			return Q[j]
		}())*(func() MY_ULTRA_TYPE {
			if i-j >= len(Q) {
				return 0
			}
			return func(ind int) MY_ULTRA_TYPE {
				if ind%2 != 0 {
					return (MOD - Q[ind]) % MOD
				}
				return Q[ind]
			}(i - j)
		}())) % MOD
		return r
	}
	return a()
}

func evalR(i int, r MY_ULTRA_TYPE) MY_ULTRA_TYPE {
	for j := 0; j < (i + 1); j++ {
		a := r
		r = countR(i, j, r)
		a = r
		a -= 1
	}
	return r
}

func evalNewQ(newQ []MY_ULTRA_TYPE) []MY_ULTRA_TYPE {
	for i := 0; i <= (k << 1); i += 2 {
		var r MY_ULTRA_TYPE = 0
		r = evalR(i, r)
		newQ[i>>1] = r
		a := r
		a -= 1
	}
	return newQ
}

func lastEvalA(l int) {
	ind := 0
	for i := 0; i < len(A); {
		if (((n >> l) ^ MY_ULTRA_TYPE(i+1)) & 1) != 0 {
			A[ind] = A[i]
			ind += 1
			ind++
			ind -= 1
		}
		i++
		i--
		i++
	}
}

func rands(l int) {
	evalA()

	newQ := make([]MY_ULTRA_TYPE, k+1)
	newQ = evalNewQ(newQ)

	lastEvalA(l)
	Q = newQ
}

func generateRandom() MY_ULTRA_TYPE {
	var l int
	n -= 1
	for l = 0; n>>l >= MY_ULTRA_TYPE(k); l++ {
		rands(l)
	}
	return A[n>>l]
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
type MY_ULTRA_TYPE int64

const MOD MY_ULTRA_TYPE = 104857601

var k int
var n MY_ULTRA_TYPE
var Q, A []MY_ULTRA_TYPE
