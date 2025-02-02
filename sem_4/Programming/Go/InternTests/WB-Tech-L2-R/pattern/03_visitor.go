package pattern

import (
	"fmt"
	"github.com/nats-io/stan.go"
)

/*
	Реализовать паттерн «посетитель».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Visitor_pattern
*/

/*
Применение:
	Посетитель применяется, когда необходимо выполнять похожие операции над разными элементами, не изменяя их.
	Например, проходиться итеративно по заказам и каждый раз отправлять сигнал об этом.
	В итоге мы получаем отдельную структуру, которая делает похожие действия с разными структурами.
	Выделяем повторяющееся действие (отправка сигнала) в отдельную структуру для удобства работы с ней и простоты кода.
Плюсы:
	Простота добавление новых операций над элементами, не надо менять/добавлять в каждом элементе.
	Собирает связанные операции и разделяет несвязанные.
	Убирает копипасту похожих действий в коде, выделяя в отдельное место
Минусы:
	Уязвимость. Приходиться давать доступ к объектам, которые не должны быть доступны
	Нарушение принципа открытости/закрытости. Когда мы добавляем новую структуру в посетителя,
	приходится модифицировать все сущности.
Пример на практике:
	Например у нас есть события в сервисе и мы хотим отправлять по NATS-STREAMING эти события,
	тогда идеально подойдет Посетитель, получая событие мы будем отправлять его в Посетителя,
	который занимается отправкой сигналов.
*/

// Element interface
type Event interface {
	Accept(v EventVisitor)
}

// Elements
type OrderPlaced struct {
	orderID string
}

func (o *OrderPlaced) Accept(v EventVisitor) {
	v.VisitOrderPlaced(o)
}

type UserRegistered struct {
	userID string
}

func (u *UserRegistered) Accept(v EventVisitor) {
	v.VisitUserRegistered(u)
}

// Visitor interface
type EventVisitor interface {
	VisitOrderPlaced(o *OrderPlaced)
	VisitUserRegistered(u *UserRegistered)
}

// Visitor
type NATSVisitor struct {
	conn stan.Conn
}

func NewNATSVisitor(clusterID, clientID, natsURL string) (*NATSVisitor, error) {
	conn, err := stan.Connect(clusterID, clientID, stan.NatsURL(natsURL))
	if err != nil {
		return nil, err
	}
	return &NATSVisitor{conn: conn}, nil
}

func (n *NATSVisitor) VisitOrderPlaced(o *OrderPlaced) {
	message := fmt.Sprintf("Order placed with ID: %s", o.orderID)
	n.conn.Publish("orderEvents", []byte(message))
	fmt.Println("Sent to NATS:", message)
}

func (n *NATSVisitor) VisitUserRegistered(u *UserRegistered) {
	message := fmt.Sprintf("User registered with ID: %s", u.userID)
	n.conn.Publish("userEvents", []byte(message))
	fmt.Println("Sent to NATS:", message)
}

func main() {
	visitor, err := NewNATSVisitor("test-cluster", "client-123", "nats://localhost:4222")
	if err != nil {
		fmt.Println("Error connecting to NATS:", err)
		return
	}
	defer visitor.conn.Close()

	orderEvent := &OrderPlaced{orderID: "12345"}
	userEvent := &UserRegistered{userID: "user123"}

	orderEvent.Accept(visitor)
	userEvent.Accept(visitor)
}
