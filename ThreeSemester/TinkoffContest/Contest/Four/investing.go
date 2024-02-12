package main

import (
	"fmt"
	"math"
)

var n, k int
var used []int
var edges [][]int
var comps []string
var cmap map[string]int

func main() {
	fmt.Scan(&n, &k)
	comps = make([]string, k)
	cmap = make(map[string]int, k)
	used = make([]int, n+1)
	for i := 0; i < k; i++ {
		fmt.Scan(&comps[i])
		cmap[comps[i]] = i
	}
	edges = make([][]int, n+1)
	var j, c int
	var s string
	for i := 1; i < n+1; i++ {
		edges[i] = make([]int, 2)
	}
	for i := 1; i < n+1; i++ {
		fmt.Scan(&j, &c, &s)
		edges[i][0] = c
		edges[i][1] = cmap[s]
		if j != 0 {
			edges[i] = append(edges[i], j)
			edges[j] = append(edges[j], i)
		}
	}
	cs, cst := dfs(1)
	for i := 0; i < k; i++ {
		if cs[i] == 0 {
			fmt.Println(-1)
			return
		}
	}
	fmt.Println(cst)
}

func dfs(v int) ([]int, int) {
	used[v] = 1
	cst := 0
	out := make([]int, k)
	finCst := math.MaxInt32
	var fin []int
	for i := 2; i < len(edges[v]); i++ {
		if used[edges[v][i]] != 1 {
			comp, cost := dfs(edges[v][i])
			m := 0
			for ; m < len(comp); m++ {
				if comp[m] == 0 {
					break
				}
			}
			if m == len(comp) {
				if cost < finCst {
					finCst = cost
					fin = comp
				}
				continue
			}
			for j, l := range comp {
				out[j] += l
			}
			cst += cost
		}
	}
	if finCst != math.MaxInt32 {
		return fin, finCst
	}
	out[edges[v][1]] += 1
	return out, cst + edges[v][0]
}
