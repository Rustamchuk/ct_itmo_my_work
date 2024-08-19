package adapters

import (
	"context"
	openapi "homework/dto/go"
	"homework/internal/domain"
	"homework/internal/usecase"
	"net/http"
)

type User struct {
	user *usecase.User
}

func NewUser(user *usecase.User) *User {
	return &User{user: user}
}

func (u *User) BindSensorToUser(ctx context.Context, i int64, binding openapi.SensorToUserBinding) (openapi.ImplResponse, error) {
	err := u.user.AttachSensorToUser(ctx, i, binding.SensorId)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	return openapi.ImplResponse{
		Code: http.StatusCreated,
	}, nil
}

func (u *User) CreateUser(ctx context.Context, create openapi.UserToCreate) (openapi.ImplResponse, error) {
	user, err := u.user.RegisterUser(ctx, &domain.User{
		Name: create.Name,
	})
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusBadRequest,
		}, err
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: openapi.User{
			Id:   user.ID,
			Name: user.Name,
		},
	}, nil
}

func (u *User) GetUserSensors(ctx context.Context, i int64) (openapi.ImplResponse, error) {
	sensors, err := u.user.GetUserSensors(ctx, i)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	sensorsResponse := make([]openapi.Sensor, len(sensors))
	for j, sensor := range sensors {
		sensorsResponse[j] = openapi.Sensor{
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

func (u *User) HeadUserSensors(ctx context.Context, i int64) (openapi.ImplResponse, error) {
	_, err := u.user.GetUserSensors(ctx, i)
	if err != nil {
		return openapi.ImplResponse{
			Code: http.StatusNotFound,
		}, err
	}
	headers := map[string][]string{
		"Content-Length": {"1"},
	}
	return openapi.ImplResponse{
		Code: http.StatusOK,
		Body: headers,
	}, nil
}

func (u *User) UsersOptions(ctx context.Context) (openapi.ImplResponse, error) {
	if err := ctx.Err(); err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}

	headers := map[string][]string{
		"Allow": {"GET", "POST", "OPTIONS"},
	}
	return openapi.ImplResponse{
		Code: http.StatusNoContent,
		Body: headers,
	}, nil
}

func (u *User) UsersSensorsOptions(ctx context.Context, _ int64) (openapi.ImplResponse, error) {
	if err := ctx.Err(); err != nil {
		return openapi.ImplResponse{
			Code: http.StatusInternalServerError,
		}, err
	}

	headers := map[string][]string{
		"Allow": {"GET", "HEAD", "POST", "OPTIONS"},
	}
	return openapi.ImplResponse{
		Code: http.StatusNoContent,
		Body: headers,
	}, nil
}
