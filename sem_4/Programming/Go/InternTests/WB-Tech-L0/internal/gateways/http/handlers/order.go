package handlers

import (
	"WB-Tech-L0/internal/adapters"
	"WB-Tech-L0/internal/gateways/http/api"
	"WB-Tech-L0/internal/service"
	"github.com/gin-gonic/gin"
	"net/http"
)

type order struct {
	service service.Order
}

func newOrder(service service.Order) Order {
	return &order{service: service}
}

func (o *order) GetOrders(c *gin.Context) {
	orders, err := o.service.GetOrders(c)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to retrieve orders" + err.Error()})
		return
	}

	if len(orders) == 0 {
		c.JSON(http.StatusOK, gin.H{"message": "No orders found"})
		return
	}

	c.JSON(http.StatusOK, adapters.OrderArrModelResponse(orders))
}

func (o *order) GetOrderByUID(c *gin.Context) {
	orderUID := c.Param("uid")
	if orderUID == "" {
		c.HTML(http.StatusBadRequest, "error.html", gin.H{
			"Message": "Order UID is required",
		})
		return
	}

	orderRes, err := o.service.GetOrderByUID(c, orderUID)
	if err != nil {
		c.HTML(http.StatusNotFound, "error.html", gin.H{
			"Message": "Failed to get order " + orderUID + " " + err.Error(),
		})
		return
	}
	c.HTML(http.StatusOK, "order.html", gin.H{
		"Order": adapters.OrderModelResponse(orderRes),
	})
}

func (o *order) AddOrder(c *gin.Context) {
	var req api.Order
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if err := o.service.AddOrder(c, adapters.OrderRequestModel(req)); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to add order " + err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "Order added successfully"})
}
