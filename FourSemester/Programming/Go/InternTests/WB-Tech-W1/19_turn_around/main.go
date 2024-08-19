package main

import "fmt"

/*
Разработать программу, которая переворачивает
подаваемую на ход строку (например: «главрыба — абырвалг»).
Символы могут быть unicode
*/

// reverseString переворачивает строку, учитывая Unicode символы
func reverseString(s string) string {
	// Преобразуем строку в срез rune для корректной работы с Unicode
	runes := []rune(s)
	for i, j := 0, len(runes)-1; i < j; i, j = i+1, j-1 {
		runes[i], runes[j] = runes[j], runes[i] // Свап
	}
	return string(runes) // Преобразуем массив символов в строку
}

func main() {
	input := "главрыба"
	reversed := reverseString(input)
	fmt.Println("Оригинальная строка:", input)
	fmt.Println("Перевернутая строка:", reversed)
}
