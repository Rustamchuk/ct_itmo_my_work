package main

import "fmt"

/*
Дана переменная int64. Разработать программу,
которая устанавливает i-й бит в 1 или 0.
*/

// setBit устанавливает i-й бит числа num в значение val (1 или 0)
func setBit(num int64, i uint, val int) int64 {
	/*
		[1 << i] - ставим 1 бит в i позицию - [0..010..0]
		^[1 << i] - реверсируем, единицы в ноль и наоборот - [1..101..1]
		num & ^[1 << i] - обнуляем i бит в num
		num | [val << i] - ставим на i позицию необходимое значение
	*/
	num &^= 1 << i
	num |= int64(val) << i
	return num
}

func main() {
	var num int64
	var i uint
	var val int

	fmt.Print("Введите число: ")
	fmt.Scan(&num)

	fmt.Print("Введите номер бита для установки (0 индексация): ")
	fmt.Scan(&i)
	if i > 63 { // int64 только 64 бита есть
		fmt.Println("number must be less 64")
	}

	fmt.Print("Введите значение бита (0 или 1): ")
	fmt.Scan(&val)

	if val != 0 && val != 1 {
		fmt.Println("Значение бита должно быть 0 или 1")
		return
	}

	result := setBit(num, i, val)
	fmt.Printf("Результат: %d\n", result)
}
