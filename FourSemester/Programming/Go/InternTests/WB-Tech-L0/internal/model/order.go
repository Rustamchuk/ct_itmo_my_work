package model

import "time"

type Order struct {
	UID               string    `db:"uid"`
	TrackNumber       string    `db:"track_number"`
	Entry             string    `db:"entry"`
	Delivery          Delivery  `db:"-"`
	Payment           Payment   `db:"-"`
	Items             []Item    `db:"-"`
	Locale            string    `db:"locale"`
	InternalSignature string    `db:"internal_signature"`
	CustomerID        string    `db:"customer_id"`
	DeliveryService   string    `db:"delivery_service"`
	ShardKey          string    `db:"shard_key"`
	SmID              int       `db:"sm_id"`
	DateCreated       time.Time `db:"date_created"`
	OofShard          string    `db:"oof_shard"`
}

type Delivery struct {
	ID       int    `db:"id"`
	OrderUID string `db:"order_uid"`
	Name     string `db:"name"`
	Phone    string `db:"phone"`
	Zip      string `db:"zip"`
	City     string `db:"city"`
	Address  string `db:"address"`
	Region   string `db:"region"`
	Email    string `db:"email"`
}

type Payment struct {
	ID           int    `db:"id"`
	OrderUID     string `db:"order_uid"`
	Transaction  string `db:"transaction"`
	RequestID    string `db:"request_id"`
	Currency     string `db:"currency"`
	Provider     string `db:"provider"`
	Amount       int    `db:"amount"`
	PaymentDT    int    `db:"payment_dt"`
	Bank         string `db:"bank"`
	DeliveryCost int    `db:"delivery_cost"`
	GoodsTotal   int    `db:"goods_total"`
	CustomFee    int    `db:"custom_fee"`
}

type Item struct {
	ID          int    `db:"id"`
	OrderUID    string `db:"order_uid"`
	ChrtID      int    `db:"chrt_id"`
	TrackNumber string `db:"track_number"`
	Price       int    `db:"price"`
	RID         string `db:"rid"`
	Name        string `db:"name"`
	Sale        int    `db:"sale"`
	Size        string `db:"size"`
	TotalPrice  int    `db:"total_price"`
	NmID        int    `db:"nm_id"`
	Brand       string `db:"brand"`
	Status      int    `db:"status"`
}
