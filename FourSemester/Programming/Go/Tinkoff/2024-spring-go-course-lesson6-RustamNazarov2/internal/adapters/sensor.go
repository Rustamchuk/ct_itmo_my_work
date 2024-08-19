package adapters

import (
	"context"
	openapi "homework/dto/go"
	"homework/internal/domain"
	"homework/internal/usecase"
	"net/http"
	"strconv"
)

type Sensor struct {
	sensor *usecase.Sensor
}

func NewSensor(sensor *usecase.Sensor) *Sensor {
	return &Sensor{sensor: sensor}
}

func (s *Sensor) GetSensor(ctx context.Context, i int64) (openapi.ImplResponse, error) {
	sensor, err := s.sensor.GetSensorByID(ctx, i)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: openapi.Sensor{
			Id:           sensor.ID,
			SerialNumber: sensor.SerialNumber,
			Type:         string(sensor.Type),
			CurrentState: sensor.CurrentState,
			Description:  sensor.Description,
			IsActive:     sensor.IsActive,
			RegisteredAt: sensor.RegisteredAt,
			LastActivity: sensor.LastActivity,
		},
	}, nil
}

func (s *Sensor) GetSensors(ctx context.Context) (openapi.ImplResponse, error) {
	sensors, err := s.sensor.GetSensors(ctx)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusBadRequest,
		}, err
	}
	sensorsResponse := make([]openapi.Sensor, len(sensors))
	for i, sensor := range sensors {
		sensorsResponse[i] = openapi.Sensor{
			Id:           sensor.ID,
			SerialNumber: sensor.SerialNumber,
			Type:         string(sensor.Type),
			CurrentState: sensor.CurrentState,
			Description:  sensor.Description,
			IsActive:     sensor.IsActive,
			RegisteredAt: sensor.RegisteredAt,
			LastActivity: sensor.LastActivity,
		}
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: sensorsResponse,
	}, nil
}

func (s *Sensor) HeadSensor(ctx context.Context, i int64) (openapi.ImplResponse, error) {
	_, err := s.sensor.GetSensorByID(ctx, i)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	header := map[string][]string{
		"Content-Length": {strconv.Itoa(1)},
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: header,
	}, nil
}

func (s *Sensor) HeadSensors(ctx context.Context) (openapi.ImplResponse, error) {
	sensors, err := s.sensor.GetSensors(ctx)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}
	headers := map[string][]string{
		"Content-Length": {strconv.Itoa(len(sensors))},
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: headers,
	}, nil
}

func (s *Sensor) RegisterSensor(ctx context.Context, create openapi.SensorToCreate) (openapi.ImplResponse, error) {
	sensor, err := s.sensor.RegisterSensor(ctx, &domain.Sensor{
		SerialNumber: create.SerialNumber,
		Type:         domain.SensorType(create.Type),
		Description:  create.Description,
		IsActive:     create.IsActive,
	})
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusBadRequest,
		}, err
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: openapi.Sensor{
			Id:           sensor.ID,
			SerialNumber: sensor.SerialNumber,
			Type:         string(sensor.Type),
			CurrentState: sensor.CurrentState,
			Description:  sensor.Description,
			IsActive:     sensor.IsActive,
			RegisteredAt: sensor.RegisteredAt,
			LastActivity: sensor.LastActivity,
		},
	}, nil
}

func (s *Sensor) SensorOptions(ctx context.Context, _ int64) (openapi.ImplResponse, error) {
	if err := ctx.Err(); err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}

	headers := map[string][]string{
		"Allow": {"GET", "OPTIONS", "HEAD"},
	}
	return openapi.ImplResponse{
		Code: http.StatusNoContent,
		Body: headers,
	}, nil
}

func (s *Sensor) SensorsOptions(ctx context.Context) (openapi.ImplResponse, error) {
	if err := ctx.Err(); err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}

	headers := map[string][]string{
		"Allow": {"GET", "POST", "HEAD", "OPTIONS"},
	}
	return openapi.ImplResponse{
		Code: http.StatusNoContent,
		Body: headers,
	}, nil
}
