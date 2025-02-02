package writer

import (
	"os"
)

type StdWriter struct{}

func NewStdWriter() *StdWriter {
	return &StdWriter{}
}

func (s *StdWriter) Write(p []byte) error {
	_, err := os.Stdout.Write(p)
	return err
}

func (s *StdWriter) Close() error {
	return nil
}
