package postgres

import (
	"context"
	"errors"
	"fmt"
	"homework/internal/domain"
	"homework/internal/usecase"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

var ErrEventNotFound = errors.New("event not found")

var eventsTable = "events"

var (
	querySave        = fmt.Sprintf("INSERT INTO %s (timestamp, sensor_serial_number, sensor_id, payload) VALUES ($1, $2, $3, $4)", eventsTable)
	queryGetLastByID = fmt.Sprintf("SELECT timestamp, sensor_serial_number, sensor_id, payload FROM %s WHERE sensor_id = $1 ORDER BY timestamp DESC LIMIT 1", eventsTable)
	queryGetHistory  = fmt.Sprintf("SELECT timestamp, sensor_serial_number, sensor_id, payload FROM %s WHERE sensor_id = $1 AND timestamp BETWEEN $2 AND $3", eventsTable)
)

type EventRepository struct {
	pool *pgxpool.Pool
}

func NewEventRepository(pool *pgxpool.Pool) *EventRepository {
	return &EventRepository{
		pool,
	}
}

func (r *EventRepository) SaveEvent(ctx context.Context, event *domain.Event) error {
	_, err := r.pool.Exec(ctx, querySave, event.Timestamp, event.SensorSerialNumber, event.SensorID, event.Payload)
	return err
}

func (r *EventRepository) GetLastEventBySensorID(ctx context.Context, id int64) (*domain.Event, error) {
	row := r.pool.QueryRow(ctx, queryGetLastByID, id)
	var event domain.Event
	err := row.Scan(&event.Timestamp, &event.SensorSerialNumber, &event.SensorID, &event.Payload)
	if err != nil {
		return nil, usecase.ErrEventNotFound
	}
	return &event, nil
}

func (r *EventRepository) GetSensorHistory(ctx context.Context, id int64, st, fn time.Time) ([]domain.Event, error) {
	_, err := r.GetLastEventBySensorID(ctx, id)
	if err != nil {
		return nil, usecase.ErrEventNotFound
	}

	rows, err := r.pool.Query(ctx, queryGetHistory, id, st, fn)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var events []domain.Event
	for rows.Next() {
		var event domain.Event
		if err := rows.Scan(&event.Timestamp, &event.SensorSerialNumber, &event.SensorID, &event.Payload); err != nil {
			return nil, err
		}
		events = append(events, event)
	}
	return events, nil
}
