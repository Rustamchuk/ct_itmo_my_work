package http

import (
	"WB-Tech-L0/internal/gateways/http/handlers"
	"WB-Tech-L0/internal/service"
	"github.com/gin-gonic/gin"
	"net/http"
)

func setupRouter(r *gin.Engine, service service.Service) {
	handler := handlers.NewHandler(service)

	r.POST("/order", handler.AddOrder)
	r.GET("/order", handler.GetOrders)
	r.GET("/order/:uid", handler.GetOrderByUID)

	r.GET("/home", func(c *gin.Context) {
		c.HTML(http.StatusOK, "home.html", nil)
	})

	r.GET("/ping", func(c *gin.Context) {
		c.String(http.StatusOK, "pong")
	})

}
