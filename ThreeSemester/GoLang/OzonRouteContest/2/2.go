package main

import (
	"bufio"
	"fmt"
	"math"
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
		var n, p, sum float64
		fmt.Fscan(in, &n, &p)
		for ; n > 0; n-- {
			var a float64
			fmt.Fscan(in, &a)
			cur := a * p / 100
			sum += math.Mod(cur*100, 100) / 100
		}
		fmt.Fprint(out, fmt.Sprintf("%.2f\n", sum))
	}
}
