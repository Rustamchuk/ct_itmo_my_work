package service

import (
	"WB-Tech-L2/develop/dev11/internal/model"
	"WB-Tech-L2/develop/dev11/internal/reposiory"
	"context"
	"time"
)

type Event interface {
	GetEventsForDay(ctx context.Context, userIdD int, day time.Time) ([]model.Event, error)
	GetEventsForWeek(ctx context.Context, userID int, week time.Time) ([]model.Event, error)
	GetEventsForMonth(ctx context.Context, userID int, month time.Time) ([]model.Event, error)
	CreateEvent(ctx context.Context, userID int, event model.Event) error
	UpdateEvent(ctx context.Context, userID int, event model.Event) error
	DeleteEvent(ctx context.Context, userID int, eventID int) error
}

type Service struct {
	Event
}

func NewService(repos reposiory.Repository) Service {
	return Service{Event: NewEvent(repos)}
}
