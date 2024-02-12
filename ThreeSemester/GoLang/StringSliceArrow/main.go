package main

func main() {
}

func getCharByIndex(str string, idx int) rune {
	return []rune(str)[idx]
}

func getStringBySliceOfIndexes(str string, indexes []int) string {
	syms := []rune(str)
	ans := ""
	for _, ind := range indexes {
		ans += string(syms[ind])
	}
	return ans
}

func addPointers(ptr1, ptr2 *int) *int {
	return nil
}

func isComplexEqual(a, b complex128) bool {
	const EPS = 1e-9
	dif := a - b
	rel := real(dif)
	img := imag(dif)
	if rel < 0 {
		rel *= -1
	}
	if img < 0 {
		img *= -1
	}
	return rel <= EPS && img <= EPS
}

func getRootsOfQuadraticEquation(a, b, c float64) (complex128, complex128) {
	d2 := b*b - 4*a*c
	if d2 < 0 {
		x1 := complex(-b/(2*a), sqrt(-d2)/(2*a))
		x2 := complex(real(x1), -imag(x1))
		return x1, x2
	}
	d2 = sqrt(d2)
	x1 := (-b + d2) / (2 * a)
	x2 := (-b - d2) / (2 * a)
	return complex(x1, 0), complex(x2, 0)
}

func sqrt(a float64) float64 {
	b := a
	for i := 0; i < 1000; i++ {
		b = b - (b*b-a)/(2*b)
	}
	return b
}

func mergeSort(s []int) []int {
	if len(s) == 1 {
		return s
	}
	m := len(s) / 2
	return merge(mergeSort(s[:m]), mergeSort(s[m:]))
}

func merge(l, r []int) []int {
	n, k := len(l), len(r)
	res := make([]int, n+k)
	m, i, j := 0, 0, 0
	for ; i < n && j < k; m++ {
		if l[i] < r[j] {
			res[m] = l[i]
			i++
		} else {
			res[m] = r[j]
			j++
		}
	}
	for ; i < n; i, m = i+1, m+1 {
		res[m] = l[i]
	}
	for ; j < k; j, m = j+1, m+1 {
		res[m] = r[j]
	}
	return res
}

func reverseSliceOne(s []int) {
	n := len(s)
	for i, j := 0, n-1; i < n/2; i, j = i+1, j-1 {
		c := s[i]
		s[i] = s[j]
		s[j] = c
	}
}

func reverseSliceTwo(s []int) []int {
	n := len(s)
	nw := make([]int, n)
	for j, i := 0, n-1; i >= 0; j, i = j+1, i-1 {
		nw[j] = s[i]
	}
	return nw
}

func swapPointers(a, b *int) {
	c := *a
	*a = *b
	*b = c
}

func isSliceEqual(a, b []int) bool {
	n, k := len(a), len(b)
	if n != k {
		return false
	}
	for i := 0; i < n; i++ {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}

func deleteByIndex(s []int, idx int) []int {
	return append(s[:idx], s[idx+1:]...)
}
