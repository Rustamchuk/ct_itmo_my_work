package main

import (
	"fmt"
)

func main() {
	fmt.Println("Hello world!!!")
	//numbers := []int{10, 4, 14, 12, 100, 10, 76, 2, 6, 4, 1}
	//quickSort(numbers, 0, len(numbers)-1)
	//fmt.Println(numbers)
}

func quickSort(numbers []int, l int, r int) {
	if len(numbers) == 0 || l >= r {
		return
	}
	mid := (r + l) / 2
	sup := numbers[mid]
	i, j := l, r
	for i <= j {
		for numbers[i] < sup {
			i++
		}
		for numbers[j] > sup {
			j--
		}
		if i <= j {
			swap := numbers[i]
			numbers[i] = numbers[j]
			numbers[j] = swap
			i++
			j--
		}
	}
	quickSort(numbers, l, j)
	quickSort(numbers, i, r)
}

//Just exploring Golang
//
//func main() {
//	//hello := "Hello, world"
//	//var hello = "Hello, world"
//	//var hello string = "Hello, world"
//	//const hello string = "Hello, world"
//	//var str string //= ""
//	// int8 uint float bool string
//	// var a byte = 62 // = 'a'
//	// var a rune = 'a' ~char // = 62
//
//	fmt.Println("Hello, world")
//
//	fmt.Println(work("check"))
//
//	res, bl, _, err := checkNumber(10)
//	if err != nil {
//		//log.Fatal(err)
//		return
//	}
//	fmt.Printf("%s - %t\n", res, bl)
//	fmt.Println(findMin(10, 12, 3, 6, 8, 123, 76, 2, 1, 99, 32, 0))
//	arr := [4]int{1, 2, 3, 4} // arr не ссылочный
//	arr2 := []int{1, 2, 3, 4} // slice ссылочный
//	fmt.Println(arr)
//	arr2 = append(arr2, 2)
//	fmt.Println(arr2)
//}
//
//func work(message string) string {
//	a, b, c := 1, 2, 3
//	a, b = b, a
//	a, _, c = a, 0, c
//	result := fmt.Sprintf("%s, %d, %d, %d\n", message, a, b, c)
//	return result
//}
//
//func checkNumber(num int) (string, bool, int, error) {
//	if num%2 == 0 && num > 0 {
//		return "Even", true, num, nil
//	}
//	return "Odd", false, num, errors.New("It is Odd!")
//}
//
//func findMin(numbers ...int) int {
//	if len(numbers) == 0 {
//		return 0
//	}
//	var min int = numbers[0]
//	for i := 1; i < len(numbers); i++ {
//		if min > numbers[i] {
//			min = numbers[i]
//		}
//	}
//	return min
//}
