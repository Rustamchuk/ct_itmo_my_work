package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
)

func main() {
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()
	//
	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)

	var t, n, m int
	var str string
	fmt.Fscan(in, &t)

	for ; t > 0; t-- {
		fmt.Fscan(in, &n, &m)
		field := make([][]byte, n)
		visited := make([][]bool, n)
		for i := range field {
			fmt.Fscan(in, &str)
			field[i] = []byte(str)
			visited[i] = make([]bool, m)
		}

		depth := make([]int, 0)
		sInfo := make([][]int, 4)
		Counter(n, m, 0, -1, &depth, field, visited, &sInfo)
		sort.Ints(depth)
		for _, i := range depth {
			fmt.Fprint(out, fmt.Sprintf("%d ", i))
		}
		fmt.Fprint(out, "\n")
	}
}

func Counter(n, m, i, deep int, depth *[]int, field [][]byte, visited [][]bool, sInfo *[][]int) {
	supJ := 0
	if len(*depth) != 0 {
		supJ = (*sInfo)[0][len(*depth)-1]
	}
	l := m
	if len(*depth) != 0 {
		l = (*sInfo)[1][len(*depth)-1] - (*sInfo)[0][len(*depth)-1] + 1
	}
	for ; i < n; i++ {
		for j := supJ; j < l+supJ; j++ {
			if visited[i][j] {
				continue
			}
			visited[i][j] = true

			if field[i][j] == '.' {
				continue
			}
			if j == m-1 || field[i][j+1] == '.' {
				continue
			}
			if len((*sInfo)[0]) != 0 && len((*sInfo)[2]) != len((*sInfo)[3]) && j == supJ {
				(*sInfo)[3] = append((*sInfo)[3], i)
				for ; j < m && field[i][j] != '.'; j++ {
					visited[i][j] = true
				}
				return
			}
			*depth = append(*depth, deep+1)
			(*sInfo)[0] = append((*sInfo)[0], j)
			(*sInfo)[2] = append((*sInfo)[2], i)
			for ; j < m && field[i][j] != '.'; j++ {
				visited[i][j] = true
			}
			j--
			(*sInfo)[1] = append((*sInfo)[1], j)

			Counter(n, m, i+1, deep+1, depth, field, visited, sInfo)
		}
	}
}
