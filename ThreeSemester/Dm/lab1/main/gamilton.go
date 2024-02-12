package main

import (
	"fmt"
)

func main() {
	var n int
	fmt.Scan(&n)

	matrix := make([]string, n)

	for i := 0; i < n-1; i++ {
		fmt.Scan(&matrix[i+1])
	}

	queue := make([]int, n)
	for i := 0; i < n; i++ {
		queue[i] = i
	}

	for i := 0; i < n*(n-1); i++ {
		if !check(matrix, queue[0], queue[1]) {
			index := 2
			for !check(matrix, queue[0], queue[index]) || !check(matrix, queue[1], queue[index+1]) {
				index++
			}
			reverse(queue, index)
		}
		queue = append(queue, queue[0])
		queue = queue[1:]
	}

	for _, i := range queue {
		fmt.Print(i+1, " ")
	}
}

func check(matrix []string, i int, j int) bool {
	if i < j {
		i, j = j, i
	}
	return matrix[i][j] == '1'
}

func reverse(queue []int, index int) {
	for k, l := 1, index; k < l; k, l = k+1, l-1 {
		queue[k], queue[l] = queue[l], queue[k]
	}
}
