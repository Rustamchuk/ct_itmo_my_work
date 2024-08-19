package pattern

import "fmt"

/*
	Реализовать паттерн «стратегия».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Strategy_pattern
*/

/*
Применение:
	Стратегия подходит для ситуаций, когда есть некоторое количество алгоритмов обработки запроса,
	и есть необходимость в зависимости от входных данных быстро сменить процесс обработки.
	Есть множество алгоритмов, есть множество случаев, на каждый случай свой алгоритм обработки.
Плюсы:
	Гибкость в выборе алгоритмов. Легкое переключение.
	Изоляция реализации алгоритмов от бизнес логики.
	Расширяемость.
Минусы:
	Множество объектом. Один алгоритм одна структура.
	Клиенты должны знать о стратегиях.
Пример использования:
	Представим, у нас есть платежная система по карте или по крипте.
	Каждую оплату проводим по-разному, отсюда и алгоритмы разные.
	Пример снизу:
*/

// PaymentStrategy - интерфейс стратегии платежа
type PaymentStrategy interface {
	Pay(amount float64) string
}

// CryptoStrategy - стратегия для платежей через PayPal
type CryptoStrategy struct{}

func (p *CryptoStrategy) Pay(amount float64) string {
	return fmt.Sprintf("Paid %.2f using Crypto", amount)
}

// CreditCardStrategy - стратегия для платежей кредитной картой
type CreditCardStrategy struct{}

func (c *CreditCardStrategy) Pay(amount float64) string {
	return fmt.Sprintf("Paid %.2f using Credit Card", amount)
}

// PaymentContext - контекст, использующий стратегию
type PaymentContext struct {
	strategy PaymentStrategy
}

func (p *PaymentContext) SetStrategy(strategy PaymentStrategy) {
	p.strategy = strategy
}

func (p *PaymentContext) Pay(amount float64) string {
	return p.strategy.Pay(amount)
}

func main() {
	payment := &PaymentContext{}

	// Выбор стратегии Crypto
	payment.SetStrategy(&CryptoStrategy{})
	fmt.Println(payment.Pay(100.0))

	// Смена стратегии на кредитную карту
	payment.SetStrategy(&CreditCardStrategy{})
	fmt.Println(payment.Pay(150.0))
}
