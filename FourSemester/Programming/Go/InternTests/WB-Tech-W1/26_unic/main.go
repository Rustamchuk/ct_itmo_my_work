package main

import (
	"fmt"
	"strings"
)

/*
Разработать программу, которая проверяет,
что все символы в строке уникальные
(true — если уникальные, false etc).
Функция проверки должна быть регистронезависимой.

Например:
abcd — true
abCdefAaf — false
aabcd — false
*/

// isUnique проверяет, все ли символы в строке уникальны (регистронезависимо)
func isUnique(s string) bool {
	charMap := make(map[rune]struct{}) // Значение - структура, так как, что-то, что ничего не весит
	s = strings.ToLower(s)             // Приводим строку к нижнему регистру для регистронезависимости

	for _, char := range s {
		if _, exists := charMap[char]; exists {
			return false // Символ уже встречался
		}
		charMap[char] = struct{}{}
	}
	return true
}

func main() {
	testStrings := []string{"abcd", "abCdefAaf", "aabcd", " "}

	for _, str := range testStrings {
		fmt.Printf("Строка \"%s\" содержит все уникальные символы: %t\n", str, isUnique(str))
	}
}
