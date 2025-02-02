package adapters

import (
	"context"
	"homework/internal/domain"
	"time"
)

//go:generate mockgen -source adapters.go -package adapters -destination adapters_mock.go

type EventUsecase interface {
	ReceiveEvent(ctx context.Context, event *domain.Event) error
	GetLastEventBySensorID(ctx context.Context, id int64) (*domain.Event, error)
	GetSensorHistory(ctx context.Context, id int64, st, fn time.Time) ([]domain.Event, error)
}

type SensorUsecase interface {
	RegisterSensor(ctx context.Context, sensor *domain.Sensor) (*domain.Sensor, error)
	GetSensors(ctx context.Context) ([]domain.Sensor, error)
	GetSensorByID(ctx context.Context, id int64) (*domain.Sensor, error)
}

type UserUsecase interface {
	RegisterUser(ctx context.Context, user *domain.User) (*domain.User, error)
	AttachSensorToUser(ctx context.Context, userID, sensorID int64) error
	GetUserSensors(ctx context.Context, userID int64) ([]domain.Sensor, error)
}
