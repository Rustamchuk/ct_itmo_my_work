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
	var t int
	fmt.Fscan(in, &t)
	for ; t > 0; t-- {
		var str string
		var res bool
		var stat int
		fmt.Fscan(in, &str)
		for _, s := range str {
			switch s {
			case 'M':
				if stat == 0 || stat == 3 {
					stat = 1
				} else {
					res = true
				}
				break
			case 'R':
				if stat == 1 {
					stat = 2
				} else {
					res = true
				}
				break
			case 'C':
				if stat == 1 || stat == 2 {
					stat = 3
				} else {
					res = true
				}
				break
			case 'D':
				if stat != 0 {
					stat = 0
				} else {
					res = true
				}
				break
			}
		}
		if stat != 0 {
			res = true
		}
		if res {
			fmt.Fprint(out, "NO\n")
			continue
		}
		fmt.Fprint(out, "YES\n")
	}
}
