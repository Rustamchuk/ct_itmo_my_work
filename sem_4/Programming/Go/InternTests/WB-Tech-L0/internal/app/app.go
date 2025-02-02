package app

import (
	httpGateway "WB-Tech-L0/internal/gateways/http"
	"WB-Tech-L0/internal/gateways/nats"
	"WB-Tech-L0/internal/repository"
	"WB-Tech-L0/internal/repository/postgres"
	"WB-Tech-L0/internal/service"
	"context"
	_ "github.com/lib/pq"
	"github.com/spf13/viper"
	"log"
	"os"
	"os/signal"
	"path/filepath"
	"strconv"
	"sync"
	"syscall"
	"time"
)

func Run() {
	abs, _ := filepath.Abs(".")
	viper.SetConfigFile(filepath.Join(abs, "configs/config.yml"))

	if err := viper.ReadInConfig(); err != nil {
		log.Printf("failed to read config file " + err.Error())
	}

	db, err := postgres.NewPostgresDB(postgres.Config{
		Host:     viper.GetString("db.host"),
		Port:     viper.GetString("db.port"),
		Username: viper.GetString("db.username"),
		DBName:   viper.GetString("db.dbname"),
		SSLMode:  viper.GetString("db.ssl_mode"),
		Password: viper.GetString("db.password"),
	})
	if err != nil {
		log.Fatalf("failed to initialize db: %s", err.Error())
	}

	repos := repository.NewRepository(db)
	services := service.NewService(repos)

	nat, err := nats.NewBroker(nats.Config{
		Host:      viper.GetString("nats.host"),
		Port:      viper.GetString("nats.port"),
		ClusterID: viper.GetString("nats.cluster_id"),
		ClientID:  viper.GetString("nats.client_id"),
	})
	if err != nil {
		log.Fatalf("failed to connect nats-streaming server: %v", err)
	}
	subscriber := nats.NewSubscriber(nat, services, nats.SubscriberConfig{
		DurableName: viper.GetString("nats.durable_name"),
		SubjectPost: viper.GetString("nats.subject_post"),
	})

	ctx, cancel := context.WithCancel(context.Background())
	wg := sync.WaitGroup{}
	if err := subscriber.Subscribe(&wg, ctx); err != nil {
		log.Printf("failed to subscribe to channel " + err.Error())
	}

	log.Printf("Server started")

	host := viper.GetString("server.host")
	port, err := strconv.Atoi(viper.GetString("server.port"))
	if err != nil {
		log.Printf("failed to parse config port " + err.Error())
	}

	r := httpGateway.NewServer(
		services,
		httpGateway.WithHost(host),
		httpGateway.WithPort(uint16(port)),
	)
	go func() {
		if err = r.Run(); err != nil {
			log.Printf("error during server start: %v", err)
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGQUIT, syscall.SIGKILL, syscall.SIGTERM)
	<-quit
	cancel()
	wg.Wait()

	log.Printf("shut down")

	if err := subscriber.UnSubscribe(); err != nil {
		log.Printf("failed to unsubscribe channel: %v", err)
	}

	if err := nat.Close(); err != nil {
		log.Printf("failed to close nats-streaming server connection: %v", err)
	}

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := r.Shutdown(shutdownCtx); err != nil {
		log.Printf("error during server shutdown: %v", err)
	}
}
