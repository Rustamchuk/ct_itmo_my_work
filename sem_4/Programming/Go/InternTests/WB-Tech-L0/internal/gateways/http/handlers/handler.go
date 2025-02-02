package handlers

import (
	"WB-Tech-L0/internal/service"
	"github.com/gin-gonic/gin"
)

type Order interface {
	GetOrders(c *gin.Context)
	GetOrderByUID(c *gin.Context)
	AddOrder(c *gin.Context)
}

type Handler struct {
	Order
}

func NewHandler(service service.Service) *Handler {
	return &Handler{Order: newOrder(service.Order)}
}
