package inmemory

import (
	"context"
	"errors"
	"homework/internal/domain"
	"homework/internal/usecase"
	"sync"
)

var (
	ErrUserNotFound = errors.New("user not found")
	ErrNilUser      = errors.New("user is nil")
)

type UserRepository struct {
	libByUserID map[int64]domain.User
	mu          sync.Mutex
}

func NewUserRepository() *UserRepository {
	return &UserRepository{
		libByUserID: make(map[int64]domain.User),
	}
}

func (r *UserRepository) SaveUser(ctx context.Context, user *domain.User) error {
	if user == nil {
		return ErrNilUser
	}
	if err := ctx.Err(); err != nil {
		return err
	}
	r.mu.Lock()
	defer r.mu.Unlock()

	user.ID = int64(len(r.libByUserID) + 1)
	r.libByUserID[user.ID] = *user
	return nil
}

func (r *UserRepository) GetUserByID(ctx context.Context, id int64) (*domain.User, error) {
	if ctx.Err() != nil {
		return nil, ctx.Err()
	}
	r.mu.Lock()
	defer r.mu.Unlock()
	if user, ok := r.libByUserID[id]; ok {
		return &user, nil
	}
	return nil, usecase.ErrUserNotFound
}
