package adapters

import (
	"context"
	"homework/internal/domain"
	"net/http"
	"testing"
	"time"

	openapi "homework/dto/go"

	"github.com/golang/mock/gomock"
)

type testUserSetup struct {
	ctrl            *gomock.Controller
	mockUserUsecase *MockUserUsecase
	userAdapter     *User
}

func setupUserTest(t *testing.T) *testUserSetup {
	ctrl := gomock.NewController(t)
	mockUserUsecase := NewMockUserUsecase(ctrl)
	userAdapter := NewUser(mockUserUsecase)
	return &testUserSetup{
		ctrl:            ctrl,
		mockUserUsecase: mockUserUsecase,
		userAdapter:     userAdapter,
	}
}

func TestSensor_BindSensorToUser(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.BindSensorToUser(ctx, 1, openapi.SensorToUserBinding{SensorId: 1})
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().AttachSensorToUser(ctx, int64(1), int64(1)).Return(nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusCreated,
				Body: nil,
			},
			expectedError: nil,
		},
		{
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.BindSensorToUser(ctx, 1, openapi.SensorToUserBinding{SensorId: 1})
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().AttachSensorToUser(ctx, int64(1), int64(1)).Return(someError)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNotFound,
				Body: nil,
			},
			expectedError: someError,
		},
	}

	runTests(t, tests)
}

func TestSensor_CreateUser(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.CreateUser(ctx, openapi.UserToCreate{Name: "1"})
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().RegisterUser(ctx, &domain.User{
					Name: "1",
				}).Return(&domain.User{
					ID:   int64(1),
					Name: "1",
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: openapi.User{
					Id:   1,
					Name: "1",
				},
			},
			expectedError: nil,
		},
		{
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.CreateUser(ctx, openapi.UserToCreate{Name: "1"})
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().RegisterUser(ctx, &domain.User{
					Name: "1",
				}).Return(nil, someError)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusBadRequest,
				Body: nil,
			},
			expectedError: someError,
		},
	}

	runTests(t, tests)
}

func TestSensor_GetUserSensors(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.GetUserSensors(ctx, 1)
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().GetUserSensors(ctx, int64(1)).Return([]domain.Sensor{
					{
						ID:           1,
						SerialNumber: "1",
						Type:         "1",
						CurrentState: 1,
						Description:  "1",
						IsActive:     true,
						RegisteredAt: now,
						LastActivity: now,
					},
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: []openapi.Sensor{
					{
						Id:           1,
						SerialNumber: "1",
						Type:         "1",
						CurrentState: 1,
						Description:  "1",
						IsActive:     true,
						RegisteredAt: now,
						LastActivity: now,
					},
				},
			},
			expectedError: nil,
		},
		{
			name: "not found",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.GetUserSensors(ctx, 1)
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().GetUserSensors(ctx, int64(1)).Return(nil, someError)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNotFound,
				Body: nil,
			},
			expectedError: someError,
		},
	}

	runTests(t, tests)
}

func TestSensor_HeadUserSensors(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.HeadUserSensors(ctx, 1)
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().GetUserSensors(ctx, int64(1)).Return([]domain.Sensor{
					{
						ID:           1,
						SerialNumber: "1",
						Type:         "1",
						CurrentState: 1,
						Description:  "1",
						IsActive:     true,
						RegisteredAt: now,
						LastActivity: now,
					},
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: map[string][]string{
					"Content-Length": {"1"},
				},
			},
			expectedError: nil,
		},
		{
			name: "not found",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.HeadUserSensors(ctx, 1)
			},
			setup: func() {
				setup.mockUserUsecase.EXPECT().GetUserSensors(ctx, int64(1)).Return(nil, someError)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNotFound,
				Body: nil,
			},
			expectedError: someError,
		},
	}

	runTests(t, tests)
}

func TestEvent_UsersSensorsOptions(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.UsersSensorsOptions(context.Background(), 1)
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNoContent,
				Body: map[string][]string{
					"Allow": {"GET", "HEAD", "POST", "OPTIONS"},
				},
			},
			expectedError: nil,
		},
		{
			name: "canceled context",
			method: func() (openapi.ImplResponse, error) {
				ctx, cancel := context.WithCancel(context.Background())
				cancel()
				return setup.userAdapter.UsersSensorsOptions(ctx, 1)
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusInternalServerError,
				Body: nil,
			},
			expectedError: context.Canceled,
		},
	}

	runTests(t, tests)
}

func TestEvent_UsersOptions(t *testing.T) {
	setup := setupUserTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.userAdapter.UsersOptions(context.Background())
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNoContent,
				Body: map[string][]string{
					"Allow": {"GET", "POST", "OPTIONS"},
				},
			},
			expectedError: nil,
		},
		{
			name: "canceled context",
			method: func() (openapi.ImplResponse, error) {
				ctx, cancel := context.WithCancel(context.Background())
				cancel()
				return setup.userAdapter.UsersOptions(ctx)
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusInternalServerError,
				Body: nil,
			},
			expectedError: context.Canceled,
		},
	}

	runTests(t, tests)
}
