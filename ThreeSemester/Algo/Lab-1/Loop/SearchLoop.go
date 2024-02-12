package main

import (
	"fmt"
)

var edges [][]int
var out []int

func main() {
	var n, m int
	fmt.Scan(&n, &m)
	n++
	edges = make([][]int, n)

	edges[0] = make([]int, n)
	for i := 1; i < n; i++ {
		edges[i] = make([]int, 0, 80)
	}
	for i := 0; i < m; i++ {
		var out, in int
		fmt.Scan(&out, &in)
		edges[out] = append(edges[out], in)
	}
	for i := 1; i < n; i++ {
		if edges[0][i] == 0 {
			dfs(i)
			if len(out) != 0 {
				fmt.Println("YES")
				for j := len(out) - 1; j >= 0; j-- {
					fmt.Printf("%d ", out[j])
				}
				return
			}
		}
	}
	fmt.Println("NO")
}

func dfs(v int) int {
	edges[0][v] = 2
	for _, u := range edges[v] {
		if edges[0][u] == 2 {
			out = append(out, v)
			return u
		} else if edges[0][u] == 0 {
			res := dfs(u)
			if res != -1 {
				if res == 0 {
					return res
				}
				out = append(out, v)
				if res == v {
					return 0
				}
				return res
			}
		}
	}
	edges[0][v] = 1
	return -1
}
