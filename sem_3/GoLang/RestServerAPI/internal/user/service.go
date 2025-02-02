package user

import (
	"RestServerAPI/pkg/logging"
	"context"
)

type Service struct {
	storage Storage
	logger  *logging.Logger
}

func (s *Service) Create(ctx context.Context, dto CreateUserDTO) (User, error) {
	return User{}, nil
}
