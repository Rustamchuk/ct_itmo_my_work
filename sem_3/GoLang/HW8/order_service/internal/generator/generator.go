package generator

import (
	"context"

	"order_service/internal/model"
)

type OrderGenerator interface {
	GenerateOrdersStream(ctx context.Context, orders []model.OrderInitialized) <-chan model.OrderInitialized
}

type OrderGeneratorImplementation struct{}

func NewOrderGeneratorImplementation() *OrderGeneratorImplementation {
	return &OrderGeneratorImplementation{}
}

func (o *OrderGeneratorImplementation) GenerateOrdersStream(
	ctx context.Context,
	orders []model.OrderInitialized,
) <-chan model.OrderInitialized {
	outCh := make(chan model.OrderInitialized)
	go func() {
		defer close(outCh)
		for _, order := range orders {
			select {
			case <-ctx.Done():
				return
			case outCh <- order:
			}
		}
	}()
	return outCh
}
