package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	var t int
	fmt.Scan(&t)
	reader := bufio.NewScanner(os.Stdin)
	for ; t >= 0; t-- {
		var n int
		reader.Scan()
		n, _ = strconv.Atoi(reader.Text())
		roots := make([]int, 0)
		kids := make(map[int][]int)
		texts := make(map[int]string)

		for ; n > 0; n-- {
			var id, old int
			reader.Scan()
			input := strings.SplitN(reader.Text(), " ", 3)
			id, _ = strconv.Atoi(input[0])
			old, _ = strconv.Atoi(input[1])
			text := input[2]

			texts[id] = text
			if old == -1 {
				roots = binSearch(roots, id)
				continue
			}
			if kids[old] == nil {
				kids[old] = make([]int, 0)
			}
			kids[old] = binSearch(kids[old], id)
		}

		for i, root := range roots {
			fmt.Println(texts[root])
			recursiveOutput("|", root, kids, texts)
			if i == len(roots)-1 && t == 1 {
				continue
			}
			fmt.Println()
		}
	}
}

func recursiveOutput(prefix string, root int, kids map[int][]int, texts map[int]string) {
	for i, kid := range kids[root] {
		fmt.Println(prefix)
		fmt.Println(fmt.Sprintf("%s--%s", prefix, texts[kid]))
		if i == len(kids[root])-1 {
			prefix = prefix[:len(prefix)-1] + " "
		}
		recursiveOutput(prefix+"  |", kid, kids, texts)
	}
}

func binSearch(arr []int, num int) []int {
	l, r := -1, len(arr)
	for l < r-1 {
		m := (l + r) / 2
		if arr[m] > num {
			r = m
			continue
		}
		l = m
	}
	if r == len(arr) {
		arr = append(arr, num)
		return arr
	}
	arr = append(arr[:r+1], arr[r:]...)
	arr[r] = num
	return arr
}
