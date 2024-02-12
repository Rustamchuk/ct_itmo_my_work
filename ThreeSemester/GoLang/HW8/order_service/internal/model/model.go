package model

type OrderState string

const (
	Initialized                 OrderState = "order_initialized"
	ProcessStarted              OrderState = "order_process_started"
	FinishedExternalInteraction OrderState = "order_finished_external_interaction"
	ProcessFinished             OrderState = "order_process_finished"
)

type OrderActions struct {
	InitToStarted                                func()
	StartedToFinishedExternalInteraction         func(orderID int32) (int32, int32)
	FinishedExternalInteractionToProcessFinished func()
}

type OrderStates interface {
	OrderInitialized | OrderProcessStarted | OrderFinishedExternalInteraction | OrderProcessFinished
}

type OrderInitialized struct {
	OrderID     int32
	ProductID   int32
	OrderStates []OrderState
	Error       error
}

type OrderProcessStarted struct {
	OrderInitialized OrderInitialized
	OrderStates      []OrderState
	Error            error
}

type OrderFinishedExternalInteraction struct {
	OrderProcessStarted OrderProcessStarted
	StorageID           int32
	PickupPointID       int32
	OrderStates         []OrderState
	Error               error
}

type OrderProcessFinished struct {
	OrderFinishedExternalInteraction OrderFinishedExternalInteraction
	OrderStates                      []OrderState
	Error                            error
}

type Order struct {
	OrderID       int32        `db:"order_id"`
	ProductID     int32        `db:"product_id"`
	StorageID     int32        `db:"storage_id"`
	PickupPointID int32        `db:"pickup_point_id"`
	IsProcessed   bool         `db:"is_processed"`
	OrderStates   []OrderState `db:"order_states"`
}
