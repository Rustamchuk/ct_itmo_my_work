package main

import "fmt"

func main() {
	var n, k, m int
	fmt.Scan(&n, &k, &m)
	arr := make([]int64, n+1)
	add := make([]int64, n+1)
	for i := 1; i <= n; i++ {
		fmt.Scan(&arr[i])
	}
	edges := make([][][]int64, n+1)
	for i := 0; i < k; i++ {
		var a, b int64
		fmt.Scan(&a, &b)
		edges[a] = append(edges[a], []int64{b, 0})
		edges[b] = append(edges[b], []int64{a, 0})
	}
	for i := 0; i < m; i++ {
		var str string
		fmt.Scan(&str)
		if str == "?" {
			var a int
			fmt.Scan(&a)
			for _, edge := range edges[a] {
				if add[edge[0]] != edge[1] {
					arr[a] += add[edge[0]] - edge[1]
					edge[1] = add[edge[0]]
				}
			}
			fmt.Println(arr[a])
		} else {
			var a, b int64
			fmt.Scan(&a, &b)
			add[a] += b
		}
	}
}
