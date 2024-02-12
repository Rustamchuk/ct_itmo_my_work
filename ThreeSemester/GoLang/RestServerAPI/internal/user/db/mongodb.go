package db

import (
	"RestServerAPI/internal/user"
	"RestServerAPI/pkg/logging"
	"context"
	"errors"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
)

type db struct {
	collection *mongo.Collection
	logger     *logging.Logger
}

func NewStorage(database *mongo.Database, collection string, logger *logging.Logger) user.Storage {
	return &db{
		collection: database.Collection(collection),
		logger:     logger,
	}
}

func (d db) Create(ctx context.Context, user user.User) (string, error) {
	res, err := d.collection.InsertOne(ctx, user)
	if err != nil {
		return "", fmt.Errorf("failed create user %v", err)
	}
	oid, ok := res.InsertedID.(primitive.ObjectID)
	if ok {
		return oid.Hex(), nil
	}
	d.logger.Trace(user)
	return "", fmt.Errorf("fail convert objectId to hex")
}

func (d db) FindOne(ctx context.Context, id string) (u user.User, err error) {
	oid, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return u, fmt.Errorf("failed to convert objectid hex %s", id)
	}
	filter := bson.M{"_id": oid}
	res := d.collection.FindOne(ctx, filter)
	if res.Err() != nil {
		if errors.Is(res.Err(), mongo.ErrNoDocuments) {
			return u, fmt.Errorf("not found")
		}
		return u, fmt.Errorf("fail find user %s, %v", id, err)
	}
	err = res.Decode(&u)
	if err != nil {
		return u, fmt.Errorf("fail find one user by id %s, %v", id, err)
	}
	return u, nil
}

func (d db) Update(ctx context.Context, user user.User) error {
	objectID, err := primitive.ObjectIDFromHex(user.ID)
	if err != nil {
		return fmt.Errorf("")
	}

	filter := bson.M{"_id": objectID}

	userBytes, err := bson.Marshal(user)
	if err != nil {
		return fmt.Errorf("")
	}

	var updateUserObj bson.M
	err = bson.Unmarshal(userBytes, &updateUserObj)
	if err != nil {
		return err
	}
	delete(updateUserObj, "_id")

	update := bson.M{
		"$set": updateUserObj,
	}

	res, err := d.collection.UpdateOne(ctx, filter, update)
	if err != nil {
		return err
	}
	if res.MatchedCount == 0 {
		return fmt.Errorf("not found")
	}

	d.logger.Tracef("Matched %d docs and modif %d docs", res.MatchedCount, res.ModifiedCount)
	return nil
}

func (d db) Delete(ctx context.Context, id string) error {
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return fmt.Errorf("")
	}

	filter := bson.M{"_id": objectID}

	res, err := d.collection.DeleteOne(ctx, filter)
	if err != nil {
		return err
	}
	if res.DeletedCount == 0 {
		return fmt.Errorf("not found")
	}

	d.logger.Tracef("del %d docs", res.DeletedCount)
	return nil
}
