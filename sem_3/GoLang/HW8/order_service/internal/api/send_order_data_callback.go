package api

import (
	"context"
	"google.golang.org/protobuf/types/known/emptypb"
	"order_service/internal/transform"
	"order_service/pkg/generated/proto/order_service"
)

func (o *OrderServiceApi) SendOrderDataCallback(
	ctx context.Context,
	request *order_service.SendOrderDataCallbackRequest,
) (*emptypb.Empty, error) {
	select {
	case <-ctx.Done():
		return &emptypb.Empty{}, nil
	default:
	}
	o.service.SendOrderDataCallback(ctx, transform.SendOrderRequestToOrder(request))
	return &emptypb.Empty{}, nil
}
