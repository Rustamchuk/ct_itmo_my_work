package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()

	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)
	var n int
	fmt.Fscan(in, &n)
	workers := make([]string, n)
	for i := 0; i < n; i++ {
		var str string
		fmt.Fscan(in, &str)
		workers[i] = str
	}
	var k int
	fmt.Fscan(in, &k)
	for i := 0; i < k; i++ {
		var str string
		fmt.Fscan(in, &str)
		var res bool
	full:
		for j := 0; j < n; j++ {
			if len(str) != len(workers[j]) {
				continue
			}
			if str == workers[j] {
				res = true
				break
			}
			was := false
			for l := 0; l < len(str); l++ {
				if str[l] != workers[j][l] {
					if was {
						res = false
						continue full
					}
					was = true
					if l+1 == len(str) {
						res = false
						continue full
					}
					if str[l] != workers[j][l+1] || str[l+1] != workers[j][l] {
						res = false
						continue full
					}
					l++
				}
			}
			res = true
			break
		}
		if res {
			fmt.Fprintln(out, 1)
			continue
		}
		fmt.Fprintln(out, 0)
	}
}
