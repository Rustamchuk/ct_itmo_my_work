package reader

import (
	"fmt"
	"io"
	"os"
)

type FileReader struct {
	file *os.File
}

func NewFileReader(fileName string) (*FileReader, error) {
	file, err := os.Open(fileName)
	if err != nil {
		return nil, err
	}
	return &FileReader{file: file}, nil
}

func (f *FileReader) Read(p []byte) (n int, err error) {
	n, err = f.file.Read(p)
	return
}

func (f *FileReader) SetCursor(offset int64) error {
	n, err := io.CopyN(io.Discard, f.file, offset)
	if n != offset {
		err = fmt.Errorf("OutOfBounds Exception")
	}
	return err
}

func (f *FileReader) Close() error {
	return f.file.Close()
}
