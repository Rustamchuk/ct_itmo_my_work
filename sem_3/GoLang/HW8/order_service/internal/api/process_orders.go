package api

import (
	"context"
	"order_service/internal/transform"
	"order_service/pkg/generated/proto/order_service"
)

func (o *OrderServiceApi) ProcessOrders(
	ctx context.Context,
	request *order_service.ProcessOrdersRequest,
) (*order_service.ProcessOrdersResponse, error) {
	select {
	case <-ctx.Done():
		return nil, nil
	default:
	}
	return o.service.ProcessOrders(ctx, transform.ProcessOrdersRequestToOrderInitialized(request))
}
