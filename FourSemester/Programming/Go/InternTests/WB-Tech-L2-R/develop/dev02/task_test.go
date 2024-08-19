package main

import "testing"

func TestUnpackString(t *testing.T) {
	tests := []struct {
		input    string
		expected string
		err      bool
	}{
		{"a4bc2d5e", "aaaabccddddde", false},
		{"abcd", "abcd", false},
		{"45", "", true},
		{"", "", false},
		{"qwe\\4\\5", "qwe45", false},
		{"qwe\\45", "qwe44444", false},
		{"qwe\\\\5", "qwe\\\\\\\\\\", false},
	}

	for _, test := range tests {
		output, err := unpackString(test.input)
		if (err != nil) != test.err {
			t.Errorf("Test failed for input %s, expected error: %v, got: %v", test.input, test.err, err)
		}
		if output != test.expected {
			t.Errorf("Test failed for input %s, expected output: %s, got: %s", test.input, test.expected, output)
		}
	}
}
