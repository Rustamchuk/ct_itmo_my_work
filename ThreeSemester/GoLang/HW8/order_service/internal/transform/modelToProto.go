package transform

import (
	"order_service/internal/model"
	"order_service/pkg/generated/proto/order_service"
)

func OrdersProcessedToOrdersResponse(
	orders []model.Order,
) *order_service.ProcessOrdersResponse {
	out := make([]*order_service.ProcessOrdersResponse_OrderResponse, len(orders))
	for i, order := range orders {
		out[i] = &order_service.ProcessOrdersResponse_OrderResponse{
			OrderId:       order.OrderID,
			ProductId:     order.ProductID,
			StorageId:     order.StorageID,
			PickupPointId: order.PickupPointID,
			IsProcessed:   order.IsProcessed,
		}
	}
	return &order_service.ProcessOrdersResponse{ProcessedOrders: out}
}
