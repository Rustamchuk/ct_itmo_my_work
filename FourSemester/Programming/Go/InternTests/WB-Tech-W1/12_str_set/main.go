package main

import "fmt"

/*
Имеется последовательность строк -
(cat, cat, dog, cat, tree) создать для нее собственное множество.
*/

func main() {
	var (
		strs   = []string{"cat", "cat", "dog", "cat", "tree"}
		strMap = make(map[string]struct{}) // Мапа, чтобы убрать повторы
		res    = make([]string, 0)         // Слайс результатов
	)

	// Все значения используем как ключ, заполняем мапу
	// Повторы убираются благодаря одинаковому Хэшу
	for _, str := range strs {
		strMap[str] = struct{}{}
	}

	// Проходимся по ключам, добавляем в слайс
	for key := range strMap {
		res = append(res, key)
	}
	fmt.Println(res)
}
