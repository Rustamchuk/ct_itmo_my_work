package watcher

import (
	"context"
	"errors"
	"io/fs"
	"os"
	"path/filepath"
	"sync"
	"time"
)

type EventType string

const (
	EventTypeFileCreate  EventType = "file_created"
	EventTypeFileRemoved EventType = "file_removed"
)

var ErrDirNotExist = errors.New("dir does not exist")

// Event - событие, формируемое при изменении в файловой системе.
type Event struct {
	// Type - тип события.
	Type EventType
	// Path - путь к объекту изменения.
	Path string
}

type Watcher struct {
	// Events - канал событий
	// refreshInterval - интервал обновления списка файлов
	Events          chan Event
	errChan         chan error
	refreshInterval time.Duration
	files           map[string]bool
	mu              sync.Mutex
}

func NewDirWatcher(refreshInterval time.Duration) *Watcher {
	return &Watcher{
		refreshInterval: refreshInterval,
		Events:          make(chan Event),
		errChan:         make(chan error),
		files:           make(map[string]bool),
	}
}

func (w *Watcher) WatchDir(ctx context.Context, path string) error {
	if _, err := os.Stat(path); err != nil {
		return ErrDirNotExist
	}
	w.mu = sync.Mutex{}
	init := false
	ticker := time.NewTicker(w.refreshInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return ctx.Err()
		case err := <-w.errChan:
			return err
		case <-ticker.C:
			go func() {
				err := w.walkFiles(path, init)
				if err != nil {
					w.errChan <- err
				}
				init = true
			}()
		}
	}
}

func (w *Watcher) walkFiles(path string, initial bool) error {
	w.mu.Lock()
	defer w.mu.Unlock()
	err := filepath.WalkDir(path, func(path string, d fs.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			return nil
		}
		if _, ok := w.files[path]; !ok {
			if initial {
				w.Events <- Event{
					Type: EventTypeFileCreate,
					Path: path,
				}
			}
		}
		w.files[path] = true
		return nil
	})
	if err != nil {
		return err
	}

	for file, exist := range w.files {
		if !exist {
			delete(w.files, file)
			w.Events <- Event{
				Type: EventTypeFileRemoved,
				Path: file,
			}
		} else {
			w.files[file] = false
		}
	}
	return nil
}

func (w *Watcher) Close() {
	close(w.Events)
}
