package cache

import (
	"WB-Tech-L0/internal/model"
	"sync"
)

type order struct {
	mutex sync.RWMutex
	data  map[string]model.Order
}

func NewOrder() Order {
	return &order{data: make(map[string]model.Order)}
}

func (c *order) Get(key string) (model.Order, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	value, ok := c.data[key]
	if !ok {
		return model.Order{}, false
	}
	return value, true
}

func (c *order) Set(key string, value model.Order) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	c.data[key] = value
}
