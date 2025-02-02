package main

import (
	"fmt"
	"strings"
)

/*
Разработать программу, которая переворачивает слова в строке.
Пример: «snow dog sun — sun dog snow».
*/

// reverseWords переворачивает слова в строке
func reverseWords(s string) string {
	words := strings.Fields(s) // Разделяем строку на слова
	for i, j := 0, len(words)-1; i < j; i, j = i+1, j-1 {
		words[i], words[j] = words[j], words[i] // Меняем местами слова
	}
	return strings.Join(words, " ") // Объединяем слова обратно в строку
}

func main() {
	input := "snow dog sun"
	reversed := reverseWords(input)
	fmt.Println("Оригинальная строка:", input)
	fmt.Println("Перевернутая строка:", reversed)
}
