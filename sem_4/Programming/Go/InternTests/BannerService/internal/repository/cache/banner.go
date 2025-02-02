package cache

import (
	openapi "github.com/Rustamchuk/Avito-Banner-Service/pkg/generated/open_api_server/go"
	"time"
)

func NewBannerCache() *BannerCache {
	return &BannerCache{
		cache: make(map[Key]Item),
	}
}

func (b *BannerCache) Get(key Key) (openapi.ImplResponse, bool) {
	b.mu.RLock()
	defer b.mu.RUnlock()

	item, exists := b.cache[key]
	if !exists {
		return openapi.ImplResponse{}, false
	}

	// Проверяем, не устарели ли данные
	if time.Since(item.Timestamp) > 5*time.Minute {
		delete(b.cache, key)
		return openapi.ImplResponse{}, false
	}

	return item.Data, true
}

func (b *BannerCache) Set(key Key, data openapi.ImplResponse) {
	b.mu.Lock()
	defer b.mu.Unlock()

	b.cache[key] = Item{
		Data:      data,
		Timestamp: time.Now(),
	}
}
