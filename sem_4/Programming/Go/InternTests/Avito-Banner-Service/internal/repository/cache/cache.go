package cache

import (
	openapi "github.com/Rustamchuk/Avito-Banner-Service/pkg/generated/open_api_server/go"
	"sync"
	"time"
)

type Key struct {
	TagId     int32
	FeatureId int32
	Token     string
}

type Item struct {
	Data      openapi.ImplResponse
	Timestamp time.Time
}

type BannerCache struct {
	cache map[Key]Item
	mu    sync.RWMutex
}
