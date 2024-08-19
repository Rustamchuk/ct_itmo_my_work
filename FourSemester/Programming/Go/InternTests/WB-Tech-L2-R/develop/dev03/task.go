package main

import (
	"bufio"
	"flag"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

/*
=== Утилита sort ===

Отсортировать строки (man sort)
Основное

Поддержать ключи

-k — указание колонки для сортировки
-n — сортировать по числовому значению
-r — сортировать в обратном порядке
-u — не выводить повторяющиеся строки

Дополнительное

Поддержать ключи

-M — сортировать по названию месяца
-b — игнорировать хвостовые пробелы
-c — проверять отсортированы ли данные
-h — сортировать по числовому значению с учётом суффиксов

Программа должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

// Определение флагов
var (
	column  = flag.Int("k", 1, "column to sort by")
	numeric = flag.Bool("n", false, "sort numerically")
	reverse = flag.Bool("r", false, "reverse the result of comparisons")
	unique  = flag.Bool("u", false, "output only the first of an equal run")
)

func main() {
	flag.Parse()

	if flag.NArg() != 1 {
		// Выводим Usage
		fmt.Fprintf(os.Stderr, "Usage: %s [options] <filename>\n", os.Args[0])
		flag.PrintDefaults()
		os.Exit(1)
	}

	// Берем файл чтения
	filename := flag.Arg(0)
	file, err := os.Open(filename)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error opening file: %v\n", err)
		os.Exit(1)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	var lines []string

	// Достаем строки
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	if err := scanner.Err(); err != nil {
		fmt.Fprintf(os.Stderr, "Error reading file: %v\n", err)
		os.Exit(1)
	}

	// Сортировка
	lines = sortLines(lines, *column-1, *numeric, *reverse, *unique)

	// Вывод
	for _, line := range lines {
		fmt.Println(line)
	}
}

// Метод сортировки
func sortLines(lines []string, col int, numeric, reverse, unique bool) []string {
	sort.SliceStable(lines, func(i, j int) bool {
		// Берем две строки
		icol := strings.Fields(lines[i])
		jcol := strings.Fields(lines[j])

		if col >= len(icol) || col >= len(jcol) {
			return false
		}

		// Численно
		if numeric {
			ival, ierr := strconv.ParseFloat(icol[col], 64)
			jval, jerr := strconv.ParseFloat(jcol[col], 64)
			if ierr != nil || jerr != nil {
				return false
			}
			if reverse {
				return ival > jval
			}
			return ival < jval
		}

		// Реверс
		if reverse {
			return icol[col] > jcol[col]
		}
		// Проверка по столбцам
		return icol[col] < jcol[col]
	})

	// На уникальность
	if unique {
		return uniqueLines(lines)
	}
	return lines
}

// Метод для флага уникальности
func uniqueLines(lines []string) []string {
	// Используем мапу, возвращаем новый массив, чтобы перезаписать len
	var unique []string
	seen := make(map[string]bool)
	for _, line := range lines {
		if !seen[line] {
			seen[line] = true
			unique = append(unique, line)
		}
	}
	return unique
}
