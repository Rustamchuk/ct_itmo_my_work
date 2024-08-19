package main

import "fmt"

/*
Реализовать пересечение двух неупорядоченных множеств.
*/

func main() {
	var (
		arr1     = []int{3, 1, 5, 2, 4}
		arr2     = []int{7, 4, 3, 6, 5}
		interMap = make(map[int]struct{}) // Нет необходимости хранить значение, храним ничего не весящую структуру
		res      = make([]int, 0)
	)

	// Кладем в мапу все ключи из массива 1
	for _, num := range arr1 {
		interMap[num] = struct{}{}
	}

	// Идем по числам массива 2, если ключ с таким значением уже есть, значит нам подходит
	for _, num := range arr2 {
		if _, ok := interMap[num]; ok {
			res = append(res, num)
		}
	}
	fmt.Println(res)
}
