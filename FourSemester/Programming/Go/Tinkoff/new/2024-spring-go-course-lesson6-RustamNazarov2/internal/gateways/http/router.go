package http

import (
	"context"
	"homework/dto/go"
	"homework/internal/adapters"
	"net/http"
	"strconv"
	"strings"

	"github.com/prometheus/client_golang/prometheus/collectors"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/prometheus/client_golang/prometheus/promhttp"

	"github.com/gin-gonic/gin"
)

//go:generate docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i /local/api/swagger.yaml -g go-server -o /local/dto
// rm -f ${PWD}/dto/go.mod
// rm -f ${PWD}/dto/main.go

var (
	totalRequests = promauto.NewCounterVec(prometheus.CounterOpts{
		Name: "http_requests_total",
		Help: "Количество HTTP запросов.",
	}, []string{"method", "endpoint"})

	requestDuration = promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name: "http_request_duration",
		Help: "Длительность HTTP запросов.",
	}, []string{"method", "endpoint"})

	errorCount = promauto.NewCounterVec(prometheus.CounterOpts{
		Name: "http_errors_total",
		Help: "Количество HTTP ошибок.",
	}, []string{"method", "endpoint", "status_code"})

	activeConnections = promauto.NewGauge(prometheus.GaugeOpts{
		Name: "http_active_connections",
		Help: "Количество активных HTTP соединений.",
	})

	responseSize = promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name: "http_response_size_bytes",
		Help: "Размер HTTP ответов в байтах.",
	}, []string{"method", "endpoint"})
)

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
	initMetrics()
	apiGroup.Use(PrometheusMiddleware())

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

	r.GET("/metrics", gin.WrapH(promhttp.HandlerFor(prometheus.DefaultGatherer, promhttp.HandlerOpts{})))

	r.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusNotFound, gin.H{"message": "Not found"})
	})

	r.HandleMethodNotAllowed = true
	r.NoMethod(func(c *gin.Context) {
		c.JSON(http.StatusMethodNotAllowed, gin.H{"message": "Method not allowed"})
	})
}

func initMetrics() {
	reg := prometheus.NewRegistry()

	reg.MustRegister(collectors.NewGoCollector())
	reg.MustRegister(collectors.NewProcessCollector(collectors.ProcessCollectorOpts{}))

	reg.MustRegister(totalRequests)
	reg.MustRegister(requestDuration)
	reg.MustRegister(errorCount)
	reg.MustRegister(activeConnections)
	reg.MustRegister(responseSize)

	prometheus.DefaultRegisterer = reg
	prometheus.DefaultGatherer = reg
}

func PrometheusMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		path := c.Request.URL.Path
		method := c.Request.Method

		timer := prometheus.NewTimer(requestDuration.WithLabelValues(method, path))
		activeConnections.Inc()
		c.Next()
		timer.ObserveDuration()

		totalRequests.WithLabelValues(method, path).Inc()
		activeConnections.Dec()

		if status := c.Writer.Status(); status >= 400 {
			errorCount.WithLabelValues(method, path, strconv.Itoa(status)).Inc()
		}

		responseSize.WithLabelValues(method, path).Observe(float64(c.Writer.Size()))
	}
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
