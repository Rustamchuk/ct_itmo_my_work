package main

import (
	"errors"
	"flag"
	"fmt"
	"io"
	"lecture03_homework/internal/io/model"
	"lecture03_homework/internal/io/reader"
	"lecture03_homework/internal/io/repository"
	"lecture03_homework/internal/io/writer"
	"math"
	"os"
	"strings"
)

var (
	OpenFileException     = errors.New("can not open file")
	InitReaderException   = errors.New("can not init read file")
	InitWriterException   = errors.New("can not init write file")
	CloseFileException    = errors.New("can not close file")
	AccessDeniedException = errors.New("no access for operation")
	WrongFlagException    = errors.New("can not parse flag")
	OutOfBoundsException  = errors.New("argument is out of bounds")
	FinishWriteException  = errors.New("can not write data in file")
	ProcessCopyException  = errors.New("can not process file work")
)

const MaxOutEncodeCharSize = 3

type Options struct {
	From   string
	To     string
	offset int
	limit  int
	block  int
	upper  bool
	lower  bool
	trimL  bool
	trimR  bool
}

func ParseFlags() (*Options, error) {
	var (
		opts Options
		conv string
	)

	flag.StringVar(&opts.From, "from", "", "file to read. by default - stdin")
	flag.StringVar(&opts.To, "to", "", "file to write. by default - stdout")
	flag.IntVar(&opts.offset, "offset", 0, "skip bytes. by default - 0")
	flag.IntVar(&opts.limit, "limit", math.MaxInt-3*MaxOutEncodeCharSize, "max bytes. by default - MaxInt")
	flag.IntVar(&opts.block, "block-size", 1024, "block size bytes. by default - 1024")
	flag.StringVar(&conv, "conv", "", "upper_case, lower_case, trim_spaces")

	flag.Parse()

	if opts.offset < 0 {
		return nil, fmt.Errorf("invalid offset")
	}

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
			opts.trimL = true
			opts.trimR = true
		default:
			return nil, fmt.Errorf("wrong conv key")
		}
	}
	if opts.lower && opts.upper {
		return nil, fmt.Errorf("impossible upper and lower at one time")
	}

	return &opts, nil
}

func tryThrowError(message, err error) {
	if err != nil {
		_, _ = fmt.Fprintln(os.Stderr, message, err)
		os.Exit(1)
	}
}

func initReader(opts *Options) (model.Reader, error) {
	if opts.From == "" {
		return reader.NewStdReader(), nil
	}
	myReader, err := reader.NewFileReader(opts.From)
	if err != nil {
		return nil, errors.Join(InitReaderException, err)
	}
	return myReader, nil
}

func initWriter(opts *Options) (model.Writer, error) {
	if opts.To == "" {
		return writer.NewStdWriter(), nil
	}
	myWriter, err := writer.NewFileWriter(opts.To)
	if err != nil {
		return nil, errors.Join(InitWriterException, err)
	}
	return myWriter, nil
}

func normaliseSlice(data model.Data, read, sliceL, sliceR int) (int, int) {
	brokenByte := data.CheckUtf8()
	if brokenByte != -1 {
		if brokenByte >= read-3 {
			sliceR = brokenByte
			return sliceL, sliceR
		}
	}
	i := 0
	for brokenByte != -1 && brokenByte+i < sliceL {
		i++
		data.SliceData(i, -1)
		brokenByte = data.CheckUtf8()
	}
	sliceL -= i

	if brokenByte != -1 {
		if brokenByte >= read-3 {
			sliceR = brokenByte
		}
	}
	return sliceL, sliceR
}

func processTrim(opts *Options, data model.Data, sliceR int) int {
	if opts.trimL {
		data.TrimLeft()
		if data.Size() != 0 {
			if data.CheckUtf8() != -1 {
				return 0
			}
			opts.trimL = false
		}
		sliceR -= sliceR - data.Size()
	}
	if opts.trimR {
		sliceR = min(data.UnChangeTrimRight(), sliceR)
	}
	return sliceR
}

func processFile(reader model.Reader, writer model.Writer, opts *Options) error {
	var (
		data      = repository.NewData()
		buffer    = make([]byte, opts.block)
		processed int
		utfOffset int
		utfSuffix int
		utfLimit  int
		sliceL    int
		sliceR    int
		pastBytes int
		err       error
	)

	utfOffset = max(opts.offset-MaxOutEncodeCharSize, 0)
	sliceL = opts.offset - utfOffset
	utfLimit = opts.limit + MaxOutEncodeCharSize + sliceL
	utfSuffix = utfLimit - opts.limit - (opts.offset - utfOffset)

	err = reader.SetCursor(int64(utfOffset))
	if err != nil {
		return errors.Join(OutOfBoundsException, err)
	}

	for processed < utfLimit {
		charSize := min(opts.block, utfLimit-processed)
		read, errR := reader.Read(buffer[:charSize])
		if errR != nil {
			if errors.Is(errR, io.EOF) {
				break
			}
			return errors.Join(AccessDeniedException, errR)
		}
		processed += read

		data.Push(buffer[:read])
		pastBytes = data.Size() - read
		read = data.Size()
		sliceR = read

		if processed == utfLimit {
			sliceR = read - utfSuffix
			data.SliceData(0, sliceR)
		} else {
			sliceL, sliceR = normaliseSlice(data, read, sliceL, sliceR)
		}

		if sliceL > 0 && sliceL < data.Size() {
			data.SliceData(sliceL, data.Size())
			sliceL = 0
		}
		sliceR = processTrim(opts, data, sliceR)

		if sliceR == 0 {
			continue
		}

		data.SliceData(sliceL, sliceR)

		if opts.upper {
			data.ToUpperCase()
		} else if opts.lower {
			data.ToLowerCase()
		}

		err = writer.Write(data.GetData())
		if err != nil {
			return errors.Join(err, FinishWriteException, err)
		}
		data.Clear()
		data.Push(buffer[sliceR-pastBytes:])
		sliceL = 0
	}

	return nil
}

func main() {
	var (
		myReader model.Reader
		myWriter model.Writer
	)

	opts, err := ParseFlags()
	tryThrowError(WrongFlagException, err)

	myReader, err = initReader(opts)
	tryThrowError(OpenFileException, err)
	defer func() {
		err = myReader.Close()
		tryThrowError(CloseFileException, err)
	}()

	myWriter, err = initWriter(opts)
	tryThrowError(OpenFileException, err)
	defer func() {
		err = myWriter.Close()
		tryThrowError(CloseFileException, err)
	}()

	err = processFile(myReader, myWriter, opts)
	tryThrowError(ProcessCopyException, err)
}
