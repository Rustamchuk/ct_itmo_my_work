package adapters

import (
	"context"
	"homework/internal/domain"
	"homework/internal/usecase"
	"net/http"
	"strconv"
	"testing"
	"time"

	openapi "homework/dto/go"

	event "homework/internal/repository/event/inmemory"
	sensor "homework/internal/repository/sensor/inmemory"

	"github.com/stretchr/testify/assert"

	"github.com/golang/mock/gomock"
)

type testEventSetup struct {
	ctrl             *gomock.Controller
	mockEventUsecase *MockEventUsecase
	eventAdapter     *Event
}

func setupEventTest(t *testing.T) *testEventSetup {
	ctrl := gomock.NewController(t)
	mockEventUsecase := NewMockEventUsecase(ctrl)
	eventAdapter := NewEvent(mockEventUsecase)
	return &testEventSetup{
		ctrl:             ctrl,
		mockEventUsecase: mockEventUsecase,
		eventAdapter:     eventAdapter,
	}
}

func TestEvent_EventsOptions(t *testing.T) {
	setup := setupEventTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.eventAdapter.EventsOptions(context.Background())
			},
			setup: nil,
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusNoContent,
				Body: map[string][]string{
					"Allow": {"OPTIONS", "POST"},
				},
			},
			expectedError: nil,
		},
		{
			name: "canceled context",
			method: func() (openapi.ImplResponse, error) {
				ctx, cancel := context.WithCancel(context.Background())
				cancel()
				return setup.eventAdapter.EventsOptions(ctx)
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

func TestEvent_RegisterEvent(t *testing.T) {
	setup := setupEventTest(t)
	defer setup.ctrl.Finish()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.eventAdapter.RegisterEvent(context.Background(), openapi.SensorEvent{
					SensorSerialNumber: "some",
					Payload:            10,
				})
			},
			setup: func() {
				setup.mockEventUsecase.EXPECT().ReceiveEvent(context.Background(), gomock.Any()).Return(nil)
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
				return setup.eventAdapter.RegisterEvent(context.Background(), openapi.SensorEvent{
					SensorSerialNumber: "some",
					Payload:            10,
				})
			},
			setup: func() {
				setup.mockEventUsecase.EXPECT().ReceiveEvent(context.Background(), gomock.Any()).Return(someError)
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

func TestEvent_GetSensorHistory(t *testing.T) {
	setup := setupEventTest(t)
	defer setup.ctrl.Finish()

	now := time.Now()

	tests := []testArgs{
		{
			name: "success",
			method: func() (openapi.ImplResponse, error) {
				return setup.eventAdapter.GetSensorHistory(context.Background(), 1, now, now)
			},
			setup: func() {
				setup.mockEventUsecase.EXPECT().GetSensorHistory(context.Background(), int64(1), now, now).Return([]domain.Event{
					{
						Timestamp: now,
						Payload:   10,
					},
				}, nil)
			},
			expectedResponse: openapi.ImplResponse{
				Code: http.StatusOK,
				Body: []openapi.GetSensorHistory200ResponseInner{
					{
						Timestamp: now,
						Payload:   10,
					},
				},
			},
			expectedError: nil,
		},
		{
			name: "not found",
			method: func() (openapi.ImplResponse, error) {
				return setup.eventAdapter.GetSensorHistory(context.Background(), 1, now, now)
			},
			setup: func() {
				setup.mockEventUsecase.EXPECT().GetSensorHistory(context.Background(), int64(1), now, now).Return(nil, someError)
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

func FuzzEvent_GetSensorHistory(f *testing.F) {
	sensorRepo := sensor.NewSensorRepository()
	eventAdapter := NewEvent(usecase.NewEvent(event.NewEventRepository(), sensorRepo))
	sensorAdapter := NewSensor(usecase.NewSensor(sensorRepo))

	f.Add(int64(1), "2021-01-01T00:00:00Z", "2021-01-02T00:00:00Z")
	f.Add(int64(-1), "invalid-date", "2021-01-02T00:00:00Z")

	for i := 1; i < 10; i++ {
		_, err := sensorAdapter.RegisterSensor(context.Background(), openapi.SensorToCreate{
			SerialNumber: strconv.Itoa(i * 1000000000),
			Type:         string(domain.SensorTypeADC),
			Description:  "1",
			IsActive:     true,
		})
		assert.NoError(f, err)

		_, err = eventAdapter.RegisterEvent(context.Background(), openapi.SensorEvent{
			SensorSerialNumber: strconv.Itoa(i * 1000000000),
			Payload:            int64(i),
		})
		assert.NoError(f, err)
	}

	f.Fuzz(func(t *testing.T, sensorId int64, startDate, endDate string) {
		ctx := context.Background()
		startTime, errStart := time.Parse(time.RFC3339, startDate)
		endTime, errEnd := time.Parse(time.RFC3339, endDate)

		if errStart != nil || errEnd != nil {
			return
		}

		response, err := eventAdapter.GetSensorHistory(ctx, sensorId, startTime, endTime)

		if sensorId < 1 {
			if response.Code != http.StatusBadRequest {
				t.Errorf("Expected BadRequest for invalid input, got %v", response.Code)
			}
		} else if sensorId > 9 {
			if response.Code != http.StatusNotFound {
				t.Errorf("Expected NotFound for valid input, got %v", response.Code)
			}
		} else {
			if response.Code != http.StatusOK {
				t.Errorf("Expected OK for valid input, got %v", response.Code)
			}
			assert.NoError(t, err)
		}
	})
}
