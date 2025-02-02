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
		var n int
		fmt.Fscan(in, &n)
		arr := make([]int, n)
		for i := 0; i < n; i++ {
			num := 0
			fmt.Fscan(in, &num)
			arr[i] = num
		}
		outt := make([]int, 0)
		for i := 1; i <= n; i++ {
			maxR := 0
			r := 0
		cir:
			for j := 0; j < n; j++ {
				res1, res2 := false, false
				for l := j - 1; l >= 0 && j-l <= i; l-- {
					if arr[l] >= arr[l+1] {
						if maxR < r {
							maxR = r
						}
						r = 0
						continue cir
					}
					if j-l == i {
						res1 = true
					}
				}
				for l := j + 1; l < n && l-j <= i; l++ {
					if arr[l] >= arr[l-1] {
						if maxR < r {
							maxR = r
						}
						r = 0
						continue cir
					}
					if l-j == i {
						res2 = true
					}
				}
				if res1 && res2 {
					r++
					j++
				}
			}
			if r > maxR {
				maxR = r
			}
			outt = append(outt, maxR)
			//fmt.Println(maxR)
		}
		for i, o := range outt {
			fmt.Fprint(out, o)
			if i != len(outt)-1 {
				fmt.Fprint(out, " ")
			}
		}
		fmt.Fprintln(out)
	}
}
