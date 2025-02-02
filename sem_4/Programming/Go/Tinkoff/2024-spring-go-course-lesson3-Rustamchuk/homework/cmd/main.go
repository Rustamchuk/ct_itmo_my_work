package main

import (
	"flag"
	"fmt"
	"io"
	"math"
	"os"
	"strings"
	"unicode"
	"unicode/utf8"
)

const (
	OpenFileException     = "Can not open file:"
	CloseFileException    = "Can not close file:"
	AccessDeniedException = "No access for operation:"
	WrongFlagException    = "Can not parse flag:"
	OutOfBoundsException  = "Argument is out of bounds:"
)

type Options struct {
	From   string
	To     string
	offset int
	limit  int
	block  int
	upper  bool
	lower  bool
	trim   bool
}

func ParseFlags() (*Options, error) {
	var (
		opts Options
		conv string
	)

	flag.StringVar(&opts.From, "from", "", "file to read. by default - stdin")
	flag.StringVar(&opts.To, "to", "", "file to write. by default - stdout")
	flag.IntVar(&opts.offset, "offset", 0, "skip bytes. by default - 0")
	flag.IntVar(&opts.limit, "limit", math.MaxInt32, "max bytes. by default - MaxInt")
	flag.IntVar(&opts.block, "block-size", 1, "block size bytes. by default - 1")
	flag.StringVar(&conv, "conv", "", "upper_case, lower_case, trim_spaces")

	flag.Parse()

	if conv == "" {
		return &opts, nil
	}

	convFlags := strings.Split(conv, ",")
	for _, convFlag := range convFlags {
		switch strings.TrimSpace(convFlag) {
		case "upper_case":
			opts.upper = true
		case "lower_case":
			opts.lower = true
		case "trim_spaces":
			opts.trim = true
		default:
			return nil, fmt.Errorf("wrong conv key")
		}
	}
	if opts.lower && opts.upper {
		return nil, fmt.Errorf("impossible upper and lower at one time")
	}

	return &opts, nil
}

func tryThrowError(message string, err error) {
	if err != nil {
		_, _ = fmt.Fprintln(os.Stderr, message, err)
		os.Exit(1)
	}
}

func initReader(opts *Options) Reader {
	if opts.From == "" {
		return NewStdReader()
	}
	myReader, err := NewFileReader(opts.From)
	tryThrowError(OpenFileException, err)
	return myReader
}

func initWriter(opts *Options) Writer {
	if opts.To == "" {
		return NewStdWriter()
	}
	myWriter, err := NewFileWriter(opts.To)
	tryThrowError(OpenFileException, err)
	return myWriter
}

func processFile(reader Reader, writer Writer, opts *Options) {
	var (
		data      = NewData()
		buffer    = make([]byte, opts.block)
		processed int
		err       error
	)

	defer data.Clear()
	defer func() {
		if !data.CheckUtf8() && opts.block != 1 {
			return
		}
		err = writer.Write(data.GetData())
		tryThrowError(AccessDeniedException, err)
	}()

	err = reader.SetCursor(int64(opts.offset))
	tryThrowError(OutOfBoundsException, err)

	for processed < opts.limit {
		charSize := min(opts.block, opts.limit-processed)
		read, err := reader.Read(buffer[:charSize])
		if err == io.EOF {
			break
		}
		tryThrowError(AccessDeniedException, err)

		data.Push(buffer)
		processed += read
	}

	if opts.upper {
		data.ToUpperCase()
	} else if opts.lower {
		data.ToLowerCase()
	}
	if opts.trim {
		data.TrimSpaces()
	}
}

func main() {
	var (
		myReader Reader
		myWriter Writer
	)

	opts, err := ParseFlags()
	tryThrowError(WrongFlagException, err)

	myReader = initReader(opts)
	defer func() {
		err = myReader.Close()
		tryThrowError(CloseFileException, err)
	}()

	myWriter = initWriter(opts)
	defer func() {
		err = myWriter.Close()
		tryThrowError(CloseFileException, err)
	}()

	processFile(myReader, myWriter, opts)
}

// ПРОВЕРКА В ОДНОМ ФАЙЛЕ ТЕСТОВ

type Reader interface {
	Read(p []byte) (n int, err error)
	SetCursor(ind int64) error
	Close() error
}

type Writer interface {
	Write(p []byte) error
	Close() error
}

type Data interface {
	Push([]byte)
	Clear()
	GetData() []byte
	CheckUtf8() bool
	TrimSpaces()
	ToUpperCase()
	ToLowerCase()
}

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
	if f.file == nil || p == nil {
		return n, fmt.Errorf("WrongArgumentException")
	}
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

type StdReader struct {
}

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

type StdWriter struct {
}

func NewStdWriter() *StdWriter {
	return &StdWriter{}
}

func (s *StdWriter) Write(p []byte) error {
	_, err := os.Stdout.Write(p)
	return err
}

func (f *StdWriter) Close() error {
	return nil
}

type TextData struct {
	data []byte
}

func NewData() *TextData {
	return &TextData{data: make([]byte, 0)}
}

func (d *TextData) GetData() []byte {
	return d.data
}

func (d *TextData) Push(txt []byte) {
	d.data = append(d.data, txt...)
}

func (d *TextData) Clear() {
	d.data = make([]byte, 0)
}

func (d *TextData) CheckUtf8() bool {
	for i := 0; i < len(d.data); {
		r, size := utf8.DecodeRune(d.data[i:])
		if r == utf8.RuneError {
			return false
		}
		i += size
	}
	return true
}

func (d *TextData) TrimSpaces() {
	d.data = []byte(strings.TrimFunc(string(d.data), func(r rune) bool {
		return unicode.IsSpace(r)
	}))
}

func (d *TextData) ToUpperCase() {
	d.data = []byte(strings.ToUpper(string(d.data)))
}

func (d *TextData) ToLowerCase() {
	d.data = []byte(strings.ToLower(string(d.data)))
}
