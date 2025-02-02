package pattern

import "fmt"

/*
	Реализовать паттерн «цепочка вызовов».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern
*/

/*
Применение:
	Цепочка вызовов позволяет без жесткой связки запроса к конкретному обработчику обрабатывать запрос объектами.
	Используется, когда запрос подойдет для одного обработчика из кепочки нескольких.
	В итоге, перебирая обработчики, находится подходящий и происходит обработка.
Плюсы:
	Снижение зависимостей между компонентами.
	Гибкость в назначениях, можно легко изменять.
	Распределение обязанностей по нескольким объектам.
Минусы:
	Нет гарантий обработки запросов, вдруг нет подходящего обработчика.
	Увеличение времени обработки из-за поиска подходящего.
	Сложность отладки, сложно следить, кто именно обрабатывает.
Сфера применения:
	Удобен в случае, когда операции делятся на Пользовательские и Админские.
	В итоге добавляем цепочку из двух обработчиков, и каждый запрос ищет подходящий и выполняется в нем.
	Пример снизу:
*/

// Handler interface
type Handler interface {
	SetNext(handler Handler) Handler
	Handle(request string) bool
}

// BaseHandler - базовый класс обработчика
type BaseHandler struct {
	next Handler
}

func (h *BaseHandler) SetNext(handler Handler) Handler {
	h.next = handler
	return handler
}

func (h *BaseHandler) Handle(request string) bool {
	if h.next != nil {
		return h.next.Handle(request)
	}
	return false
}

// Handler1 - обработчик
type AdminHandler struct {
	BaseHandler
}

func (h *AdminHandler) Handle(request string) bool {
	if request == "admin" {
		fmt.Println("Admin access granted.")
		return true
	}
	return h.BaseHandler.Handle(request)
}

// Handler2 - обработчик
type UserHandler struct {
	BaseHandler
}

func (h *UserHandler) Handle(request string) bool {
	if request == "user" {
		fmt.Println("User access granted.")
		return true
	}
	return h.BaseHandler.Handle(request)
}

func main() {
	adminHandler := &AdminHandler{}
	userHandler := &UserHandler{}

	adminHandler.SetNext(userHandler)

	// Попытка доступа
	if !adminHandler.Handle("user") {
		fmt.Println("Access denied.")
	}
	if !adminHandler.Handle("admin") {
		fmt.Println("Access denied.")
	}
}
