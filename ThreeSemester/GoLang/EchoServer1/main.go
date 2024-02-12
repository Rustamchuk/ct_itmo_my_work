package main

import (
	"github.com/labstack/echo"
	"net/http"
)

func main() {
	e := echo.New()

	e.GET("/", get)

	e.Start(":8080")
}

func get(e echo.Context) error {
	return e.String(http.StatusOK, "ok")
}
