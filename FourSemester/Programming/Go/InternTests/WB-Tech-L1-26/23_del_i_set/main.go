package main

import "fmt"

/*
Удалить i-ый элемент из слайса.
*/

func removeAtIndex(s []int, i int) []int {
	if i < 0 || i >= len(s) {
		return s // Возвращаем исходный слайс, если индекс вне допустимого диапазона
	}
	newSlice := make([]int, 0)
	// левая часть до i + правая часть после i [0,1..(i-1)] i [(i+1)..n]
	return append(append(newSlice, s[:i]...), s[i+1:]...)
}

func main() {
	slice := []int{1, 2, 3, 4, 5}
	index := 2 // Удаляем элемент с индексом 2 (третий элемент, число 3)

	// Удаление элемента
	newSlice := removeAtIndex(slice, index)
	fmt.Println("Исходный слайс:", slice)
	fmt.Println("Слайс после удаления элемента:", newSlice)
}
