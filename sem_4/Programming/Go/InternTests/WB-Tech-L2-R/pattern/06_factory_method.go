package pattern

import "fmt"

/*
	Реализовать паттерн «фабричный метод».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Factory_method_pattern
*/

/*
Применение:
	Фабрика используется, когда необходимо дать механизм создания экземпляров классов одной тематики структуре,
	которая отделит данную логику от перегруженного метода. Помогает упростить код.
Плюсы:
	Гибкость создания разновидностей объектов (Расширяемость).
	Изоляция классов, пользователь не знает реализации.
	Гибкость объектов, пользователь работает с интерфейсами, какие там классы неизвестно.
Минусы:
	Усложнение, множество классов.
Пример в работе:
	Например, к нам приходят запросы из двух разных платформ, и нам необходимо обрабатывать их по-отдельности.
	Фабрика поможет выбрать класс Хендлеров, подходящий под конкретную ситуацию.
	Пример:
*/

type OrderType string

var (
	web    OrderType = "web"
	mobile OrderType = "mobile"
)

// OrderHandler - интерфейс для обработчика заказов
type OrderHandler interface {
	HandleOrder(data string)
}

// WebOrderHandler - обработчик заказов из веб-интерфейса
type WebOrderHandler struct{}

func (w *WebOrderHandler) HandleOrder(data string) {
	fmt.Println("Обработка веб-заказа:", data)
}

// MobileOrderHandler - обработчик заказов из мобильного приложения
type MobileOrderHandler struct{}

func (m *MobileOrderHandler) HandleOrder(data string) {
	fmt.Println("Обработка мобильного заказа:", data)
}

// OrderHandlerFactory - фабрика для создания обработчиков заказов
type OrderHandlerFactory struct{}

func (f *OrderHandlerFactory) CreateOrderHandler(source OrderType) OrderHandler {
	switch source {
	case web:
		return &WebOrderHandler{}
	case mobile:
		return &MobileOrderHandler{}
	default:
		return nil
	}
}

func main() {
	factory := &OrderHandlerFactory{}
	webHandler := factory.CreateOrderHandler(web)
	mobileHandler := factory.CreateOrderHandler(mobile)

	webHandler.HandleOrder("Заказ №123")
	mobileHandler.HandleOrder("Заказ №456")
}
