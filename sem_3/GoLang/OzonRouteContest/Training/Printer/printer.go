package main

import (
	"fmt"
	"strings"
)

func main() {
	var t, n int
	var str string
	output := make([]string, 0)
	fmt.Scan(&t)
	for ; t > 0; t-- {
		fmt.Scan(&n)
		fmt.Scan(&str)
		nums := strings.Split(str, ",")
		res := make([]int, n+1)
		for _, num := range nums {
			lr := strings.Split(num, "-")
			if len(lr) == 1 {
				var ind int
				fmt.Sscanf(lr[0], "%d", &ind)
				res[ind] = 1
				continue
			}
			var ind1, ind2 int
			fmt.Sscanf(lr[0], "%d", &ind1)
			fmt.Sscanf(lr[1], "%d", &ind2)
			for i := ind1; i <= ind2; i++ {
				res[i] = 1
			}
		}
		ans := make([]string, 0)
		l, r := 1, 1
		for ; r < len(res); r++ {
			l = r
			yes := false
			for ; r < len(res) && res[r] == 0; r++ {
				yes = true
			}
			if yes {
				r--
				if l == r {
					ans = append(ans, fmt.Sprintf("%d", l))
					continue
				}
				ans = append(ans, fmt.Sprintf("%d-%d", l, r))
			}
		}
		output = append(output, strings.Join(ans, ","))
		//print(strings.Join(ans, ","))
	}
	fmt.Println(strings.Join(output, "\n"))
}
