package main

import (
	"RestServerAPI/internal/config"
	"RestServerAPI/internal/user"
	"RestServerAPI/internal/user/db"
	"RestServerAPI/pkg/client/mongodb"
	"RestServerAPI/pkg/logging"
	"context"
	"fmt"
	"github.com/julienschmidt/httprouter"
	"log"
	"net"
	"net/http"
	"os"
	"path"
	"path/filepath"
	"time"
)

func main() {
	logger := logging.GetLogger()
	logger.Info("create router")
	router := httprouter.New()

	cfg := config.GetConfig()

	cfgMongo := cfg.MongoDB
	mongoClient, err := mongodb.NewClient(context.Background(), cfgMongo.Host, cfgMongo.Port, cfgMongo.Username,
		cfgMongo.Password, cfgMongo.Database, cfgMongo.AuthDB)
	if err != nil {
		panic(err)
	}
	storage := db.NewStorage(mongoClient, cfg.MongoDB.Collection, logger)

	user1 := user.User{
		ID:           "",
		Email:        "hgnmkoujm@icloud.com",
		Username:     "Rustam",
		PasswordHash: "12345",
	}
	userID, err := storage.Create(context.Background(), user1)
	if err != nil {
		panic(err)
	}
	logger.Info(userID)

	logger.Info("Register user handler")
	handler := user.NewHandle(logger)
	handler.Register(router)

	startServer(router, cfg)
}

func startServer(router *httprouter.Router, cfg *config.Config) {
	logger := logging.GetLogger()
	logger.Info("start application")

	var listener net.Listener
	var listenErr error

	if cfg.Listen.Type == "sock" {
		appDir, err := filepath.Abs(filepath.Dir(os.Args[0]))
		if err != nil {
			logger.Fatal(err)
		}
		socketPath := path.Join(appDir, "app.sock")

		logger.Info("create unix socket")
		listener, listenErr = net.Listen("unix", socketPath)
	} else {
		listener, listenErr = net.Listen("tcp", fmt.Sprintf("%s:%s", cfg.Listen.BindIP, cfg.Listen.Port))
	}
	if listenErr != nil {
		logger.Fatal(listenErr)
	}

	server := &http.Server{
		Handler:      router,
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second,
	}
	log.Fatalln(server.Serve(listener))
}
