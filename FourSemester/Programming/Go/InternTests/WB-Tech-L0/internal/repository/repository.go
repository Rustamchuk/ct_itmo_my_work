package repository

import (
	"WB-Tech-L0/internal/repository/cache"
	"WB-Tech-L0/internal/repository/postgres"
	"github.com/jmoiron/sqlx"
)

type Repository struct {
	postgres.Order
	cache.Cache
}

func NewRepository(db *sqlx.DB) Repository {
	return Repository{
		Order: postgres.NewOrder(db),
		Cache: cache.NewCache(),
	}
}
