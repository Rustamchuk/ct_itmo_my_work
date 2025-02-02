package postgres

import (
	"context"
	"fmt"
	"homework/internal/domain"
	"homework/internal/usecase"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

var sensorsTable = "sensors"

var (
	querySave        = fmt.Sprintf("INSERT INTO %s (serial_number, type, current_state, description, is_active, registered_at, last_activity) VALUES ($1, $2, $3, $4, $5, $6, $7)", sensorsTable)
	queryUpdate      = fmt.Sprintf("UPDATE %s SET serial_number=$1, type=$2, current_state=$3, description=$4, is_active=$5, last_activity=$6 WHERE id=$7", sensorsTable)
	queryGet         = fmt.Sprintf("SELECT id, serial_number, type, current_state, description, is_active, registered_at, last_activity FROM %s", sensorsTable)
	queryGetByID     = fmt.Sprintf("SELECT id, serial_number, type, current_state, description, is_active, registered_at, last_activity FROM %s WHERE id = $1", sensorsTable)
	queryGetBySerial = fmt.Sprintf("SELECT id, serial_number, type, current_state, description, is_active, registered_at, last_activity FROM %s WHERE serial_number = $1", sensorsTable)
)

type SensorRepository struct {
	pool *pgxpool.Pool
}

func NewSensorRepository(pool *pgxpool.Pool) *SensorRepository {
	return &SensorRepository{
		pool: pool,
	}
}

func (r *SensorRepository) SaveSensor(ctx context.Context, sensor *domain.Sensor) error {
	if sensor.ID == 0 {
		_, err := r.pool.Exec(ctx, querySave,
			sensor.SerialNumber, sensor.Type, sensor.CurrentState, sensor.Description, sensor.IsActive, time.Now(), sensor.LastActivity)
		return err
	}
	_, err := r.pool.Exec(ctx, queryUpdate,
		sensor.SerialNumber, sensor.Type, sensor.CurrentState, sensor.Description, sensor.IsActive, sensor.LastActivity, sensor.ID)
	return err
}

func (r *SensorRepository) GetSensors(ctx context.Context) ([]domain.Sensor, error) {
	rows, err := r.pool.Query(ctx, queryGet)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var sensors []domain.Sensor
	for rows.Next() {
		var sensor domain.Sensor
		err = rows.Scan(&sensor.ID, &sensor.SerialNumber, &sensor.Type, &sensor.CurrentState,
			&sensor.Description, &sensor.IsActive, &sensor.RegisteredAt, &sensor.LastActivity)
		if err != nil {
			return nil, err
		}
		sensors = append(sensors, sensor)
	}
	return sensors, nil
}

func (r *SensorRepository) GetSensorByID(ctx context.Context, id int64) (*domain.Sensor, error) {
	row := r.pool.QueryRow(ctx, queryGetByID, id)
	var sensor domain.Sensor
	err := row.Scan(&sensor.ID, &sensor.SerialNumber, &sensor.Type, &sensor.CurrentState,
		&sensor.Description, &sensor.IsActive, &sensor.RegisteredAt, &sensor.LastActivity)
	if err != nil {
		return nil, usecase.ErrSensorNotFound
	}
	return &sensor, nil
}

func (r *SensorRepository) GetSensorBySerialNumber(ctx context.Context, sn string) (*domain.Sensor, error) {
	row := r.pool.QueryRow(ctx, queryGetBySerial, sn)
	var sensor domain.Sensor
	err := row.Scan(&sensor.ID, &sensor.SerialNumber, &sensor.Type, &sensor.CurrentState,
		&sensor.Description, &sensor.IsActive, &sensor.RegisteredAt, &sensor.LastActivity)
	if err != nil {
		return nil, usecase.ErrSensorNotFound
	}
	return &sensor, nil
}
