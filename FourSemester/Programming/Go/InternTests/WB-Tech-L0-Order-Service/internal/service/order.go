package service

import (
	"WB-Tech-L0/internal/model"
	"WB-Tech-L0/internal/repository"
	"context"
)

type order struct {
	repo repository.Repository
}

func NewOrder(repo repository.Repository) Order {
	return &order{repo: repo}
}

func (o *order) GetOrders(ctx context.Context) ([]model.Order, error) {
	return o.repo.GetOrders(ctx)
}

func (o *order) GetOrderByUID(ctx context.Context, orderUID string) (model.Order, error) {
	orderRes, ok := o.repo.Get(orderUID)
	if ok {
		return orderRes, nil
	}
	orderRes, err := o.repo.GetOrderByUID(ctx, orderUID)
	if err != nil {
		return model.Order{}, err
	}
	o.repo.Set(orderUID, orderRes)
	return orderRes, nil
}

func (o *order) AddOrder(ctx context.Context, order model.Order) error {
	err := o.repo.AddOrder(ctx, order)
	if err != nil {
		return err
	}
	o.repo.Set(order.UID, order)
	return nil
}
