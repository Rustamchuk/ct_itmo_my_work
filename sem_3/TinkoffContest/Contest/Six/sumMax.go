package main

import (
	"fmt"
	"math"
)

type tree struct {
	add     int64
	min     int64
	changed bool
}

func newTree() *tree {
	return &tree{
		0,
		math.MaxInt64,
		false,
	}
}

var trees []tree
var size int

func main() {
	var n, m int
	fmt.Scan(&n, &m)
	create(n)
	input := make([]int64, n)
	for i := 0; i < n; i++ {
		fmt.Scan(&input[i])
	}
	build(input, 0, 0, size)
	for i := 0; i < m; i++ {
		var str string
		fmt.Scan(&str)
		if str == "?" {
			var l, r int
			var k, b int64
			fmt.Scan(&l, &r, &k, &b)
			var maxx int64 = 0
			for ; l <= r; l++ {
				maxx = maxNum(maxx, get(l-1, l, 0, 0, size, k*int64(l)+b))
			}
			fmt.Println(maxx)
		} else {
			var l, r, x int
			fmt.Scan(&l, &r, &x)
			add(l-1, r, 0, 0, size, int64(x))
		}
	}
}

func create(n int) {
	size = 1
	for size < n {
		size *= 2
	}
	trees = make([]tree, size*2-1)
	for i := 0; i < len(trees); i++ {
		trees[i] = *newTree()
	}
}

func build(input []int64, cur, left, right int) {
	if right-left == 1 {
		if left < len(input) {
			trees[cur].min = input[left]
		}
	} else {
		m := (left + right) / 2
		build(input, 2*cur+1, left, m)
		build(input, 2*cur+2, m, right)
		trees[cur].min = minNum(trees[2*cur+1].min, trees[2*cur+2].min)
	}
}

func maxNum(a, b int64) int64 {
	if a > b {
		return a
	}
	return b
}

func minNum(a, b int64) int64 {
	if a < b {
		return a
	}
	return b
}

func push(pos, l, r int) {
	if !trees[pos].changed || r-l == 1 {
		return
	}
	ll, rr := 2*pos+1, 2*pos+2
	trees[ll].add += trees[pos].add
	trees[rr].add += trees[pos].add
	trees[ll].min += trees[pos].add
	trees[rr].min += trees[pos].add
	trees[pos].add = 0
	trees[pos].changed = false
	trees[ll].changed, trees[rr].changed = true, true
}

func add(st, fin, cur, l, r int, val int64) {
	push(cur, l, r)
	if st >= r || fin <= l {
		return
	}
	if st <= l && fin >= r {
		trees[cur].add += val
		trees[cur].changed = true
		trees[cur].min += val
		return
	}
	m := (r + l) / 2
	add(st, fin, cur*2+1, l, m, val)
	add(st, fin, cur*2+2, m, r, val)
	trees[cur].min = minNum(trees[2*cur+1].min, trees[2*cur+2].min)
}

func get(st, fin, cur, l, r int, min int64) int64 {
	push(cur, l, r)
	if st >= r || fin <= l {
		return 0
	}
	if trees[cur].min >= min {
		return min
	}
	if st <= l && fin >= r {
		return trees[cur].min
	}
	m := (r + l) / 2
	return get(st, fin, cur*2+1, l, m, min) + get(st, fin, cur*2+2, m, r, min)
}
