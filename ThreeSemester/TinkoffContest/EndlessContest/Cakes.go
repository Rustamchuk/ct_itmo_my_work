package main

import (
	"fmt"
	"math"
)

func main() {
	var a int
	fmt.Scan(&a)
	fmt.Println(int(math.Ceil(math.Log2(float64(a)))))
}
