package main

import (
	"fmt"
	"os"
)

const (
	a = iota + 1
	_
	b
	c
)

func main() {
	fmt.Println(b)
}

func f() error {
	var err *os.PathError = nil
	return err
}
