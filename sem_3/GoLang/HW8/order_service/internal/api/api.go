package api

import (
	"order_service/internal/service"
	"order_service/pkg/generated/proto/order_service"
)

type OrderServiceApi struct {
	service service.OrderService
	order_service.UnimplementedOrderServiceServer
}

func NewOrderServiceApi(service service.OrderService) *OrderServiceApi {
	return &OrderServiceApi{
		service:                         service,
		UnimplementedOrderServiceServer: order_service.UnimplementedOrderServiceServer{},
	}
}
