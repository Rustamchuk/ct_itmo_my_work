package writer

import (
	"os"
)

type FileWriter struct {
	file *os.File
}

func NewFileWriter(fileName string) (*FileWriter, error) {
	file, err := os.Open(fileName)
	if err != nil {
		file, err = os.Create(fileName)
		if err != nil {
			return nil, err
		}
	}
	return &FileWriter{file: file}, nil
}

func (f *FileWriter) Write(p []byte) error {
	_, err := f.file.Write(p)
	return err
}

func (f *FileWriter) Close() error {
	return f.file.Close()
}
