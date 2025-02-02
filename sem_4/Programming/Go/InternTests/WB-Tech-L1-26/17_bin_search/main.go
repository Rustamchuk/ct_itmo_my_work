package main

import "fmt"

/*
Реализовать бинарный поиск встроенными методами языка.
*/

// binarySearch реализует алгоритм бинарного поиска
// Возвращает индекс искомого элемента или -1, если элемент не найден
func binarySearch(arr []int, target int) int {
	l := 0
	r := len(arr) - 1

	for l <= r {
		mid := (r + l) / 2
		guess := arr[mid]

		switch {
		case guess == target: // Нашли ответ
			return mid
		case guess > target: // Правая половина не подходит
			r = mid - 1
		default: // Левая половина не подходит
			l = mid + 1
		}
	}

	return -1 // Элемент не найден
}

func main() {
	arr := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	target := 7

	index := binarySearch(arr, target)
	if index != -1 {
		fmt.Printf("Элемент %d найден на позиции %d.\n", target, index)
		return
	}
	fmt.Printf("Элемент %d не найден.\n", target)
}
