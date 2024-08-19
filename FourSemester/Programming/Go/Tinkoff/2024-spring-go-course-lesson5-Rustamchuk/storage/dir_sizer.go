package storage

import (
	"context"
	"sync/atomic"

	"golang.org/x/sync/errgroup"
)

// Result represents the Size function result
type Result struct {
	// Total Size of File objects
	Size int64
	// Count is a count of File objects processed
	Count int64
}

type DirSizer interface {
	// Size calculate a size of given Dir, receive a ctx and the root Dir instance
	// will return Result or error if happened
	Size(ctx context.Context, d Dir) (Result, error)
}

// sizer implement the DirSizer interface
type sizer struct {
	// maxWorkersCount number of workers for asynchronous run
	maxWorkersCount int
}

// NewSizer returns new DirSizer instance
func NewSizer() DirSizer {
	return &sizer{maxWorkersCount: 20}
}

func (a *sizer) Size(ctx context.Context, d Dir) (Result, error) {
	var (
		g, newCtx   = errgroup.WithContext(ctx)
		res         = Result{}
		dirsInWork  = int64(0)
		dirChan     = make(chan Dir, a.maxWorkersCount)
		fileChanSet = make([]<-chan File, a.maxWorkersCount/2)
	)

	dirsInWork++
	dirChan <- d
	for i := 0; i < len(fileChanSet); i++ {
		fileChanSet[i] = a.pullFiles(newCtx, &dirsInWork, dirChan, g)
	}

	for _, fanInFiles := range fileChanSet {
		a.countFiles(newCtx, &res, fanInFiles, g)
	}

	if err := g.Wait(); err != nil {
		return Result{}, err
	}
	return res, nil
}

func (a *sizer) pullFiles(ctx context.Context, dirsInWork *int64, dirChan chan Dir, g *errgroup.Group) <-chan File {
	fileChan := make(chan File)
	g.Go(func() error {
		defer func() {
			close(fileChan)
		}()

		for atomic.LoadInt64(dirsInWork) != 0 {
			select {
			case <-ctx.Done():
				return ctx.Err()
			case dir := <-dirChan:
				dirs, files, err := dir.Ls(ctx)
				if err != nil {
					return err
				}
				atomic.AddInt64(dirsInWork, int64(len(dirs)))
				for _, dir := range dirs {
					dirChan <- dir
				}
				for _, file := range files {
					fileChan <- file
				}
				atomic.AddInt64(dirsInWork, -1)
			default:
			}
		}
		return nil
	})
	return fileChan
}

func (a *sizer) countFiles(ctx context.Context, res *Result, fileChan <-chan File, g *errgroup.Group) {
	g.Go(func() error {
		for {
			select {
			case <-ctx.Done():
				return ctx.Err()
			case f, ok := <-fileChan:
				if !ok {
					return nil
				}
				size, err := f.Stat(ctx)
				if err != nil {
					return err
				}
				atomic.AddInt64(&res.Size, size)
				atomic.AddInt64(&res.Count, 1)
			}
		}
	})
}
