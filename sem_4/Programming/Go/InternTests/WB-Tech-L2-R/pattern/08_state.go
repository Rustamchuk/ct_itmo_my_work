package pattern

import "fmt"

/*
	Реализовать паттерн «состояние».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/State_pattern
*/

/*
Применение:
	Состояние применяется во время обработки сервисом какого-то объекта (прим. Заказ), чтобы во время отладки или
	логирования быть в курсе, на каком этапе произошла ошибка. Так же упрощает логику обработки, так как
	работа делится на стадии, что помогает и в расширении в дальнейшем.
Плюсы:
	Деление на локальные поведения, состояния разделились в отдельные классы (изоляция).
	Упрощение кода.
	Большая гибкость в изменениях.
Минусы:
	Число классов.
	Сложность управления состояниями.
Опыт использования:
	У меня был проект, где приходит заказ, и обработка делится на три стадии: создание, работа с заказом, отправка в бд.
	Тут реализовал нечто похожее. У нас есть заказ, и его обработка делится на стадии.
	Пример снизу:
*/

// State interface
type State interface {
	ProcessOrder(order *OrderService)
}

// OrderService - контекст, который изменяет свое состояние
type OrderService struct {
	state State
}

func (o *OrderService) NextState() {
	o.state.ProcessOrder(o)
}

func (o *OrderService) SetState(state State) {
	o.state = state
	fmt.Println("Order state changed to", state)
}

// NewOrderState - состояние нового заказа
type NewOrderState struct{}

func (n *NewOrderState) ProcessOrder(order *OrderService) {
	fmt.Println("Processing new order")
	order.SetState(&ProcessedOrderState{})
}

// ProcessedOrderState - состояние обработанного заказа
type ProcessedOrderState struct{}

func (p *ProcessedOrderState) ProcessOrder(order *OrderService) {
	fmt.Println("Order is processed, ready for shipment")
	order.SetState(&ShippedOrderState{})
}

// ShippedOrderState - состояние отправленного заказа
type ShippedOrderState struct{}

func (s *ShippedOrderState) ProcessOrder(order *OrderService) {
	fmt.Println("Order has been shipped")
	order.SetState(&DeliveredOrderState{})
}

// DeliveredOrderState - состояние доставленного заказа
type DeliveredOrderState struct{}

func (d *DeliveredOrderState) ProcessOrder(order *OrderService) {
	fmt.Println("Order has been delivered")
	// Заказ доставлен, процесс завершен
}

func main() {
	orderService := &OrderService{state: &NewOrderState{}}
	orderService.NextState() // Обработка нового заказа
	orderService.NextState() // Заказ обработан, готов к отправке
	orderService.NextState() // Заказ отправлен
	orderService.NextState() // Заказ доставлен
}
