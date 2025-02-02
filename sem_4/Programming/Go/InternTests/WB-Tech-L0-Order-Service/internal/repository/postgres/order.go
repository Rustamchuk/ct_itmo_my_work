package postgres

import (
	"WB-Tech-L0/internal/model"
	"context"
	"fmt"
	"github.com/jmoiron/sqlx"
)

var (
	queryGetOrders     = fmt.Sprintf(`SELECT * FROM %s`, ordersTable)
	queryGetOrderByUID = fmt.Sprintf(`SELECT * FROM %s WHERE uid = $1`, ordersTable)
	queryAddOrder      = fmt.Sprintf(`
        INSERT INTO %s (
            uid, track_number, entry, locale, internal_signature, 
            customer_id, delivery_service, shard_key, sm_id, 
            date_created, oof_shard
        ) VALUES (
            $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
        )
    `, ordersTable)
)

var (
	queryGetDelivery = fmt.Sprintf(`SELECT * FROM %s WHERE order_uid = $1`, deliveriesTable)
	queryAddDelivery = fmt.Sprintf(`
        INSERT INTO %s (
            order_uid, name, phone, zip, city, address, 
            region, email
        ) VALUES (
            $1, $2, $3, $4, $5, $6, $7, $8
        )
    `, deliveriesTable)
)

var (
	queryGetPayment = fmt.Sprintf(`SELECT * FROM %s WHERE order_uid = $1`, paymentsTable)
	queryAddPayment = fmt.Sprintf(`
        INSERT INTO %s (
            order_uid, transaction, request_id, currency, provider, 
            amount, payment_dt, bank, delivery_cost, goods_total, custom_fee
        ) VALUES (
            $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
        )
    `, paymentsTable)
)

var (
	queryGetItems = fmt.Sprintf(`SELECT * FROM %s WHERE order_uid = $1`, itemsTable)
	queryAddItem  = fmt.Sprintf(`
        INSERT INTO %s (
            order_uid, chrt_id, track_number, price, rid, name, 
            sale, size, total_price, nm_id, brand, status
        ) VALUES (
            $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12
        )
    `, itemsTable)
)

type order struct {
	db *sqlx.DB
}

func NewOrder(db *sqlx.DB) Order {
	return &order{db: db}
}

func (o *order) GetOrders(ctx context.Context) ([]model.Order, error) {
	var orders []model.Order

	if err := o.db.SelectContext(ctx, &orders, queryGetOrders); err != nil {
		return nil, err
	}

	for i, order := range orders {
		var err error
		orders[i].Delivery, orders[i].Payment, orders[i].Items, err = o.GetDataByUID(ctx, order.UID)
		if err != nil {
			return nil, err
		}
	}

	return orders, nil
}

func (o *order) GetOrderByUID(ctx context.Context, orderUID string) (model.Order, error) {
	var order model.Order

	if err := o.db.GetContext(ctx, &order, queryGetOrderByUID, orderUID); err != nil {
		return model.Order{}, err
	}

	var err error
	order.Delivery, order.Payment, order.Items, err = o.GetDataByUID(ctx, order.UID)
	if err != nil {
		return model.Order{}, err
	}

	return order, nil
}

func (o *order) AddOrder(ctx context.Context, order model.Order) error {
	tx, err := o.db.BeginTxx(ctx, nil)
	if err != nil {
		return err
	}

	_, err = tx.ExecContext(ctx, queryAddOrder,
		order.UID, order.TrackNumber, order.Entry, order.Locale, order.InternalSignature,
		order.CustomerID, order.DeliveryService, order.ShardKey, order.SmID, order.DateCreated, order.OofShard)
	if err != nil {
		tx.Rollback()
		return err
	}

	_, err = tx.ExecContext(ctx, queryAddDelivery,
		order.UID, order.Delivery.Name, order.Delivery.Phone, order.Delivery.Zip,
		order.Delivery.City, order.Delivery.Address, order.Delivery.Region, order.Delivery.Email)
	if err != nil {
		tx.Rollback()
		return err
	}

	_, err = tx.ExecContext(ctx, queryAddPayment,
		order.UID, order.Payment.Transaction, order.Payment.RequestID, order.Payment.Currency,
		order.Payment.Provider, order.Payment.Amount, order.Payment.PaymentDT, order.Payment.Bank,
		order.Payment.DeliveryCost, order.Payment.GoodsTotal, order.Payment.CustomFee)
	if err != nil {
		tx.Rollback()
		return err
	}

	for _, item := range order.Items {
		_, err = tx.ExecContext(ctx, queryAddItem, order.UID,
			item.ChrtID, item.TrackNumber, item.Price, item.RID, item.Name, item.Sale,
			item.Size, item.TotalPrice, item.NmID, item.Brand, item.Status)
		if err != nil {
			tx.Rollback()
			return err
		}
	}

	if err := tx.Commit(); err != nil {
		tx.Rollback()
		return err
	}

	return nil
}

func (o *order) GetDataByUID(ctx context.Context, uid string) (delivery model.Delivery, payment model.Payment, items []model.Item, err error) {
	if err = o.db.GetContext(ctx, &delivery, queryGetDelivery, uid); err != nil {
		return
	}
	if err = o.db.GetContext(ctx, &payment, queryGetPayment, uid); err != nil {
		return
	}
	err = o.db.SelectContext(ctx, &items, queryGetItems, uid)
	return
}
