package adapters

import (
	"context"
	openapi "homework/dto/go"
	"homework/internal/domain"
	"homework/internal/usecase"
	"net/http"
	"time"
)

type Event struct {
	event *usecase.Event
}

func NewEvent(event *usecase.Event) *Event {
	return &Event{event: event}
}

func (e *Event) EventsOptions(ctx context.Context) (openapi.ImplResponse, error) {
	if err := ctx.Err(); err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}

	headers := map[string][]string{
		"Allow": {"OPTIONS", "POST"},
	}
	return openapi.ImplResponse{
		Code: http.StatusNoContent,
		Body: headers,
	}, nil
}

func (e *Event) RegisterEvent(ctx context.Context, event openapi.SensorEvent) (openapi.ImplResponse, error) {
	err := e.event.ReceiveEvent(ctx, &domain.Event{
		Timestamp:          time.Now(),
		SensorSerialNumber: event.SensorSerialNumber,
		Payload:            event.Payload,
	})
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusBadRequest,
		}, err
	}
	return openapi.ImplResponse{
		Code: http.StatusCreated,
	}, nil
}
