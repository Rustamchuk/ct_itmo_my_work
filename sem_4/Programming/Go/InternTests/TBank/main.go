package main

import "fmt"

func main() {
	a := make(map[int]int)
	a[1] = 1
	b := a[1]
	c := a[2]
	fmt.Println(b)
	fmt.Println(c)
}
