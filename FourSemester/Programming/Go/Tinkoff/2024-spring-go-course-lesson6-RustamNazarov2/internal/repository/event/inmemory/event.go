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
	ErrEventNotFound = errors.New("event not found")
	ErrNilEvent      = errors.New("event is nil")
)

type EventRepository struct {
	libBySensorID map[int64][]domain.Event
	mu            sync.Mutex
}

func NewEventRepository() *EventRepository {
	return &EventRepository{
		libBySensorID: make(map[int64][]domain.Event),
	}
}

func (r *EventRepository) SaveEvent(ctx context.Context, event *domain.Event) error {
	if ctx.Err() != nil {
		return ctx.Err()
	}
	if event == nil {
		return ErrNilEvent
	}

	r.mu.Lock()
	if _, ok := r.libBySensorID[event.SensorID]; !ok {
		r.libBySensorID[event.SensorID] = make([]domain.Event, 0)
	}
	r.libBySensorID[event.SensorID] = append(r.libBySensorID[event.SensorID], *event)
	r.mu.Unlock()
	return nil
}

func (r *EventRepository) GetLastEventBySensorID(ctx context.Context, id int64) (*domain.Event, error) {
	if ctx.Err() != nil {
		return nil, ctx.Err()
	}
	r.mu.Lock()
	events, ok := r.libBySensorID[id]
	r.mu.Unlock()

	if !ok {
		return nil, usecase.ErrEventNotFound
	}

	var maxTime time.Time
	var resInd int
	for i, event := range events {
		if ctx.Err() != nil {
			return nil, ctx.Err()
		}
		if maxTime.Before(event.Timestamp) {
			maxTime = event.Timestamp
			resInd = i
		}
	}
	return &events[resInd], nil
}
