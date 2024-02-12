package _defer

import (
	"fmt"
	"io"
)

type Counter struct {
	writer io.Writer
	value  int
}

func (c *Counter) Increment() {
	c.value++
}

func (c *Counter) Printer() func() {
	value := c.value
	printer := func() {
		_, _ = fmt.Fprint(c.writer, value)
	}
	return printer
}

func PrintSequence(writer io.Writer) {
	// TODO: 1345287960
	counter := Counter{writer, 0}
	defer counter.Printer()()
	counter.Increment()
	counter.Printer()()
	defer func() {
		counter.Increment()
		defer counter.Printer()()
		defer func() {
			counter.Increment()
			defer counter.Printer()()
		}()
		counter.Increment()
		defer counter.Printer()()
		counter.Increment()
		counter.Printer()()
	}()
	counter.Increment()
	defer counter.Printer()()
	counter.Increment()
	counter.Printer()()
	counter.Increment()
	counter.Printer()()
	counter.Increment()
	counter.Printer()()
}

func PrintSequenceLight(writer io.Writer) {
	// TODO: 135642
	counter := Counter{writer, 0}
	counter.Increment()
	counter.Printer()()
	counter.Increment()
	defer counter.Printer()()
	counter.Increment()
	counter.Printer()()
	counter.Increment()
	defer counter.Printer()()
	counter.Increment()
	counter.Printer()()
	counter.Increment()
	defer counter.Printer()()
}
