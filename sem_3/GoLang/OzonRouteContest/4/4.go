package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

var up, down bool

func main() {
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()

	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)
	var t int
	fmt.Fscan(in, &t)
	for ; t > 0; t-- {
		var n, m int
		var str string
		up, down = false, false
		fmt.Fscan(in, &n, &m)
		field := make([][]rune, n)
		for i := 0; i < n; i++ {
			fmt.Fscan(in, &str)
			field[i] = []rune(str)
		}
		for i := 0; i < n; i++ {
			for j := 0; j < m; j++ {
				if field[i][j] == 'A' {
					if !up && dfsUP(i, j, 0, 0, 'a', field) {
						up = true
					} else if !down && dfsDWN(i, j, 0, 0, 'a', field) {
						down = true
					}
				} else if field[i][j] == 'B' {
					if !up && dfsUP(i, j, 0, 0, 'b', field) {
						up = true
					} else if !down && dfsDWN(i, j, 0, 0, 'b', field) {
						down = true
					}
				}
			}
		}
		outp := make([]string, n)
		for i := 0; i < n; i++ {
			outp[i] = string(field[i])
		}
		fmt.Fprintln(out, strings.Join(outp, "\n"))
	}
}

func dfsUP(i, j int, u, g int, c rune, field [][]rune) bool {
	if up {
		return false
	}
	if i == 0 && j == 0 {
		if u != 0 || g != 0 {
			field[0][0] = c
		}
		return true
	}
	if u != 1 && i-1 >= 0 && field[i-1][j] == '.' {
		if dfsUP(i-1, j, -1, 0, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if g != -1 && j-1 >= 0 && field[i][j-1] == '.' {
		if dfsUP(i, j-1, 0, 1, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if u != -1 && i+1 < len(field) && field[i+1][j] == '.' {
		if dfsUP(i+1, j, 1, 0, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if g != 1 && j+1 < len(field[0]) && field[i][j+1] == '.' {
		if dfsUP(i, j+1, 0, -1, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	return false
}

func dfsDWN(i, j int, u, g int, c rune, field [][]rune) bool {
	if down {
		return false
	}
	if i == len(field)-1 && j == len(field[0])-1 {
		if u != 0 || g != 0 {
			field[len(field)-1][len(field[0])-1] = c
		}
		return true
	}
	if u != -1 && i+1 < len(field) && field[i+1][j] == '.' {
		if dfsDWN(i+1, j, 1, 0, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if g != 1 && j+1 < len(field[0]) && field[i][j+1] == '.' {
		if dfsDWN(i, j+1, 0, -1, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if u != 1 && i-1 > 0 && field[i-1][j] == '.' {
		if dfsDWN(i-1, j, -1, 0, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	if g != -1 && j-1 > 0 && field[i][j-1] == '.' {
		if dfsDWN(i, j-1, 0, 1, c, field) {
			if u != 0 || g != 0 {
				field[i][j] = c
			}
			return true
		}
	}
	return false
}
