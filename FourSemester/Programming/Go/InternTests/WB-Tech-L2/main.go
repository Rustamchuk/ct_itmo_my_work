package main

import (
	"log"
)

func main() {
	arr1 := []int{1, 2}
	arr2 := []int{3, 4}
	median := findMedian(arr1, arr2)
	log.Println(median)
}

func findMedian(arr1, arr2 []int) float64 {
	if len(arr1) > len(arr2) {
		return findMedian(arr2, arr1)
	}

	l1, l2 := len(arr1), len(arr2)
	l, r := 0, l1

	for l <= r {
		m1 := (l + r) / 2
		m2 := (l1 + l2 + 1) / 2
		m2 -= m1

		max1 := getMax(arr1, m1)
		max2 := getMax(arr2, m2)
		min1 := getMin(arr1, m1)
		min2 := getMin(arr2, m2)

		if max1 <= min2 && max2 <= min1 {
			if (l1+l2)%2 == 0 {
				return float64(max(max1, max2)+min(min1, min2)) / 2
			}
			return float64(max(max1, max2))
		}
		if max1 > min2 {
			r = m1 - 1
			continue
		}
		l = m1 + 1
	}

	return 0
}

func getMax(arr []int, m int) int {
	if m == 0 {
		return -1 << 31
	}
	return arr[m-1]
}

func getMin(arr []int, m int) int {
	if m == len(arr) {
		return 1<<31 - 1
	}
	return arr[m]
}
