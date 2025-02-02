package model

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
	SliceData(i, j int)
	Clear()
	GetData() []byte
	Size() int
	CheckUtf8() int
	TrimLeft()
	UnChangeTrimRight() int
	ToUpperCase()
	ToLowerCase()
}
