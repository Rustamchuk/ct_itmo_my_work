package repository

import (
	"context"
	"fmt"
	"order_service/internal/model"
	"sync"
)

type OrderServiceRepository interface {
	CreateOrder(ctx context.Context, order model.Order)
	GetOrderByID(ctx context.Context, orderID int32) (model.Order, error)
	DeleteOrderByID(ctx context.Context, orderID int32)
}

type OrderServiceRepositoryImplementation struct {
	mutex     sync.Mutex
	ordersLib map[int32]model.Order
}

func NewOrderServiceRepositoryImplementation() OrderServiceRepository {
	return &OrderServiceRepositoryImplementation{
		mutex:     sync.Mutex{},
		ordersLib: make(map[int32]model.Order),
	}
}

func (o *OrderServiceRepositoryImplementation) CreateOrder(ctx context.Context, order model.Order) {
	o.mutex.Lock()
	defer o.mutex.Unlock()

	select {
	case <-ctx.Done():
		return
	default:
	}
	o.ordersLib[order.OrderID] = order
}

func (o *OrderServiceRepositoryImplementation) GetOrderByID(ctx context.Context, orderID int32) (model.Order, error) {
	o.mutex.Lock()
	defer o.mutex.Unlock()

	select {
	case <-ctx.Done():
		return model.Order{}, nil
	default:
	}
	if order, ok := o.ordersLib[orderID]; ok {
		return order, nil
	}
	return model.Order{}, fmt.Errorf("no such order with ID %v", orderID)
}

func (o *OrderServiceRepositoryImplementation) DeleteOrderByID(ctx context.Context, orderID int32) {
	o.mutex.Lock()
	defer o.mutex.Unlock()

	select {
	case <-ctx.Done():
		return
	default:
	}
	delete(o.ordersLib, orderID)
}
