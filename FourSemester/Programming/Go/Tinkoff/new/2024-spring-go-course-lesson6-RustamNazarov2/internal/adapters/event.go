package adapters

import (
	"context"
	"errors"
	"homework/internal/domain"
	"net/http"
	"time"

	openapi "homework/dto/go"
)

type Event struct {
	event EventUsecase
}

func NewEvent(event EventUsecase) *Event {
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

func (s *Event) GetSensorHistory(ctx context.Context, id int64, st, fn time.Time) (openapi.ImplResponse, error) {
	if id <= 0 {
		return openapi.ImplResponse{
			Code: http.StatusBadRequest,
		}, errors.New("wrong id format")
	}
	history, err := s.event.GetSensorHistory(ctx, id, st, fn)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	apiHistory := make([]openapi.GetSensorHistory200ResponseInner, len(history))
	for i, event := range history {
		apiHistory[i] = openapi.GetSensorHistory200ResponseInner{
			Timestamp: event.Timestamp,
			Payload:   event.Payload,
		}
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: apiHistory,
	}, nil
}
