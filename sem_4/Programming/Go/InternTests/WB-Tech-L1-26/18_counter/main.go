package main

import (
	"fmt"
	"sync"
)

/*
Реализовать структуру-счетчик,
которая будет инкрементироваться в конкурентной среде.
По завершению программа должна выводить итоговое значение счетчика.
*/

// Counter структура для безопасного счетчика
type Counter struct {
	value int
	mu    sync.RWMutex // Чтобы читатели работали параллельно
}

// Increment увеличивает счетчик на 1
func (c *Counter) Increment() {
	c.mu.Lock() // Стоим в блокировке, пока не дойдет очередь до нас. Запрещаем остальным увеличивать счетчик
	defer c.mu.Unlock()
	c.value++
}

// Value возвращает текущее значение счетчика
func (c *Counter) Value() int {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return c.value
}

func main() {
	var wg sync.WaitGroup
	counter := Counter{}

	// Запускаем 1000 горутин, каждая из которых увеличивает счетчик на 1
	for i := 0; i < 1000; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			counter.Increment()
		}()
	}

	// Ожидаем завершения всех горутин
	wg.Wait()

	// Выводим итоговое значение счетчика
	fmt.Println("Итоговое значение счетчика:", counter.Value())
}
