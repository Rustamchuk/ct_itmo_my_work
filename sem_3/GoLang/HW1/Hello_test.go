package main

import (
	"reflect"
	"testing"
)

func TestQuickSort(t *testing.T) {
	numbers := []int{10, 4, 14, 12, 100, 10, 76, 2, 6, 4, 1}
	numbersSorted := []int{1, 2, 4, 4, 6, 10, 10, 12, 14, 76, 100}
	quickSort(numbers, 0, len(numbers)-1)
	if !reflect.DeepEqual(numbers, numbersSorted) {
		t.Errorf("Wrong sorted slice. \nExpected: %v\nActual: %v\n", numbersSorted, numbers)
	}
}

//HARD TESTS
//
//import (
//	"fmt"
//	"math/rand"
//	"reflect"
//	"sort"
//	"testing"
//)
//
//func Test(t *testing.T) {
//	var numbers []int
//	var numbersSorted []int
//	quickSort(numbers, 0, len(numbers)-1)
//	for i := 0; i < 10; i++ {
//		t.Run(fmt.Sprintf("Test %d", i+1), func(t *testing.T) {})
//		{
//			size := rand.Intn(20)
//			for i := 0; i < size; i++ {
//				numbers = append(numbers, rand.Intn(50))
//				numbersSorted = append(numbersSorted, numbers[i])
//			}
//			quickSort(numbers, 0, size-1)
//			sort.Slice(numbersSorted, func(i int, j int) bool { return numbersSorted[i] < numbersSorted[j] })
//			if !reflect.DeepEqual(numbers, numbersSorted) {
//				t.Errorf("Wrong sorted slice. \nExpected: %v\nActual: %v\n", numbersSorted, numbers)
//			}
//			numbers, numbersSorted = nil, nil
//		}
//	}
//}
