package http

import (
	"context"
	"log"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
	"nhooyr.io/websocket"
	"nhooyr.io/websocket/wsjson"
)

type WebSocketHandler struct {
	useCases    UseCases
	connections map[*websocket.Conn]context.CancelFunc
	mu          sync.Mutex
}

func NewWebSocketHandler(useCases UseCases) *WebSocketHandler {
	return &WebSocketHandler{
		useCases:    useCases,
		connections: make(map[*websocket.Conn]context.CancelFunc),
	}
}

func (h *WebSocketHandler) Handle(c *gin.Context, id int64) error {
	_, err := h.useCases.Sensor.GetSensorByID(c.Request.Context(), id)
	if err != nil {
		return err
	}

	conn, err := websocket.Accept(c.Writer, c.Request, nil)
	if err != nil {
		return err
	}

	ctx, cancel := context.WithCancel(context.Background())

	h.mu.Lock()
	h.connections[conn] = cancel
	h.mu.Unlock()

	go func() {
		defer func() {
			_ = conn.Close(websocket.StatusInternalError, "inside error")
		}()

		ticker := time.NewTicker(5 * time.Second)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				event, err := h.useCases.Event.GetLastEventBySensorID(ctx, id)
				if err != nil {
					log.Printf("error while socket GetLastEventBySensorID: %v", err)
					return
				}

				err = wsjson.Write(ctx, conn, event)
				if err != nil {
					log.Printf("error while socket writing: %v", err)
					return
				}
			case <-ctx.Done():
				return
			}
		}
	}()

	go func() {
		for {
			_, msg, err := conn.Reader(context.Background())
			if websocket.CloseStatus(err) != -1 {
				cancel()
				return
			}
			err = wsjson.Write(ctx, conn, msg)
			if err != nil {
				log.Printf("error while socket writing: %v", err)
				cancel()
				return
			}
		}
	}()
	return nil
}

func (h *WebSocketHandler) Shutdown() error {
	h.mu.Lock()
	defer h.mu.Unlock()

	for _, cancel := range h.connections {
		cancel()
	}
	return nil
}
