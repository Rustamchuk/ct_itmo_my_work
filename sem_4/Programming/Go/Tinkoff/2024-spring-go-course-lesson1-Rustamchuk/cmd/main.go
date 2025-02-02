package main

import (
	"fmt"
	"github.com/central-university-dev/2024-spring-go-course-lesson1/fizzbuzz"
)

// import "github.com/central-university-dev/2024-spring-go-course-lesson1/fizzbuzz"

func main() {
	for i := 1; i < 100; i++ {
		fmt.Println(fizzbuzz.FizzBuzz(i))
	}
}
