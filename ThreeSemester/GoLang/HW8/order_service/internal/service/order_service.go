package service

import (
	"context"
	"order_service/internal/generator"
	"order_service/internal/model"
	"order_service/internal/repository"
	"order_service/internal/transform"
	"order_service/internal/workerpool"
	"order_service/pkg/generated/proto/data_manager"
	"order_service/pkg/generated/proto/order_service"
)

type OrderService interface {
	ProcessOrders(ctx context.Context, orders []model.OrderInitialized) (*order_service.ProcessOrdersResponse, error)
	SendOrderDataCallback(ctx context.Context, order model.Order)
}

type OrderServiceImplementation struct {
	repository   repository.OrderServiceRepository
	database     repository.OrderServiceDataBase
	dataManager  data_manager.DataManagerClient
	generator    generator.OrderGenerator
	workerPool   workerpool.OrderWorkerPool
	orderActions model.OrderActions
}

func NewOrderServiceImplementation(
	manager data_manager.DataManagerClient,
	repository repository.OrderServiceRepository,
	dataBase repository.OrderServiceDataBase,
	actions ...model.OrderActions,
) OrderService {
	orderService := &OrderServiceImplementation{
		repository:  repository,
		database:    dataBase,
		dataManager: manager,
		generator:   generator.NewOrderGeneratorImplementation(),
		workerPool:  workerpool.NewOrderWorkerPoolImplementation(),
	}
	if actions == nil {
		orderService.orderActions = orderService.getOrderActions(context.Background())
	} else {
		orderService.orderActions = actions[0]
	}
	return orderService
}

func (o *OrderServiceImplementation) ProcessOrders(
	ctx context.Context,
	orders []model.OrderInitialized,
) (*order_service.ProcessOrdersResponse, error) {
	orders, err := o.addBrokenOrders(orders)
	if err != nil {
		return nil, err
	}
	result := o.workerPool.StartWorkerPool(ctx, o.generator.GenerateOrdersStream(ctx, orders), o.orderActions, 10)
	ordersOut := make([]model.Order, 0)

	for orderFin := range result {
		order := o.finishedToOrder(orderFin)
		if !order.IsProcessed {
			o.repository.DeleteOrderByID(ctx, order.OrderID)
			err = o.database.InsertOrder(order)
			continue
		}
		o.repository.CreateOrder(ctx, order)
		ordersOut = append(ordersOut, order)
		err = o.database.DeleteOrderByID(order.OrderID)
	}
	return transform.OrdersProcessedToOrdersResponse(ordersOut), err
}

func (o *OrderServiceImplementation) SendOrderDataCallback(ctx context.Context, order model.Order) {
	o.repository.CreateOrder(ctx, order)
}

func (o *OrderServiceImplementation) addBrokenOrders(orders []model.OrderInitialized) ([]model.OrderInitialized, error) {
	brokenOrders, err := o.database.GetOrders()
	if err != nil {
		return orders, err
	}
	for _, order := range brokenOrders {
		orders = append(orders, *model.NewOrderInitialized(order.OrderID, order.ProductID, nil))
	}
	return orders, nil
}

func (o *OrderServiceImplementation) getOrderActions(ctx context.Context) model.OrderActions {
	initToStarted := func() {}
	startedToFinishedInteraction := func(orderID int32) (storageID int32, pickUpID int32) {
		_, err := o.dataManager.GetOrderDataCallback(ctx, &data_manager.GetOrderDataCallbackRequest{OrderId: orderID})
		if err != nil {
			panic(err)
		}
		order, err := o.repository.GetOrderByID(ctx, orderID)
		if err != nil {
			panic(err)
		}
		storageID, pickUpID = order.StorageID, order.PickupPointID
		return
	}
	finishedInteractionToFinished := func() {}
	return model.OrderActions{
		InitToStarted:                                initToStarted,
		StartedToFinishedExternalInteraction:         startedToFinishedInteraction,
		FinishedExternalInteractionToProcessFinished: finishedInteractionToFinished,
	}
}

func (o *OrderServiceImplementation) finishedToOrder(order model.OrderProcessFinished) model.Order {
	return model.Order{
		OrderID:       order.OrderFinishedExternalInteraction.OrderProcessStarted.OrderInitialized.OrderID,
		ProductID:     order.OrderFinishedExternalInteraction.OrderProcessStarted.OrderInitialized.ProductID,
		StorageID:     order.OrderFinishedExternalInteraction.StorageID,
		PickupPointID: order.OrderFinishedExternalInteraction.PickupPointID,
		IsProcessed:   order.Error == nil,
		OrderStates:   order.OrderStates,
	}
}
