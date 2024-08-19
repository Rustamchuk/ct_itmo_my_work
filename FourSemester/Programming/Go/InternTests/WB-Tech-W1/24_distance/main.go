package main

import (
	"fmt"
	"math"
)

/*
Разработать программу нахождения расстояния
между двумя точками, которые представлены
в виде структуры Point
с инкапсулированными параметрами x,y и конструктором.
*/

// Point структура, представляющая точку на плоскости
type Point struct {
	x float64
	y float64
}

// NewPoint конструктор для создания новой точки
func NewPoint(x, y float64) *Point {
	return &Point{x: x, y: y}
}

// Distance метод для вычисления расстояния между двумя точками
func (p *Point) Distance(other *Point) float64 {
	// Формула Пифагора. Находим гипотенузу
	return math.Sqrt(math.Pow(p.x-other.x, 2) + math.Pow(p.y-other.y, 2))
}

func main() {
	point1 := NewPoint(1.0, 2.0)
	point2 := NewPoint(4.0, 6.0)

	distance := point1.Distance(point2)
	fmt.Printf("Расстояние между точками: %.2f\n", distance)
}
