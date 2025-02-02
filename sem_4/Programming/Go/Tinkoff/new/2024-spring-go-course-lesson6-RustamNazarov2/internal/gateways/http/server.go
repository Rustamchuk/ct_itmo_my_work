package http

import (
	"context"
	"errors"
	"fmt"
	"homework/internal/usecase"
	"log"
	"net/http"

	"github.com/gin-gonic/gin"
)

type Server struct {
	host       string
	port       uint16
	router     *gin.Engine
	httpServer *http.Server
}

type UseCases struct {
	Event  *usecase.Event
	Sensor *usecase.Sensor
	User   *usecase.User
}

func NewServer(useCases UseCases, options ...func(*Server)) *Server {
	r := gin.Default()
	setupRouter(r, useCases, NewWebSocketHandler(useCases))

	s := &Server{router: r, host: "localhost", port: 8080}
	for _, o := range options {
		o(s)
	}

	s.httpServer = &http.Server{
		Addr:    fmt.Sprintf("%s:%d", s.host, s.port),
		Handler: s.router,
	}

	return s
}

func WithHost(host string) func(*Server) {
	return func(s *Server) {
		s.host = host
	}
}

func WithPort(port uint16) func(*Server) {
	return func(s *Server) {
		s.port = port
	}
}

func (s *Server) Run(_ context.Context) error {
	if err := s.httpServer.ListenAndServe(); !errors.Is(err, http.ErrServerClosed) {
		return fmt.Errorf("HTTP server ListenAndServe: %w", err)
	}
	return nil
}

func (s *Server) Shutdown(ctx context.Context) error {
	if err := s.httpServer.Shutdown(ctx); err != nil {
		return fmt.Errorf("server shutdown failed: %w", err)
	}
	log.Println("Server gracefully stopped")
	return nil
}
