package main

import (
	"fmt"
	"sync"
)

/*
Реализовать конкурентную запись данных в map.
*/

// SafeMap структура для безопасной работы с map
type SafeMap struct {
	mu   sync.RWMutex // Позволит разделить блокировку для Read и Write операций, чтобы читатели работали параллельно
	data map[string]int
}

// NewSafeMap создает новый экземпляр SafeMap
func NewSafeMap() *SafeMap {
	return &SafeMap{
		data: make(map[string]int),
	}
}

// Set устанавливает значение в map с блокировкой для безопасной записи
func (sm *SafeMap) Set(key string, value int) {
	sm.mu.Lock()
	defer sm.mu.Unlock()
	sm.data[key] = value
}

// Get возвращает значение из map с R блокировкой для безопасного чтения
func (sm *SafeMap) Get(key string) (int, bool) {
	sm.mu.RLock()
	defer sm.mu.RUnlock()
	value, exists := sm.data[key]
	return value, exists
}

func main() {
	sm := NewSafeMap()

	// Создаем несколько горутин для демонстрации конкурентной записи
	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			key := fmt.Sprintf("key%d", i%5) // 5 уникальных ключей, потом повторяемся
			sm.Set(key, i)
			if value, exists := sm.Get(key); exists {
				fmt.Printf("Key: %s, Value: %d\n", key, value)
			}
		}(i)
	}

	wg.Wait() // Ожидаем завершения всех горутин
}
