package _defer

import (
	"bytes"
	"testing"
)

func TestPrintSequence(t *testing.T) {
	testCases := []struct {
		name       string
		wantWriter string
	}{
		{
			"SECOND PART BONUS",
			"1345287960",
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			writer := &bytes.Buffer{}
			PrintSequence(writer)
			gotWriter := writer.String()
			if gotWriter != tc.wantWriter {
				t.Errorf("PrintSequence() = %v, want %v", gotWriter, tc.wantWriter)
			}
		})
	}
}
