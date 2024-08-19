package main

import (
	"fmt"
	"reflect"
)

/*
Разработать программу, которая в рантайме способна определить
тип переменной: int, string, bool, channel из переменной типа interface{}.
*/

func determineType(v interface{}) { // Тип аргумента общий интерфейс, все типы имплементируют его
	switch v.(type) { // Получаем тип переменной
	case int: // Если типа int
		fmt.Println("Переменная имеет тип int", v)
	case string: // Если типа string
		fmt.Println("Переменная имеет тип string", v)
	case bool: // Если типа bool
		fmt.Println("Переменная имеет тип bool", v)
	case chan int: // Если типа chan int
		fmt.Println("Переменная имеет тип chan int", v)
	default: // Неизвестный
		fmt.Println("Неизвестный тип:", reflect.TypeOf(v))
	}
}

func main() {
	var myInt int = 42                   // myInt типа int со значением 42
	var myString string = "Привет"       // myString типа string со значением "Привет"
	var myBool bool = true               // myBool типа bool со значением true
	var myChan chan int = make(chan int) // myChan типа chan int со значением (пустой канал)

	determineType(myInt)
	determineType(myString)
	determineType(myBool)
	determineType(myChan)
	determineType(3.14) // Для демонстрации неизвестного типа
}
