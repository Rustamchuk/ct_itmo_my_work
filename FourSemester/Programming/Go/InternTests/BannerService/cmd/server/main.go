package main

import (
	"github.com/Rustamchuk/Avito-Banner-Service/internal/repository"
	"github.com/Rustamchuk/Avito-Banner-Service/internal/repository/postgres"
	"github.com/Rustamchuk/Avito-Banner-Service/internal/service"
	openapi "github.com/Rustamchuk/Avito-Banner-Service/pkg/generated/open_api_server/go"
	_ "github.com/lib/pq"
	"github.com/sirupsen/logrus"
	"golang.org/x/net/context"
	"log"
	"net/http"
	"time"
)

func main() {
	logrus.SetFormatter(new(logrus.JSONFormatter))

	//if err := initConfig(); err != nil {
	//	logrus.Fatalf("error initializing configs: %s", err.Error())
	//}

	//if err := godotenv.Load(); err != nil {
	//	logrus.Fatalf("error loading env variables: %s", err.Error())
	//}

	db, err := postgres.NewPostgresDB(postgres.Config{
		Host:     "banner-service-db",
		Port:     "5432",
		Username: "postgres",
		DBName:   "postgres",
		SSLMode:  "disable",
		Password: "admin",
	})
	if err != nil {
		logrus.Fatalf("failed to initialize db: %s", err.Error())
	}

	repos := repository.NewRepository(db)
	serviceBanner := service.NewBannerImplementation(repos)

	log.Printf("Server started")

	DefaultAPIController := openapi.NewDefaultAPIController(serviceBanner)

	router := openapi.NewRouter(DefaultAPIController)

	log.Fatal(http.ListenAndServe(":8080", router))

	//srv := new(Server)
	//go func() {
	//	if err := srv.Run(viper.GetString("port"), router); err != nil {
	//		logrus.Fatalf("error occured while running http server: %s", err.Error())
	//	}
	//}()
	//
	//logrus.Print("TodoApp Started")
	//
	//quit := make(chan os.Signal, 1)
	//signal.Notify(quit, syscall.SIGTERM, syscall.SIGINT)
	//<-quit
	//
	//logrus.Print("TodoApp Shutting Down")
	//
	//if err := srv.Shutdown(context.Background()); err != nil {
	//	logrus.Errorf("error occured on server shutting down: %s", err.Error())
	//}
	//
	//if err := db.Close(); err != nil {
	//	logrus.Errorf("error occured on db connection close: %s", err.Error())
	//}
}

//func initConfig() error {
//	viper.AddConfigPath("configs")
//	viper.SetConfigName("config")
//	return viper.ReadInConfig()
//}

type Server struct {
	httpServer *http.Server
}

func (s *Server) Run(port string, handler http.Handler) error {
	s.httpServer = &http.Server{
		Addr:           ":" + port,
		Handler:        handler,
		MaxHeaderBytes: 1 << 20, // 1 MB
		ReadTimeout:    10 * time.Second,
		WriteTimeout:   10 * time.Second,
	}

	return s.httpServer.ListenAndServe()
}

func (s *Server) Shutdown(ctx context.Context) error {
	return s.httpServer.Shutdown(ctx)
}
