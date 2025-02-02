package main

import (
	"fmt"
	"math"
)

func main() {
	var n, m int
	fmt.Scan(&n, &m)
	minn := math.MaxInt32
	sum, last, c, cur := 0, 0, 0, 0
	for i := 0; i < n; i++ {
		fmt.Scan(&c)
		if c > m {
			continue
		}
		if minn > c {
			minn = c
		}
		if sum+c <= m {
			sum += c
		}
		l := last
		if l >= c {
			l = maxNum(last-c, last-(absNum(last-c)+1))
		}
		cur = maxNum(m-sum, maxNum(minn-1, l))
		last = cur
	}
	if minn > m {
		last = m
	}
	fmt.Println(minNum(last, m))
}

func maxNum(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func minNum(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func absNum(a int) int {
	if a < 0 {
		return -a
	}
	return a
}
