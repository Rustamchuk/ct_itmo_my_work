package fact

import (
	"context"
	"fmt"
	"io"
	"math"
	"strconv"
	"strings"
	"sync"
)

type Input struct {
	NumsOfGoroutine int   // n - число горутин
	Numbers         []int // слайс чисел, которые необходимо факторизовать
}

type Factorization interface {
	Work(Input, io.Writer) error
}

type myFactorial struct {
}

func NewFactorization() Factorization {
	return &myFactorial{}
}

func (f *myFactorial) Work(input Input, writer io.Writer) error {
	var (
		numInd      = 0
		numLine     = 0
		errCh       = make(chan error)
		ctx, cancel = context.WithCancel(context.Background())
		wg          sync.WaitGroup
		muInd       sync.Mutex
		muWrite     sync.Mutex
	)
	defer cancel()

	for i := 0; i < input.NumsOfGoroutine; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			err := f.searchNums(ctx, &muInd, &muWrite, &numInd, &numLine, input.Numbers, writer)
			if err != nil {
				errCh <- err
				cancel()
				return
			}
		}()
	}

	wg.Wait()
	close(errCh)
	return <-errCh
}

func (f *myFactorial) searchNums(
	ctx context.Context,
	muInd *sync.Mutex,
	muWrite *sync.Mutex,
	numInd *int,
	numLine *int,
	numbers []int,
	writer io.Writer,
) error {
	for {
		select {
		case <-ctx.Done():
			return nil
		default:
			ind := f.bookNum(muInd, numInd, numbers)
			if ind == -1 {
				return nil
			}

			factors := f.factorization(numbers[ind])

			muWrite.Lock()
			*numLine++
			_, err := writer.Write([]byte(fmt.Sprintf("line %d, %d = %s\n",
				*numLine, numbers[ind], strings.Join(factors, " * "))))
			if err != nil {
				muWrite.Unlock()
				return err
			}
			muWrite.Unlock()
		}
	}
}

func (f *myFactorial) bookNum(
	mu *sync.Mutex,
	numInd *int,
	numbers []int,
) int {
	mu.Lock()
	if *numInd >= len(numbers) {
		mu.Unlock()
		return -1
	}
	ind := *numInd
	*numInd++
	mu.Unlock()
	return ind
}

func (f *myFactorial) factorization(n int) []string {
	factors := make([]string, 0)

	if n < 0 {
		factors = append(factors, "-1")
		n *= -1
	}
	if n == 1 {
		factors = append(factors, "1")
	}

	sqrtN := int(math.Sqrt(float64(n)))
	for i := 2; i <= sqrtN && n > 1; i++ {
		if n%i == 0 {
			factors = append(factors, strconv.Itoa(i))
			n = n / i
			i--
		}
	}
	if n != 1 {
		factors = append(factors, strconv.Itoa(n))
	}
	return factors
}
