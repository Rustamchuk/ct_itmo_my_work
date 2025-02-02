package main

import "fmt"

/*
Дана структура Human (с произвольным набором полей и методов).
Реализовать встраивание методов в структуре Action от родительской структуры Human (аналог наследования).
*/

// Human Структура с двумя полями Name и Lastname
type Human struct {
	Name     string
	Lastname string
}

// Greet Метод от Human. Здороваемся и представляемся.
func (h *Human) Greet() {
	fmt.Println(fmt.Sprintf("Nice to meet you, my name is %s %s", h.Name, h.Lastname))
}

// Goodbye Метод от Human. Прощаемся.
func (h *Human) Goodbye() {
	fmt.Println("See you soon!")
}

/*
В Action встраиваем Human.
Action по сути думает, что поля и методы Human это его, определены будто бы в нем.
Тем самым Action имеет доступ по всем полям и методам Human.
*/

// Action структура с полем Present и встраиванием Human
type Action struct {
	Human
	Present string
}

// GiveGift метод Action. Дарим подарок.
func (a *Action) GiveGift() {
	fmt.Println(
		fmt.Sprintf("I want to give you a gift, please take this %s, I will be very pleased", a.Present))
}

func main() {
	// Создаю экземпляр Action
	action := Action{
		Human: Human{
			Name:     "Rustam",
			Lastname: "Nazarov",
		},
		Present: "airplane",
	}

	action.Greet()    // метод встроенной структуры Human
	action.GiveGift() // метод Action
	action.Goodbye()  // метод встроенной структуры Human
}
