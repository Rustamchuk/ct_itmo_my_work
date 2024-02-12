package main

import "fmt"

const CELLS = 10

func main() {
	var t, c int
	var nums []int
	fmt.Scan(&t)
	for ; t > 0; t-- {
		nums = []int{0, 4, 3, 2, 1}
		for i := 0; i < CELLS; i++ {
			fmt.Scan(&c)
			nums[c]--
		}
		ans := "YES"
		for i := 1; i < 5; i++ {
			if nums[i] != 0 {
				ans = "NO"
				break
			}
		}
		fmt.Println(ans)
	}
}
