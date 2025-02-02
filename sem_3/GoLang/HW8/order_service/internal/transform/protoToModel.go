package transform

import (
	"order_service/internal/model"
	"order_service/pkg/generated/proto/order_service"
)

func ProcessOrdersRequestToOrderInitialized(
	request *order_service.ProcessOrdersRequest,
) []model.OrderInitialized {
	out := make([]model.OrderInitialized, len(request.Orders))
	for i, order := range request.Orders {
		out[i] = *model.NewOrderInitialized(order.OrderId, order.ProductId, nil)
	}
	return out
}

func SendOrderRequestToOrder(
	request *order_service.SendOrderDataCallbackRequest,
) model.Order {
	return model.Order{
		OrderID:       request.OrderId,
		StorageID:     request.StorageId,
		PickupPointID: request.PickupPointId,
	}
}
