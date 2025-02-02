package reader

import (
	"fmt"
	"io"
	"os"
)

type StdReader struct{}

func NewStdReader() *StdReader {
	return &StdReader{}
}

func (s *StdReader) Read(p []byte) (n int, err error) {
	n, err = os.Stdin.Read(p)
	return
}

func (s *StdReader) SetCursor(offset int64) error {
	n, err := io.CopyN(io.Discard, os.Stdin, offset)
	if n != offset {
		err = fmt.Errorf("OutOfBounds Exception")
	}
	return err
}

func (s *StdReader) Close() error {
	return nil
}
