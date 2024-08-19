package usecase

import (
	"context"
	"errors"
	"homework/internal/domain"
)

type Sensor struct {
	SensorRepository
}

func NewSensor(sr SensorRepository) *Sensor {
	return &Sensor{
		SensorRepository: sr,
	}
}

func (s *Sensor) RegisterSensor(ctx context.Context, sensor *domain.Sensor) (*domain.Sensor, error) {
	if sensor.Type != domain.SensorTypeADC &&
		sensor.Type != domain.SensorTypeContactClosure {
		return nil, ErrWrongSensorType
	}
	if len(sensor.SerialNumber) != domain.SerialNumberLen {
		return nil, ErrWrongSensorSerialNumber
	}

	sr, err := s.GetSensorBySerialNumber(ctx, sensor.SerialNumber)
	if err == nil {
		return sr, nil
	}
	if !errors.Is(err, ErrSensorNotFound) {
		return nil, err
	}

	err = s.SaveSensor(ctx, sensor)
	if err != nil {
		return nil, err
	}
	return sensor, nil
}

func (s *Sensor) GetSensors(ctx context.Context) ([]domain.Sensor, error) {
	return s.SensorRepository.GetSensors(ctx)
}

func (s *Sensor) GetSensorByID(ctx context.Context, id int64) (*domain.Sensor, error) {
	return s.SensorRepository.GetSensorByID(ctx, id)
}
