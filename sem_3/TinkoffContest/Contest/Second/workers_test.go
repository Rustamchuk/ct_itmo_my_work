package main

import (
	"math/rand"
	"testing"
	"time"
)

func Test_workers(t *testing.T) {
	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 100; i++ {
		// Генерация случайной строки
		cur := make([]int, rand.Intn(15))
		var sum int64 = 0
		for i := 0; i < len(cur); i++ {
			cur[i] = rand.Intn(9)
			sum += int64(cur[i])
		}
		// Проверка результата
		quickSort(cur, 0, len(cur)-1)
		m := 1
		res := "No"
		for j := 0; j < len(cur); j++ {
			if m >= len(cur) {
				res = "Yes"
				break
			}
			for ; cur[j] > 0; cur[j]-- {
				if m >= len(cur) {
					res = "Yes"
					break
				}
				cur[m]--
				m++
			}
		}
		actual := "No"
		if sum/2+1 >= int64(len(cur)) {
			actual = "Yes"
		}
		if res != actual {
			t.Errorf("Test failed for input %v: expected %s, got %s", cur, res, actual)
		}
	}
}

func quickSort(arr []int, l, r int) {
	if len(arr) == 0 || l >= r {
		return
	}
	mid := rand.Intn(r-l) + l
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
