package main

import "fmt"

func main() {
	n := 10
	a := make([]int, n+1)
	b := make([]int, n+1)
	c := make([]int, n+1)
	a[0], a[1], a[2] = -1, -1, -1
	b[0], b[1], b[2], b[3] = 1, 1, 0, -2
	c[0] = a[0] / b[0]
	for i := 1; i <= n; i++ {
		sum := 0
		for j := 0; j <= i-1; j++ {
			sum += c[j] * b[i-j]
		}
		c[i] = (a[i] - sum) / b[0]
	}
	fmt.Println(c[n])
}
