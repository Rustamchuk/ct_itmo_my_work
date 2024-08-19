package usecase

import (
	"context"
	"errors"
	"homework/internal/domain"
	"testing"
	"time"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
)

func Test_event_ReceiveEvent(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	t.Run("err, invalid event", func(t *testing.T) {
		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		e := NewEvent(nil, nil)

		err := e.ReceiveEvent(ctx, &domain.Event{})
		assert.ErrorIs(t, err, ErrInvalidEventTimestamp)
	})

	t.Run("err, sensor not found", func(t *testing.T) {
		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		sr := NewMockSensorRepository(ctrl)

		sr.EXPECT().GetSensorBySerialNumber(ctx, gomock.Any()).Times(1).Return(nil, ErrSensorNotFound)

		e := NewEvent(nil, sr)

		err := e.ReceiveEvent(ctx, &domain.Event{
			Timestamp: time.Now(),
		})
		assert.ErrorIs(t, err, ErrSensorNotFound)
	})

	t.Run("err, event save error", func(t *testing.T) {
		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		sr := NewMockSensorRepository(ctrl)

		sr.EXPECT().GetSensorBySerialNumber(ctx, "123").Times(1).Return(&domain.Sensor{
			ID: 1,
		}, nil)

		er := NewMockEventRepository(ctrl)
		expectedError := errors.New("some error")
		er.EXPECT().SaveEvent(ctx, gomock.Any()).Times(1).Return(expectedError)

		e := NewEvent(er, sr)

		err := e.ReceiveEvent(ctx, &domain.Event{
			Timestamp:          time.Now(),
			SensorSerialNumber: "123",
		})
		assert.ErrorIs(t, err, expectedError)
	})

	t.Run("err, sensor save error", func(t *testing.T) {
		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		sr := NewMockSensorRepository(ctrl)

		sr.EXPECT().GetSensorBySerialNumber(ctx, "123").Times(1).Return(&domain.Sensor{
			ID: 1,
		}, nil)
		expectedError := errors.New("some error")
		sr.EXPECT().SaveSensor(ctx, gomock.Any()).Times(1).Times(1).Return(expectedError)

		er := NewMockEventRepository(ctrl)
		er.EXPECT().SaveEvent(ctx, gomock.Any()).Times(1).Return(nil)

		e := NewEvent(er, sr)

		err := e.ReceiveEvent(ctx, &domain.Event{
			Timestamp:          time.Now(),
			SensorSerialNumber: "123",
		})
		assert.ErrorIs(t, err, expectedError)
	})

	t.Run("ok, no error", func(t *testing.T) {
		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		sr := NewMockSensorRepository(ctrl)

		sr.EXPECT().GetSensorBySerialNumber(ctx, "123").Times(1).Return(&domain.Sensor{
			ID: 1,
		}, nil)
		sr.EXPECT().SaveSensor(ctx, gomock.Any()).Times(1).Do(func(_ context.Context, s *domain.Sensor) {
			assert.Equal(t, int64(8), s.CurrentState)
			assert.NotEmpty(t, s.LastActivity)
		})

		er := NewMockEventRepository(ctrl)
		er.EXPECT().SaveEvent(ctx, gomock.Any()).Times(1).DoAndReturn(func(_ context.Context, event *domain.Event) error {
			assert.Equal(t, int64(1), event.SensorID)
			assert.Equal(t, "123", event.SensorSerialNumber)

			return nil
		})

		e := NewEvent(er, sr)
		err := e.ReceiveEvent(ctx, &domain.Event{
			Timestamp:          time.Now(),
			SensorSerialNumber: "123",
			Payload:            8,
		})
		assert.NoError(t, err)
	})
}

func Test_event_GetSensorHistory(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	mockEventRepository := NewMockEventRepository(ctrl)
	eventUsecase := NewEvent(mockEventRepository, nil)

	now := time.Now()
	ctx := context.Background()
	someError := errors.New("someError")

	tests := []struct {
		name          string
		method        func() ([]domain.Event, error)
		setup         func()
		expectedArr   []domain.Event
		expectedError error
	}{
		{
			name: "success",
			method: func() ([]domain.Event, error) {
				return eventUsecase.GetSensorHistory(
					ctx,
					1,
					now,
					now)
			},
			setup: func() {
				mockEventRepository.EXPECT().GetSensorHistory(ctx, int64(1), now, now).Return([]domain.Event{
					{
						Timestamp:          now,
						SensorSerialNumber: "1",
						SensorID:           1,
						Payload:            1,
					},
				}, nil)
			},
			expectedArr: []domain.Event{
				{
					Timestamp:          now,
					SensorSerialNumber: "1",
					SensorID:           1,
					Payload:            1,
				},
			},
			expectedError: nil,
		},
		{
			name: "error",
			method: func() ([]domain.Event, error) {
				return eventUsecase.GetSensorHistory(
					ctx,
					1,
					now,
					now)
			},
			setup: func() {
				mockEventRepository.EXPECT().GetSensorHistory(ctx, int64(1), now, now).Return(nil, someError)
			},
			expectedArr:   nil,
			expectedError: someError,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.setup()
			response, err := tt.method()
			assert.Equal(t, tt.expectedArr, response)
			if tt.expectedError != nil {
				assert.ErrorIs(t, err, tt.expectedError)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}
