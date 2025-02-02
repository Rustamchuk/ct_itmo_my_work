package main

import (
	"fmt"
)

func main() {
	var t, p, c int
	var g string
	fmt.Scan(&t)
	for ; t > 0; t-- {
		minN, maxN := 15, 30
		fmt.Scan(&p)
		for ; p > 0; p-- {
			fmt.Scan(&g)
			fmt.Scan(&c)
			if g == ">=" {
				minN = maxF(minN, c)
			} else {
				maxN = minF(maxN, c)
			}
			if minN > maxN {
				fmt.Println(-1)
				continue
			}
			fmt.Println(minN)
		}
		fmt.Println()
	}
}

func minF(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func maxF(a, b int) int {
	if a > b {
		return a
	}
	return b
}
