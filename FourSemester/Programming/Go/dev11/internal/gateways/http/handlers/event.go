package handlers

import (
	"WB-Tech-L2/develop/dev11/internal/gateways/http/handlers/api"
	"WB-Tech-L2/develop/dev11/internal/model"
	"net/http"
	"time"
)

func (h *Handler) CreateEvent(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodPost) {
		return
	}

	var req api.CreateEventRequest
	if err := decodeJSON(w, r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	event := model.Event{
		Name:        req.Name,
		Description: req.Description,
		Date:        time.Time(req.Date),
	}
	if err := h.service.CreateEvent(r.Context(), req.UserID, event); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not create event",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: "event created",
	})
}

func (h *Handler) UpdateEvent(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodPost) {
		return
	}

	var req api.UpdateEventRequest
	if err := decodeJSON(w, r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	event := model.Event{
		ID:          req.EventID,
		Name:        req.Name,
		Description: req.Description,
		Date:        time.Time(req.Date),
	}
	if err := h.service.UpdateEvent(r.Context(), req.UserID, event); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not update event",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: "event updated",
	})
}

func (h *Handler) DeleteEvent(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodPost) {
		return
	}

	var req api.DeleteEventRequest
	if err := decodeJSON(w, r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	if err := h.service.DeleteEvent(r.Context(), req.UserID, req.EventID); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not delete event",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: "event deleted",
	})
}

func (h *Handler) GetEventsForDay(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodGet) {
		return
	}

	var req api.GetEventsRequest
	if err := decodeQuery(r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	events, err := h.service.GetEventsForDay(r.Context(), req.UserID, time.Time(req.Date))
	if err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not get events",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: events,
	})
}

func (h *Handler) GetEventsForWeek(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodGet) {
		return
	}

	var req api.GetEventsRequest
	if err := decodeQuery(r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	events, err := h.service.GetEventsForWeek(r.Context(), req.UserID, time.Time(req.Date))
	if err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not get events",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: events,
	})
}

func (h *Handler) GetEventsForMonth(w http.ResponseWriter, r *http.Request) {
	if !validateMethod(w, r, http.MethodGet) {
		return
	}

	var req api.GetEventsRequest
	if err := decodeQuery(r, &req); err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusBadRequest,
			Body: "invalid request",
		})
		return
	}
	events, err := h.service.GetEventsForMonth(r.Context(), req.UserID, time.Time(req.Date))
	if err != nil {
		api.WriteJSONResponse(w, api.Response{
			Code: http.StatusInternalServerError,
			Body: "could not get events",
		})
		return
	}
	api.WriteJSONResponse(w, api.Response{
		Code: http.StatusOK,
		Body: events,
	})
}
