package main

import "fmt"

func main() {
	var n, k int
	fmt.Scan(&n)
	for i := 0; i < n; i++ {
		fmt.Scan(&k)
		arr := make([]int, k)
		for j := 0; j < k; j++ {
			fmt.Scan(&arr[j])
		}
		quickSort(arr, 0, k-1)
		m := 1
		res := "NO"
		for j := 0; j < k; j++ {
			if m >= k {
				res = "YES"
				break
			}
			for ; arr[j] > 0; arr[j]-- {
				arr[m]--
				m++
			}
		}
		fmt.Println(res)
	}
}

func quickSort(arr []int, l, r int) {
	if len(arr) == 0 || l >= r {
		return
	}
	mid := (r + l) / 2
	sup := arr[mid]
	i, j := l, r
	for i <= j {
		for arr[i] > sup {
			i++
		}
		for arr[j] < sup {
			j--
		}
		if i <= j {
			swap := arr[i]
			arr[i] = arr[j]
			arr[j] = swap
			i++
			j--
		}
	}
	quickSort(arr, l, j)
	quickSort(arr, i, r)
}
