package postgres

import (
	"context"
	"fmt"
	"homework/internal/domain"
	"homework/internal/usecase"

	"github.com/jackc/pgx/v5/pgxpool"
)

var usersTable = "users"

var (
	querySaveUser    = fmt.Sprintf("INSERT INTO %s (name) VALUES ($1)", usersTable)
	queryGetByUserID = fmt.Sprintf("SELECT id, name FROM %s WHERE id = $1", usersTable)
)

type UserRepository struct {
	pool *pgxpool.Pool
}

func NewUserRepository(pool *pgxpool.Pool) *UserRepository {
	return &UserRepository{
		pool: pool,
	}
}

func (r *UserRepository) SaveUser(ctx context.Context, user *domain.User) error {
	_, err := r.pool.Exec(ctx, querySaveUser, user.Name)
	return err
}

func (r *UserRepository) GetUserByID(ctx context.Context, id int64) (*domain.User, error) {
	row := r.pool.QueryRow(ctx, queryGetByUserID, id)
	var user domain.User
	err := row.Scan(&user.ID, &user.Name)
	if err != nil {
		return nil, usecase.ErrUserNotFound
	}
	return &user, nil
}
