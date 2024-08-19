package main

import "fmt"

/*
Реализовать быструю сортировку массива (quicksort)
встроенными методами языка.
*/

// quickSort реализует алгоритм быстрой сортировки
func quickSort(arr []int) []int {
	if len(arr) < 2 {
		return arr
	}

	left, right := 0, len(arr)-1

	// Выбираем опорный элемент
	pivot := arr[len(arr)/2]

	// Разделяем массив на элементы меньше опорного и больше опорного
	for left <= right {
		// Ищем нарушение слева
		for arr[left] < pivot {
			left++
		}
		// Ищем нарушение справа
		for arr[right] > pivot {
			right--
		}
		// Если нашли нарушение, то свап
		if left <= right {
			arr[left], arr[right] = arr[right], arr[left]
			left++
			right--
		}
	}

	// Рекурсивно применяем ту же логику к двум подмассивам
	if 0 < right {
		quickSort(arr[:right+1])
	}
	if left < len(arr) {
		quickSort(arr[left:])
	}

	return arr
}

func main() {
	arr := []int{10, 7, 8, 9, 1, 5}
	sortedArr := quickSort(arr)
	fmt.Println("Отсортированный массив:", sortedArr)
}
