package watcher

import (
	"context"
	"errors"
	"io/fs"
	"os"
	"path/filepath"
	"time"
)

type EventType string

const (
	EventTypeFileCreate  EventType = "file_created"
	EventTypeFileRemoved EventType = "file_removed"
)

var (
	ErrDirNotExist = errors.New("dir does not exist")
	ErrDirNotWalk  = errors.New("cannot walk in dirs")
)

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
	refreshInterval time.Duration
	files           map[string]bool
}

func NewDirWatcher(refreshInterval time.Duration) *Watcher {
	return &Watcher{
		refreshInterval: refreshInterval,
		Events:          make(chan Event),
		files:           make(map[string]bool),
	}
}

func (w *Watcher) WatchDir(ctx context.Context, path string) error {
	var (
		ticker = time.NewTicker(w.refreshInterval)
		finish bool
	)
	defer ticker.Stop()

	walk := func(init bool) {
		errWalk := w.walkFiles(path, init)
		if errWalk != nil {
			finish = true
		}
	}

	if _, err := os.Stat(path); err != nil {
		return ErrDirNotExist
	}

	walk(false)

	for !finish {
		select {
		case <-ctx.Done():
			return ctx.Err()
		case <-ticker.C:
			walk(true)
		}
	}
	return ErrDirNotWalk
}

func (w *Watcher) walkFiles(path string, initial bool) error {
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
