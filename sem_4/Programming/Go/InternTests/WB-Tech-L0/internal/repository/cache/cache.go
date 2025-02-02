package cache

import "WB-Tech-L0/internal/model"

type Order interface {
	Get(key string) (model.Order, bool)
	Set(key string, value model.Order)
}

type Cache struct {
	Order
}

func NewCache() Cache {
	return Cache{Order: NewOrder()}
}
