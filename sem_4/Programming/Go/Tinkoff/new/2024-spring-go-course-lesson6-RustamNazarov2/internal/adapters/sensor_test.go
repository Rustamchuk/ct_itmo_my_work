package adapters

import (
	"context"
	"homework/internal/domain"
	"net/http"
	"strconv"
	"testing"
	"time"

	openapi "homework/dto/go"

	"github.com/golang/mock/gomock"
)

type testSensorSetup struct {
	ctrl              *gomock.Controller
	mockSensorUsecase *MockSensorUsecase
	sensorAdapter     *Sensor
}

func setupSensorTest(t *testing.T) *testSensorSetup {
	ctrl := gomock.NewController(t)
	mockSensorUsecase := NewMockSensorUsecase(ctrl)
	sensorAdapter := NewSensor(mockSensorUsecase)
	return &testSensorSetup{
		ctrl:              ctrl,
		mockSensorUsecase: mockSensorUsecase,
		sensorAdapter:     sensorAdapter,
	}
}

func TestSensor_GetSensor(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.GetSensor(ctx, 1)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensorByID(ctx, int64(1)).Return(&domain.Sensor{
					ID:           1,
					SerialNumber: "1",
					Type:         "1",
					CurrentState: 1,
					Description:  "1",
					IsActive:     true,
					RegisteredAt: now,
					LastActivity: now,
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: openapi.Sensor{
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
			expectedError: nil,
		},
		{
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.GetSensor(ctx, 1)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensorByID(ctx, int64(1)).Return(nil, someError)
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

func TestSensor_GetSensors(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.GetSensors(ctx)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensors(ctx).Return([]domain.Sensor{
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
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.GetSensors(ctx)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensors(ctx).Return(nil, someError)
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

func TestSensor_HeadSensor(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.HeadSensor(ctx, 1)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensorByID(ctx, int64(1)).Return(&domain.Sensor{
					ID:           1,
					SerialNumber: "1",
					Type:         "1",
					CurrentState: 1,
					Description:  "1",
					IsActive:     true,
					RegisteredAt: now,
					LastActivity: now,
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: map[string][]string{
					"Content-Length": {strconv.Itoa(1)},
				},
			},
			expectedError: nil,
		},
		{
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.HeadSensor(ctx, 1)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensorByID(ctx, int64(1)).Return(nil, someError)
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

func TestSensor_HeadSensors(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.HeadSensors(ctx)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensors(ctx).Return([]domain.Sensor{
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
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.HeadSensors(ctx)
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().GetSensors(ctx).Return(nil, someError)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusInternalServerError,
				Body: nil,
			},
			expectedError: someError,
		},
	}

	runTests(t, tests)
}

func TestSensor_RegisterSensor(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()
	ctx := context.Background()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.RegisterSensor(ctx, openapi.SensorToCreate{
					SerialNumber: "1",
					Type:         string(domain.SensorTypeADC),
					Description:  "1",
					IsActive:     true,
				})
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().RegisterSensor(ctx, &domain.Sensor{
					SerialNumber: "1",
					Type:         domain.SensorTypeADC,
					Description:  "1",
					IsActive:     true,
				}).Return(&domain.Sensor{
					ID:           1,
					SerialNumber: "1",
					Type:         "1",
					CurrentState: 1,
					Description:  "1",
					IsActive:     true,
					RegisteredAt: now,
					LastActivity: now,
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: openapi.Sensor{
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
			expectedError: nil,
		},
		{
			name: "bad request",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.RegisterSensor(ctx, openapi.SensorToCreate{
					SerialNumber: "1",
					Type:         string(domain.SensorTypeADC),
					Description:  "1",
					IsActive:     true,
				})
			},
			setup: func() {
				setup.mockSensorUsecase.EXPECT().RegisterSensor(ctx, &domain.Sensor{
					SerialNumber: "1",
					Type:         domain.SensorTypeADC,
					Description:  "1",
					IsActive:     true,
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

func TestEvent_SensorOptions(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.SensorOptions(context.Background(), 1)
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNoContent,
				Body: map[string][]string{
					"Allow": {"GET", "OPTIONS", "HEAD"},
				},
			},
			expectedError: nil,
		},
		{
			name: "canceled context",
			method: func() (openapi.ImplResponse, error) {
				ctx, cancel := context.WithCancel(context.Background())
				cancel()
				return setup.sensorAdapter.SensorOptions(ctx, 1)
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

func TestEvent_SensorsOptions(t *testing.T) {
	setup := setupSensorTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.sensorAdapter.SensorsOptions(context.Background())
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNoContent,
				Body: map[string][]string{
					"Allow": {"GET", "POST", "HEAD", "OPTIONS"},
				},
			},
			expectedError: nil,
		},
		{
			name: "canceled context",
			method: func() (openapi.ImplResponse, error) {
				ctx, cancel := context.WithCancel(context.Background())
				cancel()
				return setup.sensorAdapter.SensorOptions(ctx, 1)
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
