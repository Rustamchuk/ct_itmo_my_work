package usecase

import (
	"context"
	"homework/internal/domain"
)

type User struct {
	UserRepository
	SensorOwnerRepository
	SensorRepository
}

func NewUser(ur UserRepository, sor SensorOwnerRepository, sr SensorRepository) *User {
	return &User{
		UserRepository:        ur,
		SensorOwnerRepository: sor,
		SensorRepository:      sr,
	}
}

func (u *User) RegisterUser(ctx context.Context, user *domain.User) (*domain.User, error) {
	if user.Name == "" {
		return nil, ErrInvalidUserName
	}
	err := u.SaveUser(ctx, user)
	if err != nil {
		return nil, err
	}
	return user, nil
}

func (u *User) AttachSensorToUser(ctx context.Context, userID, sensorID int64) error {
	_, err := u.GetUserByID(ctx, userID)
	if err != nil {
		return err
	}

	_, err = u.GetSensorByID(ctx, sensorID)
	if err != nil {
		return err
	}

	err = u.SaveSensorOwner(ctx, domain.SensorOwner{
		UserID:   userID,
		SensorID: sensorID,
	})
	return err
}

func (u *User) GetUserSensors(ctx context.Context, userID int64) ([]domain.Sensor, error) {
	_, err := u.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	sensorsOwner, err := u.GetSensorsByUserID(ctx, userID)
	if err != nil {
		return nil, err
	}

	sensors := make([]domain.Sensor, len(sensorsOwner))
	for i, sensorOwner := range sensorsOwner {
		sensor, err := u.GetSensorByID(ctx, sensorOwner.SensorID)
		if err != nil {
			return nil, err
		}
		sensors[i] = *sensor
	}
	return sensors, nil
}
