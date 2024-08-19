package postgres

import (
	"context"
	"fmt"
	"homework/internal/domain"

	"github.com/jackc/pgx/v5/pgxpool"
)

var SensorOwnersTable = "sensors_users"

var (
	querySaveOwner    = fmt.Sprintf("INSERT INTO %s (sensor_id, user_id) VALUES ($1, $2)", SensorOwnersTable)
	queryGetByOwnerID = fmt.Sprintf("SELECT sensor_id, user_id FROM %s WHERE user_id = $1", SensorOwnersTable)
)

type SensorOwnerRepository struct {
	pool *pgxpool.Pool
}

func NewSensorOwnerRepository(pool *pgxpool.Pool) *SensorOwnerRepository {
	return &SensorOwnerRepository{
		pool,
	}
}

func (r *SensorOwnerRepository) SaveSensorOwner(ctx context.Context, sensorOwner domain.SensorOwner) error {
	_, err := r.pool.Exec(ctx, querySaveOwner, sensorOwner.SensorID, sensorOwner.UserID)
	return err
}

func (r *SensorOwnerRepository) GetSensorsByUserID(ctx context.Context, userID int64) ([]domain.SensorOwner, error) {
	rows, err := r.pool.Query(ctx, queryGetByOwnerID, userID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var sensorOwners []domain.SensorOwner
	for rows.Next() {
		var sensorOwner domain.SensorOwner
		if err := rows.Scan(&sensorOwner.SensorID, &sensorOwner.UserID); err != nil {
			return nil, err
		}
		sensorOwners = append(sensorOwners, sensorOwner)
	}
	return sensorOwners, nil
}
