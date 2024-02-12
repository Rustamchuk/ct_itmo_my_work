package model

func NewOrderInitialized(orderID int32, productID int32, error error) *OrderInitialized {
	return &OrderInitialized{
		OrderID:     orderID,
		ProductID:   productID,
		OrderStates: []OrderState{Initialized},
		Error:       error,
	}
}
