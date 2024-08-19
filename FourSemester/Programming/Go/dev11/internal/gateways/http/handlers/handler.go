package handlers

import (
	"WB-Tech-L2/develop/dev11/internal/service"
	"net/http"
)

type Handler struct {
	service service.Service
}

func NewHandler(service service.Service) *Handler {
	return &Handler{service: service}
}

func (h *Handler) InitRoutes() http.Handler {
	mux := http.NewServeMux()
	routes := map[string]http.HandlerFunc{
		"/create_event":     h.CreateEvent,
		"/update_event":     h.UpdateEvent,
		"/delete_event":     h.DeleteEvent,
		"/events_for_day":   h.GetEventsForDay,
		"/events_for_week":  h.GetEventsForWeek,
		"/events_for_month": h.GetEventsForMonth,
	}

	for route, handler := range routes {
		mux.HandleFunc(route, LoggingMiddleware(handler))
	}
	return mux
}
