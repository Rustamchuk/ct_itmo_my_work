package main

import (
	"bufio"
	"flag"
	"fmt"
	"os"
	"strconv"
	"strings"
)

/*
=== Утилита cut ===

Принимает STDIN, разбивает по разделителю (TAB) на колонки, выводит запрошенные

Поддержать флаги:
-f - "fields" - выбрать поля (колонки)
-d - "delimiter" - использовать другой разделитель
-s - "separated" - только строки с разделителем

Программа должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

type cutOptions struct {
	fieldIndexes  []int
	delimiter     string
	separatedOnly bool
}

func main() {
	opts := parseFlags()
	processInput(opts)
}

// Парсинг флагов
func parseFlags() cutOptions {
	fieldsFlag := flag.String("f", "", "fields to extract (e.g., 1,2,3)")
	delimiterFlag := flag.String("d", "\t", "delimiter to use for splitting the fields")
	separatedOnlyFlag := flag.Bool("s", false, "only process lines that contain the delimiter")
	flag.Parse()

	if *fieldsFlag == "" {
		fmt.Fprintln(os.Stderr, "Error: -f flag is required")
		os.Exit(1)
	}

	fieldIndexes, err := parseFields(*fieldsFlag)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error parsing fields: %v\n", err)
		os.Exit(1)
	}

	return cutOptions{
		fieldIndexes:  fieldIndexes,
		delimiter:     *delimiterFlag,
		separatedOnly: *separatedOnlyFlag,
	}
}

// Парсинг флагов
func parseFields(fieldStr string) ([]int, error) {
	var fields []int
	for _, f := range strings.Split(fieldStr, ",") {
		field, err := strconv.Atoi(f)
		if err != nil {
			return nil, err
		}
		fields = append(fields, field)
	}
	return fields, nil
}

// Cut
func processInput(opts cutOptions) {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()

		// Проверка на разделители
		if opts.separatedOnly && !strings.Contains(line, opts.delimiter) {
			continue
		}

		// Разбивка на столбцы
		columns := strings.Split(line, opts.delimiter)
		selectedFields := selectFields(columns, opts.fieldIndexes)
		if len(selectedFields) > 0 {
			fmt.Println(strings.Join(selectedFields, opts.delimiter))
		}
	}

	if err := scanner.Err(); err != nil {
		fmt.Fprintf(os.Stderr, "Error reading standard input: %v\n", err)
		os.Exit(1)
	}
}

// Выбор полей
func selectFields(columns []string, fieldIndexes []int) []string {
	var selectedFields []string
	for _, index := range fieldIndexes {
		if index-1 < len(columns) {
			selectedFields = append(selectedFields, columns[index-1])
		}
	}
	return selectedFields
}
