package http

import (
	"WB-Tech-L0/internal/service"
	"context"
	"errors"
	"fmt"
	"log"
	"net/http"
	"path/filepath"

	"github.com/gin-gonic/gin"
)

type Server struct {
	host       string
	port       uint16
	router     *gin.Engine
	httpServer *http.Server
}

func NewServer(service service.Service, options ...func(*Server)) *Server {
	r := gin.Default()
	abs, _ := filepath.Abs(".")
	r.LoadHTMLGlob(filepath.Join(abs, "web/*.html"))
	setupRouter(r, service)

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

func (s *Server) Run() error {
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
