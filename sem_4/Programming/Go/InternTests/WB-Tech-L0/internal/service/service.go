package service

import (
	"WB-Tech-L0/internal/model"
	"WB-Tech-L0/internal/repository"
	"context"
)

type Order interface {
	GetOrders(ctx context.Context) ([]model.Order, error)
	GetOrderByUID(ctx context.Context, orderUID string) (model.Order, error)
	AddOrder(ctx context.Context, order model.Order) error
}

type Service struct {
	Order
}

func NewService(repos repository.Repository) Service {
	return Service{Order: NewOrder(repos)}
}
