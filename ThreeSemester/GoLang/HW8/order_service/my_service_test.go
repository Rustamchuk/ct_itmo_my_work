package order_service

import (
	"context"
	"github.com/stretchr/testify/require"
	"log"
	"order_service/internal/model"
	"order_service/internal/repository"
	"order_service/internal/service"
	"order_service/pkg/generated/proto/order_service"
	"slices"
	"testing"
)

func TestBrokenDatabase(t *testing.T) {
	ctx := context.Background()
	orders := []model.OrderInitialized{
		*model.NewOrderInitialized(1, 1, nil),
		*model.NewOrderInitialized(2, 2, nil),
		*model.NewOrderInitialized(3, 3, nil),
		*model.NewOrderInitialized(4, 4, nil),
	}
	counter := 0
	orderActions := model.OrderActions{
		InitToStarted: func() {},
		StartedToFinishedExternalInteraction: func(orderID int32) (storageID, pickUpID int32) {
			counter++
			if counter < 5 {
				panic(nil)
			}
			return 1, 2
		},
		FinishedExternalInteractionToProcessFinished: func() {},
	}
	database := repository.NewOrderServiceDataBaseImplementation()
	err := database.Connect()
	if err != nil {
		log.Fatal(err)
	}
	orderService := service.NewOrderServiceImplementation(
		nil,
		repository.NewOrderServiceRepositoryImplementation(),
		database,
		orderActions,
	)
	processOrders, err := orderService.ProcessOrders(ctx, orders)
	require.NoError(t, err)
	require.Equal(t, 0, len(processOrders.ProcessedOrders))
	require.Equal(t, 4, counter)

	processOrders2, err2 := orderService.ProcessOrders(ctx, []model.OrderInitialized{})
	require.NoError(t, err2)
	require.Equal(t, 4, len(processOrders2.ProcessedOrders))
	require.Equal(t, 8, counter)

	slices.SortFunc(processOrders2.ProcessedOrders, func(a, b *order_service.ProcessOrdersResponse_OrderResponse) int {
		return int(a.OrderId - b.OrderId)
	})

	for i := 0; i < len(processOrders2.ProcessedOrders); i++ {
		responseOrder := processOrders2.ProcessedOrders[i]

		require.Equal(t, orders[i].OrderID, responseOrder.OrderId)
		require.Equal(t, orders[i].ProductID, responseOrder.ProductId)
		require.True(t, responseOrder.IsProcessed)
	}
}
