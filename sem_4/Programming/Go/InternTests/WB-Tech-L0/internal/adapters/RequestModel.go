package adapters

import (
	"WB-Tech-L0/internal/gateways/http/api"
	"WB-Tech-L0/internal/model"
)

func OrderRequestModel(req api.Order) model.Order {
	delivery := model.Delivery{
		Name:    req.Delivery.Name,
		Phone:   req.Delivery.Phone,
		Zip:     req.Delivery.Zip,
		City:    req.Delivery.City,
		Address: req.Delivery.Address,
		Region:  req.Delivery.Region,
		Email:   req.Delivery.Email,
	}
	payment := model.Payment{
		Transaction:  req.Payment.Transaction,
		RequestID:    req.Payment.RequestID,
		Currency:     req.Payment.Currency,
		Provider:     req.Payment.Provider,
		Amount:       req.Payment.Amount,
		PaymentDT:    req.Payment.PaymentDT,
		Bank:         req.Payment.Bank,
		DeliveryCost: req.Payment.DeliveryCost,
		GoodsTotal:   req.Payment.GoodsTotal,
		CustomFee:    req.Payment.CustomFee,
	}
	items := make([]model.Item, len(req.Items))
	for i, item := range req.Items {
		items[i] = model.Item{
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
	order := model.Order{
		UID:               req.OrderUID,
		TrackNumber:       req.TrackNumber,
		Entry:             req.Entry,
		Delivery:          delivery,
		Payment:           payment,
		Items:             items,
		Locale:            req.Locale,
		InternalSignature: req.InternalSignature,
		CustomerID:        req.CustomerID,
		DeliveryService:   req.DeliveryService,
		ShardKey:          req.ShardKey,
		SmID:              req.SmID,
		DateCreated:       req.DateCreated,
		OofShard:          req.OofShard,
	}
	return order
}
