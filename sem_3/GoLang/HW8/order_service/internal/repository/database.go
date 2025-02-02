package repository

import (
	"fmt"
	"github.com/jmoiron/sqlx"
	"order_service/internal/model"
	"sync"

	_ "github.com/mattn/go-sqlite3"
)

type OrderServiceDataBase interface {
	Connect() error
	GetOrders() ([]model.Order, error)
	InsertOrder(order model.Order) error
	DeleteOrderByID(orderID int32) error
}

type OrderServiceDataBaseImplementation struct {
	db    *sqlx.DB
	mutex sync.Mutex
}

const tableName = "BrokenOrders"

func NewOrderServiceDataBaseImplementation() OrderServiceDataBase {
	return &OrderServiceDataBaseImplementation{}
}

func (o *OrderServiceDataBaseImplementation) Connect() (err error) {
	o.db, err = sqlx.Open("sqlite3", "./orders.db")
	if err != nil {
		return
	}
	_, err = o.db.Exec(fmt.Sprintf(`
		CREATE TABLE IF NOT EXISTS %s (
			order_id INTEGER PRIMARY KEY,
			product_id INTEGER
		)
	`, tableName))
	return
}

func (o *OrderServiceDataBaseImplementation) GetOrders() ([]model.Order, error) {
	o.mutex.Lock()
	defer o.mutex.Unlock()
	var orders []model.Order
	err := o.db.Select(&orders, fmt.Sprintf("SELECT * FROM %s", tableName))
	return orders, err
}

func (o *OrderServiceDataBaseImplementation) InsertOrder(order model.Order) error {
	o.mutex.Lock()
	defer o.mutex.Unlock()
	_, err := o.db.Exec(
		fmt.Sprintf("INSERT INTO %s (order_id, product_id) VALUES (?, ?)", tableName),
		order.OrderID, order.ProductID,
	)
	return err
}

func (o *OrderServiceDataBaseImplementation) DeleteOrderByID(orderID int32) error {
	o.mutex.Lock()
	defer o.mutex.Unlock()
	_, err := o.db.Exec(fmt.Sprintf("DELETE FROM %s WHERE order_id = ?", tableName), orderID)
	return err
}
