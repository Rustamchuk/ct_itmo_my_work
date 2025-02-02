package main

import (
	"fmt"
	"math/big"
)

/*
Разработать программу, которая перемножает,
делит, складывает, вычитает две числовых переменных a,b,
значение которых > 2^20
*/

func main() {
	// Инициализация переменных a и b с большими значениями
	a := big.NewInt(1 << 21) // 2^21
	b := big.NewInt(1 << 22) // 2^22

	// Создание переменных для хранения результатов
	sum := big.NewInt(0)
	difference := big.NewInt(0)
	product := big.NewInt(0)
	quotient := big.NewInt(0)

	// Сложение
	sum.Add(a, b)

	// Вычитание
	difference.Sub(a, b)

	// Умножение
	product.Mul(a, b)

	// Деление
	quotient.Div(b, a)

	// Вывод результатов
	fmt.Println("a:", a, "b:", b)
	fmt.Println("Сумма:", sum)
	fmt.Println("Разность:", difference)
	fmt.Println("Произведение:", product)
	fmt.Println("Частное:", quotient)
}
