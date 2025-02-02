package service

import (
	"WB-Tech-L2/develop/dev11/internal/model"
	"WB-Tech-L2/develop/dev11/internal/reposiory"
	"context"
	"fmt"
	"time"
)

type event struct {
	repo reposiory.Repository
}

func NewEvent(repo reposiory.Repository) Event {
	return &event{repo: repo}
}

func (e *event) GetEventsForDay(ctx context.Context, userID int, day time.Time) ([]model.Event, error) {
	events, err := e.repo.GetEvents(ctx, userID)
	if err != nil {
		return nil, err
	}
	var result []model.Event
	for _, event := range events {
		if event.Date.Year() == day.Year() && event.Date.YearDay() == day.YearDay() {
			result = append(result, event)
		}
	}
	return result, nil
}

func (e *event) GetEventsForWeek(ctx context.Context, userID int, week time.Time) ([]model.Event, error) {
	events, err := e.repo.GetEvents(ctx, userID)
	if err != nil {
		return nil, err
	}
	var result []model.Event
	year, weekNumber := week.ISOWeek()
	for _, event := range events {
		eventYear, eventWeek := event.Date.ISOWeek()
		if eventYear == year && eventWeek == weekNumber {
			result = append(result, event)
		}
	}
	return result, nil
}

func (e *event) GetEventsForMonth(ctx context.Context, userID int, month time.Time) ([]model.Event, error) {
	events, err := e.repo.GetEvents(ctx, userID)
	if err != nil {
		return nil, err
	}
	var result []model.Event
	for _, event := range events {
		if event.Date.Year() == month.Year() && event.Date.Month() == month.Month() {
			result = append(result, event)
		}
	}
	return result, nil
}

func (e *event) CreateEvent(ctx context.Context, userID int, event model.Event) error {
	return e.repo.CreateEvent(ctx, userID, event)
}

func (e *event) UpdateEvent(ctx context.Context, userID int, event model.Event) error {
	events, err := e.repo.GetEvents(ctx, userID)
	if err != nil {
		return err
	}
	for i, e := range events {
		if e.ID == event.ID {
			events[i] = event
			return nil
		}
	}
	return fmt.Errorf("event %d not found for user %d", event.ID, userID)
}

func (e *event) DeleteEvent(ctx context.Context, userID int, eventID int) error {
	return e.repo.DeleteEvent(ctx, userID, eventID)
}
