package main

import (
	"reflect"
	"testing"
)

func TestSortLines(t *testing.T) {
	tests := []struct {
		name     string
		lines    []string
		col      int
		numeric  bool
		reverse  bool
		unique   bool
		expected []string
	}{
		{
			name:     "Sort by first column alphabetically",
			lines:    []string{"b line", "a line"},
			col:      0,
			numeric:  false,
			reverse:  false,
			unique:   false,
			expected: []string{"a line", "b line"},
		},
		{
			name:     "Sort by second column numerically",
			lines:    []string{"row 10", "row 2"},
			col:      1,
			numeric:  true,
			reverse:  false,
			unique:   false,
			expected: []string{"row 2", "row 10"},
		},
		{
			name:     "Reverse sort by first column",
			lines:    []string{"a line", "b line"},
			col:      0,
			numeric:  false,
			reverse:  true,
			unique:   false,
			expected: []string{"b line", "a line"},
		},
		{
			name:     "Unique lines",
			lines:    []string{"a line", "a line"},
			col:      0,
			numeric:  false,
			reverse:  false,
			unique:   true,
			expected: []string{"a line"},
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			result := sortLines(test.lines, test.col, test.numeric, test.reverse, test.unique)
			if !reflect.DeepEqual(result, test.expected) {
				t.Errorf("Test %s failed: expected %v, got %v", test.name, test.expected, result)
			}
		})
	}
}

func TestUniqueLines(t *testing.T) {
	lines := []string{"one", "two", "two", "three", "three", "three"}
	expected := []string{"one", "two", "three"}
	result := uniqueLines(lines)
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("uniqueLines failed: expected %v, got %v", expected, result)
	}
}
