package main

import (
	"bufio"
	"flag"
	"fmt"
	"os"
	"strings"
)

/*
=== Утилита grep ===

Реализовать утилиту фильтрации (man grep)

Поддержать флаги:
-A - "after" печатать +N строк после совпадения
-B - "before" печатать +N строк до совпадения
-C - "context" (A+B) печатать ±N строк вокруг совпадения
-c - "count" (количество строк)
-i - "ignore-case" (игнорировать регистр)
-v - "invert" (вместо совпадения, исключать)
-F - "fixed", точное совпадение со строкой, не паттерн
-n - "line num", печатать номер строки

Программа должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

type grepOptions struct {
	after      int
	before     int
	context    int
	countOnly  bool
	ignoreCase bool
	invert     bool
	fixed      bool
	lineNum    bool
}

func main() {
	// Парсинг флагов
	opts := parseFlags()

	// Проверка аргументов
	if flag.NArg() != 2 {
		fmt.Println("Usage: grep [options] <pattern> <file>")
		os.Exit(1)
	}

	pattern := flag.Arg(0)
	filename := flag.Arg(1)

	// Выполнение поиска
	results := grep(pattern, filename, opts)

	// Вывод результатов
	for _, result := range results {
		fmt.Println(result)
	}
}

// Метод парсинга флагов
func parseFlags() grepOptions {
	var opts grepOptions
	flag.IntVar(&opts.after, "A", 0, "print +N lines after match")
	flag.IntVar(&opts.before, "B", 0, "print +N lines before match")
	flag.IntVar(&opts.context, "C", 0, "print ±N lines around match")
	flag.BoolVar(&opts.countOnly, "c", false, "count of matching lines")
	flag.BoolVar(&opts.ignoreCase, "i", false, "ignore case distinctions")
	flag.BoolVar(&opts.invert, "v", false, "select non-matching lines")
	flag.BoolVar(&opts.fixed, "F", false, "interpret pattern as a fixed string")
	flag.BoolVar(&opts.lineNum, "n", false, "print line number with output lines")
	flag.Parse()
	return opts
}

// Метод поиска
func grep(pattern, filename string, opts grepOptions) []string {
	// Открываем файл
	file, err := os.Open(filename)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error opening file: %v\n", err)
		os.Exit(1)
	}
	defer file.Close()

	var results []string
	scanner := bufio.NewScanner(file)
	var lines []string // Сохраняем все строки для доступа к контексту
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	// Перебор строк
	for i, line := range lines {
		match := lineMatches(line, pattern, opts)

		// Если да, то добавляем, проверяя на необходимость контекста влево вправо
		if match {
			start := max(0, i-opts.before)
			if opts.context > 0 {
				start = max(0, i-opts.context)
			}
			end := min(len(lines)-1, i+opts.after)
			if opts.context > 0 {
				end = min(len(lines)-1, i+opts.context)
			}

			for j := start; j <= end; j++ {
				results = append(results, formatLine(lines[j], j+1, opts.lineNum))
			}
		}
	}

	// Проверка на флаг количества
	if opts.countOnly {
		return []string{fmt.Sprintf("Count: %d", len(results))}
	}

	return results
}

// Форматирование
func formatLine(line string, lineNum int, lineNumFlag bool) string {
	if lineNumFlag {
		return fmt.Sprintf("%d:%s", lineNum, line)
	}
	return line
}

// Метод проверки паттерна
func lineMatches(line, pattern string, opts grepOptions) bool {
	if opts.ignoreCase {
		line = strings.ToLower(line)
		pattern = strings.ToLower(pattern)
	}

	match := false
	if opts.fixed {
		match = line == pattern
	} else {
		match = strings.Contains(line, pattern)
	}

	// Инвертируем результат, если установлен флаг -v
	if opts.invert {
		return !match
	}
	return match
}
