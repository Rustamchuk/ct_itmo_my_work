package http

import (
	"context"
	"homework/dto/go"
	"homework/internal/adapters"
	"net/http"
	"strconv"
	"strings"

	"github.com/gin-gonic/gin"
)

//go:generate docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i /local/api/swagger.yaml -g go-server -o /local/dto
// rm -f ${PWD}/dto/go.mod
// rm -f ${PWD}/dto/main.go

func setupRouter(r *gin.Engine, useCases UseCases, socket *WebSocketHandler) {
	EventsAdapter := adapters.NewEvent(useCases.Event)
	EventsAPIController := openapi.NewEventsAPIController(EventsAdapter)

	SensorsAdapter := adapters.NewSensor(useCases.Sensor)
	SensorsAPIController := openapi.NewSensorsAPIController(SensorsAdapter)

	UsersAdapter := adapters.NewUser(useCases.User)
	UsersAPIController := openapi.NewUsersAPIController(UsersAdapter)
	routers := []openapi.Router{EventsAPIController, SensorsAPIController, UsersAPIController}

	apiGroup := r.Group("/")
	apiGroup.Use(ContentTypeMiddleware())
	apiGroup.Use(AcceptMiddleware())
	apiGroup.Use(ParamsToContextMiddleware())

	for _, api := range routers {
		for _, route := range api.Routes() {
			pattern := SetMuxToGinURL(route.Pattern)
			handler := gin.WrapF(route.HandlerFunc)
			switch route.Method {
			case http.MethodGet:
				apiGroup.GET(pattern, handler)
			case http.MethodPost:
				apiGroup.POST(pattern, handler)
			case http.MethodHead:
				apiGroup.HEAD(pattern, handler)
			case http.MethodOptions:
				apiGroup.OPTIONS(pattern, handler)
			}
		}
	}

	r.GET("/sensors/:sensor_id/events", func(c *gin.Context) {
		sensorID, err := strconv.ParseInt(c.Param("sensor_id"), 10, 64)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Неверный формат sensor_id"})
			return
		}
		err = socket.Handle(c, sensorID)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{"error": "Ошибка обработки WebSocket"})
		}
		c.JSON(http.StatusNoContent, nil)
	})

	r.GET("/ping", func(c *gin.Context) {
		c.String(200, "pong")
	})

	r.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusNotFound, gin.H{"message": "Not found"})
	})

	r.HandleMethodNotAllowed = true
	r.NoMethod(func(c *gin.Context) {
		c.JSON(http.StatusMethodNotAllowed, gin.H{"message": "Method not allowed"})
	})
}

func SetMuxToGinURL(pattern string) string {
	if !strings.Contains(pattern, "{") {
		return pattern
	}
	pattern = strings.ReplaceAll(pattern, "{", ":")
	pattern = strings.ReplaceAll(pattern, "}", "")
	return pattern
}

func ParamsToContextMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		ctx := c.Request.Context()
		for _, param := range c.Params {
			ctx = context.WithValue(ctx, param.Key, param.Value) //nolint:staticcheck // неправда
		}
		c.Request = c.Request.WithContext(ctx)
		c.Next()
	}
}

func ContentTypeMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		if c.Request.Method == "POST" && c.GetHeader("Content-Type") != "application/json" {
			c.JSON(http.StatusUnsupportedMediaType, gin.H{"message": "Not supported Content-type"})
			c.Abort()
			return
		}
		c.Next()
	}
}

func AcceptMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		if (c.Request.Method == "GET" || c.Request.Method == "HEAD") && c.GetHeader("Accept") != "application/json" {
			c.JSON(http.StatusNotAcceptable, gin.H{"message": "Not Acceptable"})
			c.Abort()
			return
		}
		c.Next()
	}
}
