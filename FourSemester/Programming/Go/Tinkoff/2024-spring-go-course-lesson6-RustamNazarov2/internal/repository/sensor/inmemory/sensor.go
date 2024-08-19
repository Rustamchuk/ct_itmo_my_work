package inmemory

import (
	"context"
	"errors"
	"homework/internal/domain"
	"homework/internal/usecase"
	"sync"
	"time"
)

var (
	ErrSensorNotFound = errors.New("sensor not found")
	ErrNilSensor      = errors.New("sensor is nil")
)

type SensorRepository struct {
	libBySensorID     map[int64]domain.Sensor
	libBySerialNumber map[string]domain.Sensor
	mu                sync.Mutex
}

func NewSensorRepository() *SensorRepository {
	return &SensorRepository{
		libBySensorID:     make(map[int64]domain.Sensor),
		libBySerialNumber: make(map[string]domain.Sensor),
	}
}

func (r *SensorRepository) SaveSensor(ctx context.Context, sensor *domain.Sensor) error {
	if sensor == nil {
		return ErrNilSensor
	}
	sensor.RegisteredAt = time.Now()
	sensor.IsActive = true

	r.mu.Lock()
	sensor.ID = int64(len(r.libBySensorID) + 1)
	r.libBySensorID[sensor.ID] = *sensor
	r.libBySerialNumber[sensor.SerialNumber] = *sensor
	r.mu.Unlock()
	return ctx.Err()
}

func (r *SensorRepository) GetSensors(ctx context.Context) ([]domain.Sensor, error) {
	sensors := make([]domain.Sensor, 0)
	r.mu.Lock()
	for _, sensor := range r.libBySensorID {
		sensors = append(sensors, sensor)
	}
	r.mu.Unlock()
	return sensors, ctx.Err()
}

func (r *SensorRepository) GetSensorByID(ctx context.Context, id int64) (*domain.Sensor, error) {
	if ctx.Err() != nil {
		return nil, ctx.Err()
	}
	r.mu.Lock()
	defer r.mu.Unlock()
	if sensor, ok := r.libBySensorID[id]; ok {
		return &sensor, nil
	}
	return nil, usecase.ErrSensorNotFound
}

func (r *SensorRepository) GetSensorBySerialNumber(ctx context.Context, sn string) (*domain.Sensor, error) {
	if ctx.Err() != nil {
		return nil, ctx.Err()
	}
	r.mu.Lock()
	defer r.mu.Unlock()
	if sensor, ok := r.libBySerialNumber[sn]; ok {
		return &sensor, nil
	}
	return nil, usecase.ErrSensorNotFound
}
