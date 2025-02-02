package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	writer := bufio.NewWriter(os.Stdout)
	defer writer.Flush()
	scanner.Scan()
	p := scanner.Text()
	scanner.Scan()
	t := scanner.Text()

	connected := p + "#" + t
	n, pLen, tLen := len(connected), len(p), len(t)
	prefix := make([]int, n)
	for i := 1; i < n; i++ {
		j := prefix[i-1]
		for j > 0 && connected[i] != connected[j] {
			j = prefix[j-1]
		}
		if connected[i] == connected[j] {
			j++
		}
		prefix[i] = j
	}
	res := make([]int, 0)
	for i := pLen; i < pLen+1+tLen; i++ {
		if prefix[i] == pLen {
			res = append(res, i-pLen*2+1)
		}
	}
	fmt.Fprintln(writer, len(res))
	var sb strings.Builder
	for i := 0; i < len(res); i++ {
		sb.WriteString(fmt.Sprintf("%d ", res[i]))
	}
	fmt.Fprint(writer, sb.String())

	//fmt.Println(len(res))
	//for i := 0; i < len(res); i++ {
	//	fmt.Print(res[i])
	//	fmt.Print(" ")
	//}
}
