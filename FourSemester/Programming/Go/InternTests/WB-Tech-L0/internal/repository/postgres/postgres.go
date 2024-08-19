package postgres

import (
	"WB-Tech-L0/internal/model"
	"context"
	"fmt"
	"github.com/jmoiron/sqlx"
	"log"
)

const (
	ordersTable     = "orders"
	deliveriesTable = "deliveries"
	paymentsTable   = "payments"
	itemsTable      = "items"
)

type Order interface {
	GetOrders(ctx context.Context) ([]model.Order, error)
	GetOrderByUID(ctx context.Context, orderUID string) (model.Order, error)
	AddOrder(ctx context.Context, order model.Order) error
}

type Config struct {
	Host     string
	Port     string
	Username string
	Password string
	DBName   string
	SSLMode  string
}

func NewPostgresDB(cfg Config) (*sqlx.DB, error) {
	db, err := sqlx.Open("postgres", fmt.Sprintf("host=%s port=%s user=%s dbname=%s sslmode=%s password=%s",
		cfg.Host, cfg.Port, cfg.Username, cfg.DBName, cfg.SSLMode, cfg.Password))
	log.Printf("Подключение к DB: host=%s port=%s user=%s dbname=%s sslmode=%s password=%s",
		cfg.Host, cfg.Port, cfg.Username, cfg.DBName, cfg.SSLMode, cfg.Password)
	if err != nil {
		return nil, err
	}

	err = db.Ping()
	if err != nil {
		return nil, err
	}

	return db, nil
}
