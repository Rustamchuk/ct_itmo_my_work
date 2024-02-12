package main

import "fmt"

func main() {
	// 31: 1 3 5 7 8 10 12
	// 30: 4 6 9 11
	// 28/29: 2

	var t int
	fmt.Scan(&t)
	days := []int{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
	var d, m, y int
	var ans string
	for ; t > 0; t-- {
		fmt.Scan(&d, &m, &y)
		ans = "YES"
		if m == 2 && d > 28 {
			if d > 29 || (y%4 != 0 || y%100 == 0) && y%400 != 0 {
				ans = "NO"
			}
		} else if d > days[m] {
			ans = "NO"
		}
		fmt.Println(ans)
	}
}
