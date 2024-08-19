package inmemory

import (
	"WB-Tech-L2/develop/dev11/internal/model"
	"context"
)

type Event interface {
	CreateEvent(ctx context.Context, userID int, event model.Event) error
	GetEvents(ctx context.Context, userID int) ([]model.Event, error)
	DeleteEvent(ctx context.Context, userID int, eventID int) error
}

type InMemory struct {
	Event
}

func NewInMemory() InMemory {
	return InMemory{Event: NewEvent()}
}
