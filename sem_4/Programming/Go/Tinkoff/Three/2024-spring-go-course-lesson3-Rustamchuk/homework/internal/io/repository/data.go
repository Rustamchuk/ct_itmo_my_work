package repository

import (
	"strings"
	"unicode"
	"unicode/utf8"
)

type TextData struct {
	data []byte
}

func NewData() *TextData {
	return &TextData{data: make([]byte, 0)}
}

func (d *TextData) GetData() []byte {
	return d.data
}

func (d *TextData) Size() int {
	return len(d.data)
}

func (d *TextData) Push(txt []byte) {
	d.data = append(d.data, txt...)
}

func (d *TextData) SliceData(i, j int) {
	if i == -1 {
		d.data = d.data[:j]
	}
	if j == -1 {
		d.data = d.data[i:]
	}
	j = min(len(d.data), j)
	d.data = d.data[i:j]
}

func (d *TextData) Clear() {
	d.data = make([]byte, 0)
}

func (d *TextData) CheckUtf8() int {
	for i := 0; i < len(d.data); {
		r, size := utf8.DecodeRune(d.data[i:])
		if r == utf8.RuneError {
			return i
		}
		i += size
	}
	return -1
}

func (d *TextData) TrimLeft() {
	d.data = []byte(strings.TrimLeftFunc(string(d.data), unicode.IsSpace))
}

func (d *TextData) UnChangeTrimRight() int {
	for i := len(d.data); i > 0; {
		r, size := utf8.DecodeLastRune(d.data[:i])
		if !unicode.IsSpace(r) {
			return i
		}
		i -= size
	}
	return 0
}

func (d *TextData) ToUpperCase() {
	d.data = []byte(strings.ToUpper(string(d.data)))
}

func (d *TextData) ToLowerCase() {
	d.data = []byte(strings.ToLower(string(d.data)))
}
