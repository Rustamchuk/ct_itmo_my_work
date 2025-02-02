package main

import (
	"bytes"
	"io"
	"os"
	"testing"
)

func TestSelectFields(t *testing.T) {
	tests := []struct {
		name         string
		columns      []string
		fieldIndexes []int
		expected     []string
	}{
		{
			name:         "Select single field",
			columns:      []string{"apple", "banana", "cherry"},
			fieldIndexes: []int{2},
			expected:     []string{"banana"},
		},
		{
			name:         "Select multiple fields",
			columns:      []string{"apple", "banana", "cherry"},
			fieldIndexes: []int{1, 3},
			expected:     []string{"apple", "cherry"},
		},
		{
			name:         "Select non-existent field",
			columns:      []string{"apple", "banana", "cherry"},
			fieldIndexes: []int{4},
			expected:     []string{},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := selectFields(tt.columns, tt.fieldIndexes)
			if !compareSlices(result, tt.expected) {
				t.Errorf("selectFields() = %v, want %v", result, tt.expected)
			}
		})
	}
}

func TestProcessInput(t *testing.T) {
	tests := []struct {
		name     string
		input    string
		opts     cutOptions
		expected string
	}{
		{
			name:     "Process simple input",
			input:    "apple\tbanana\tcherry\n",
			opts:     cutOptions{fieldIndexes: []int{2}, delimiter: "\t", separatedOnly: false},
			expected: "banana\n",
		},
		{
			name:     "Process with multiple fields",
			input:    "apple\tbanana\tcherry\n",
			opts:     cutOptions{fieldIndexes: []int{1, 3}, delimiter: "\t", separatedOnly: false},
			expected: "apple\tcherry\n",
		},
		{
			name:     "Ignore lines without delimiter",
			input:    "apple banana cherry\n",
			opts:     cutOptions{fieldIndexes: []int{1}, delimiter: "\t", separatedOnly: true},
			expected: "",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create a pipe to simulate stdin
			inReader, inWriter, err := os.Pipe()
			if err != nil {
				t.Fatal(err)
			}

			// Write input to the pipe
			go func() {
				defer inWriter.Close()
				io.WriteString(inWriter, tt.input)
			}()

			// Capture output by replacing stdout
			oldStdout := os.Stdout
			r, w, err := os.Pipe()
			if err != nil {
				t.Fatal(err)
			}
			os.Stdout = w

			// Restore stdout back to normal
			defer func() {
				os.Stdout = oldStdout
			}()

			// Run the function with the pipe as stdin
			os.Stdin = inReader
			processInput(tt.opts)
			w.Close()

			// Read the captured output
			var buf bytes.Buffer
			io.Copy(&buf, r)
			output := buf.String()

			if output != tt.expected {
				t.Errorf("processInput() = %q, want %q", output, tt.expected)
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
