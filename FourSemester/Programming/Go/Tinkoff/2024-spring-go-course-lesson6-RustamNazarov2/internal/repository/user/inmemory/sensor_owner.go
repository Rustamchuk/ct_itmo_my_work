package inmemory

import (
	"context"
	"homework/internal/domain"
	"sync"
)

type SensorOwnerRepository struct {
	libByID map[int64][]domain.SensorOwner
	mu      sync.Mutex
}

func NewSensorOwnerRepository() *SensorOwnerRepository {
	return &SensorOwnerRepository{
		libByID: make(map[int64][]domain.SensorOwner),
	}
}

func (r *SensorOwnerRepository) SaveSensorOwner(ctx context.Context, sensorOwner domain.SensorOwner) error {
	r.mu.Lock()
	if _, ok := r.libByID[sensorOwner.UserID]; !ok {
		r.libByID[sensorOwner.UserID] = make([]domain.SensorOwner, 0)
	}
	r.libByID[sensorOwner.UserID] = append(r.libByID[sensorOwner.UserID], sensorOwner)
	r.mu.Unlock()
	return ctx.Err()
}

func (r *SensorOwnerRepository) GetSensorsByUserID(ctx context.Context, userID int64) ([]domain.SensorOwner, error) {
	r.mu.Lock()
	sensorOwner := r.libByID[userID]
	r.mu.Unlock()
	if sensorOwner == nil {
		sensorOwner = make([]domain.SensorOwner, 0)
	}
	return sensorOwner, ctx.Err()
}
