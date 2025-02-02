package model

import (
	"context"
	"errors"
	"fmt"
	"sync"
)

func NewOrderFinishedExternalInteraction(orderProcessStarted OrderProcessStarted, storageID int32, pickupPointID int32, orderStates []OrderState, error error) *OrderFinishedExternalInteraction {
	return &OrderFinishedExternalInteraction{
		OrderProcessStarted: orderProcessStarted,
		StorageID:           storageID,
		PickupPointID:       pickupPointID,
		OrderStates:         orderStates,
		Error:               error,
	}
}

func (o *OrderFinishedExternalInteraction) PipelineFan(ctx context.Context, actions OrderActions, k int, orders <-chan OrderProcessStarted) <-chan OrderFinishedExternalInteraction {
	fanOutInteract := make([]<-chan OrderFinishedExternalInteraction, k)
	for i := 0; i < k; i++ {
		fanOutInteract[i] = o.process(ctx, actions, orders)
	}
	outCh := o.fanIn(fanOutInteract)
	return outCh
}

func (o *OrderFinishedExternalInteraction) process(ctx context.Context, actions OrderActions, orders <-chan OrderProcessStarted) <-chan OrderFinishedExternalInteraction {
	outCh := make(chan OrderFinishedExternalInteraction)
	go func() {
		defer close(outCh)
		for order := range orders {
			var storageID, pickupPointID int32 = 0, 0
			states := order.OrderStates
			err := order.Error
			if err == nil {
				storageID, pickupPointID, err = o.stateToState(actions, order.OrderInitialized.OrderID)
				if err == nil {
					states = append(order.OrderStates, FinishedExternalInteraction)
				}
			}
			select {
			case <-ctx.Done():
				return
			case outCh <- *NewOrderFinishedExternalInteraction(order, storageID, pickupPointID, states, err):
			}
		}
	}()
	return outCh
}

func (o *OrderFinishedExternalInteraction) fanIn(fanOutInteract []<-chan OrderFinishedExternalInteraction) <-chan OrderFinishedExternalInteraction {
	outCh := make(chan OrderFinishedExternalInteraction)
	wgFan := sync.WaitGroup{}
	for _, fanIn := range fanOutInteract {
		wgFan.Add(1)
		go func(fanIn <-chan OrderFinishedExternalInteraction) {
			defer wgFan.Done()
			for t := range fanIn {
				outCh <- t
			}
		}(fanIn)
	}
	go func() {
		wgFan.Wait()
		close(outCh)
	}()
	return outCh
}

func (o *OrderFinishedExternalInteraction) stateToState(actions OrderActions, orderID int32) (storageID, pickUpID int32, err error) {
	defer func() {
		if r := recover(); r != nil {
			err = errors.New(fmt.Sprint(r))
		}
	}()
	storageID, pickUpID = actions.StartedToFinishedExternalInteraction(orderID)
	return
}
