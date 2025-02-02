package main

import (
	"errors"
	"fmt"
	"os"
	"strconv"
	"strings"
	"unicode"
)

/*
=== Задача на распаковку ===

Создать Go функцию, осуществляющую примитивную распаковку строки, содержащую повторяющиеся символы / руны, например:
	- "a4bc2d5e" => "aaaabccddddde"
	- "abcd" => "abcd"
	- "45" => "" (некорректная строка)
	- "" => ""
Дополнительное задание: поддержка escape - последовательностей
	- qwe\4\5 => qwe45 (*)
	- qwe\45 => qwe44444 (*)
	- qwe\\5 => qwe\\\\\ (*)

В случае если была передана некорректная строка функция должна возвращать ошибку. Написать unit-тесты.

Функция должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

func unpackString(input string) (string, error) {
	var (
		result     strings.Builder // Конкатенируем строки в результат
		escapeMode bool            // Если встретился знак \
		lastChar   rune            // Предыдущий символ
	)

	for _, char := range input {
		// Если это число - количество повторений
		if unicode.IsDigit(char) && !escapeMode {
			// Если нечего клонировать
			if lastChar == 0 {
				return "", errors.New("некорректная строка")
			}
			// Берем количество
			count, err := strconv.Atoi(string(char))
			if err != nil {
				return "", errors.New("ожидалось число")
			}
			// Дополняем недостающие
			result.WriteString(strings.Repeat(string(lastChar), count-1))
			// Обнуляем предыдущий, больше клонировать не надо
			lastChar = 0
			continue
		}
		// Если встретили \
		if char == '\\' && !escapeMode {
			escapeMode = true
			continue
		}
		// Записываем в результат
		result.WriteRune(char)
		lastChar = char
		escapeMode = false
	}

	return result.String(), nil
}

func main() {
	// Пример использования функции
	output, err := unpackString("a4bc2d5e")
	if err != nil {
		fmt.Fprint(os.Stderr, err)
		os.Exit(1)
	}
	println(output)
}
