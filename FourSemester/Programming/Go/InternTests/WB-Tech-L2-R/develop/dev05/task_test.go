package main

import (
	"os"
	"testing"
)

func TestGrep(t *testing.T) {
	tests := []struct {
		name     string
		pattern  string
		content  []string
		opts     grepOptions
		expected []string
	}{
		{
			name:     "Basic match",
			pattern:  "test",
			content:  []string{"this is a test", "another line", "test again"},
			opts:     grepOptions{},
			expected: []string{"this is a test", "test again"},
		},
		{
			name:     "Case insensitive match",
			pattern:  "TEST",
			content:  []string{"this is a test", "another line", "test again"},
			opts:     grepOptions{ignoreCase: true},
			expected: []string{"this is a test", "test again"},
		},
		{
			name:     "Invert match",
			pattern:  "test",
			content:  []string{"this is a test", "another line", "test again"},
			opts:     grepOptions{invert: true},
			expected: []string{"another line"},
		},
		{
			name:     "Fixed string match",
			pattern:  "a test exactly",
			content:  []string{"this is a test", "another line", "a test exactly"},
			opts:     grepOptions{fixed: true},
			expected: []string{"a test exactly"},
		},
		{
			name:     "Count lines",
			pattern:  "test",
			content:  []string{"test", "test", "no match"},
			opts:     grepOptions{countOnly: true},
			expected: []string{"Count: 2"},
		},
		{name: "After match",
			pattern:  "test",
			content:  []string{"line1", "line2 test", "line3", "line4"},
			opts:     grepOptions{after: 1},
			expected: []string{"line2 test", "line3"},
		},
		{
			name:     "Before match",
			pattern:  "test",
			content:  []string{"line1", "line2", "line3 test", "line4"},
			opts:     grepOptions{before: 1},
			expected: []string{"line2", "line3 test"},
		},
		{
			name:     "Context match",
			pattern:  "test",
			content:  []string{"line1", "line2", "line3 test", "line4", "line5"},
			opts:     grepOptions{context: 1},
			expected: []string{"line2", "line3 test", "line4"},
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			// Создание временного файла с содержимым
			tmpfile, err := os.CreateTemp("", "example.*.txt")
			if err != nil {
				t.Fatal(err)
			}
			defer os.Remove(tmpfile.Name()) // очистка

			for _, line := range test.content {
				_, err := tmpfile.WriteString(line + "\n")
				if err != nil {
					t.Fatal(err)
				}
			}
			if err := tmpfile.Close(); err != nil {
				t.Fatal(err)
			}

			// Вызов функции grep
			results := grep(test.pattern, tmpfile.Name(), test.opts)

			// Сравнение результатов
			if !compareSlices(results, test.expected) {
				t.Errorf("Test %s failed: expected %v, got %v", test.name, test.expected, results)
			}
		})
	}
}

func compareSlices(a, b []string) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}
