package main

import (
	"fmt"
)

func main() {
	var n, k int
	fmt.Scan(&n)
	for i := 0; i < n; i++ {
		fmt.Scan(&k)
		var sum, cur int64 = 0, 0
		for j := 0; j < k; j++ {
			fmt.Scan(&cur)
			sum += cur
		}
		if sum/2+1 >= int64(k) {
			fmt.Println("Yes")
		} else {
			fmt.Println("No")
		}
	}
}
