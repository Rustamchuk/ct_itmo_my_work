package usecase

import (
	"context"
	"homework/internal/domain"
	"time"
)

type Event struct {
	EventRepository
	SensorRepository
}

func NewEvent(er EventRepository, sr SensorRepository) *Event {
	return &Event{
		EventRepository:  er,
		SensorRepository: sr,
	}
}

func (e *Event) ReceiveEvent(ctx context.Context, event *domain.Event) error {
	if event.Timestamp.IsZero() {
		return ErrInvalidEventTimestamp
	}

	sensor, err := e.GetSensorBySerialNumber(ctx, event.SensorSerialNumber)
	if err != nil {
		return err
	}

	event.SensorID = sensor.ID
	err = e.SaveEvent(ctx, event)
	if err != nil {
		return err
	}

	sensor.CurrentState = event.Payload
	sensor.LastActivity = event.Timestamp
	return e.SaveSensor(ctx, sensor)
}

func (e *Event) GetLastEventBySensorID(ctx context.Context, id int64) (*domain.Event, error) {
	return e.EventRepository.GetLastEventBySensorID(ctx, id)
}

func (e *Event) GetSensorHistory(ctx context.Context, id int64, st, fn time.Time) ([]domain.Event, error) {
	return e.EventRepository.GetSensorHistory(ctx, id, st, fn)
}
