package main

import (
	"reflect"
	"testing"
)

func TestFindAnagrams(t *testing.T) {
	tests := []struct {
		name     string
		input    []string
		expected map[string]*[]string
	}{
		{
			name:  "Standard case with multiple anagrams",
			input: []string{"пятак", "пятка", "тяпка", "листок", "слиток", "столик", "слово"},
			expected: map[string]*[]string{
				"листок": {"листок", "слиток", "столик"},
				"пятак":  {"пятак", "пятка", "тяпка"},
			},
		},
		{
			name:     "Case with no anagrams",
			input:    []string{"слово", "другое", "ещеодно"},
			expected: map[string]*[]string{},
		},
		{
			name:     "Case with single word groups",
			input:    []string{"один", "один", "два", "два", "три"},
			expected: map[string]*[]string{},
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			result := findAnagrams(&test.input)
			if !reflect.DeepEqual(result, test.expected) {
				t.Errorf("Test %s failed: expected %v, got %v", test.name, test.expected, result)
			}
		})
	}
}
