package adapters

import (
	"WB-Tech-L0/internal/gateways/http/api"
	"WB-Tech-L0/internal/model"
)

func OrderArrModelResponse(orders []model.Order) []api.Order {
	res := make([]api.Order, len(orders))
	for i, order := range orders {
		res[i] = OrderModelResponse(order)
	}
	return res
}

func OrderModelResponse(order model.Order) api.Order {
	delivery := api.Delivery{
		Name:    order.Delivery.Name,
		Phone:   order.Delivery.Phone,
		Zip:     order.Delivery.Zip,
		City:    order.Delivery.City,
		Address: order.Delivery.Address,
		Region:  order.Delivery.Region,
		Email:   order.Delivery.Email,
	}
	payment := api.Payment{
		Transaction:  order.Payment.Transaction,
		RequestID:    order.Payment.RequestID,
		Currency:     order.Payment.Currency,
		Provider:     order.Payment.Provider,
		Amount:       order.Payment.Amount,
		PaymentDT:    order.Payment.PaymentDT,
		Bank:         order.Payment.Bank,
		DeliveryCost: order.Payment.DeliveryCost,
		GoodsTotal:   order.Payment.GoodsTotal,
		CustomFee:    order.Payment.CustomFee,
	}
	items := make([]api.Item, len(order.Items))
	for i, item := range order.Items {
		items[i] = api.Item{
			ChrtID:      item.ChrtID,
			TrackNumber: item.TrackNumber,
			Price:       item.Price,
			RID:         item.RID,
			Name:        item.Name,
			Sale:        item.Sale,
			Size:        item.Size,
			TotalPrice:  item.TotalPrice,
			NmID:        item.NmID,
			Brand:       item.Brand,
			Status:      item.Status,
		}
	}
	res := api.Order{
		OrderUID:          order.UID,
		TrackNumber:       order.TrackNumber,
		Entry:             order.Entry,
		Delivery:          delivery,
		Payment:           payment,
		Items:             items,
		Locale:            order.Locale,
		InternalSignature: order.InternalSignature,
		CustomerID:        order.CustomerID,
		DeliveryService:   order.DeliveryService,
		ShardKey:          order.ShardKey,
		SmID:              order.SmID,
		DateCreated:       order.DateCreated,
		OofShard:          order.OofShard,
	}
	return res
}
