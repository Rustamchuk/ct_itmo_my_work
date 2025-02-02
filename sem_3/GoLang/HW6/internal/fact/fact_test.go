package fact

import (
	"bytes"
	"context"
	"reflect"
	"strings"
	"sync"
	"testing"
)

func Test_myFactorial_Work(t *testing.T) {
	type args struct {
		input Input
	}
	tests := []struct {
		name       string
		args       args
		wantWriter string
		wantErr    bool
	}{
		{
			"Base",
			args{Input{3, []int{-321, 100, -99, 98, 97, 96, 95, 94, 32}}},
			"line 1, -321 = -1 * 3 * 107\n" +
				"line 2, 98 = 2 * 7 * 7\n" +
				"line 3, 97 = 97\n" +
				"line 4, 96 = 2 * 2 * 2 * 2 * 2 * 3\n" +
				"line 5, 95 = 5 * 19\n" +
				"line 6, 94 = 2 * 47\n" +
				"line 7, 32 = 2 * 2 * 2 * 2 * 2\n" +
				"line 8, 100 = 2 * 2 * 5 * 5\n" +
				"line 9, -99 = -1 * 3 * 3 * 11\n",
			false,
		},
		{
			"Empty",
			args{Input{3, []int{}}},
			"",
			false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			f := &myFactorial{}
			writer := &bytes.Buffer{}
			err := f.Work(tt.args.input, writer)
			if (err != nil) != tt.wantErr {
				t.Errorf("Work() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			gotWriter := strings.Split(writer.String(), "\n")
			for i := 0; i < len(gotWriter)-1; i++ {
				if !strings.Contains(tt.wantWriter, strings.Split(gotWriter[i], ",")[1]) {
					t.Errorf("Work() gotWriter = %v, want %v", writer.String(), tt.wantWriter)
				}
			}
			if len(gotWriter)-1 != len(tt.args.input.Numbers) {
				t.Errorf("Defferent number strs got %d, want %d", len(gotWriter), len(tt.args.input.Numbers))
			}
		})
	}
}

func Test_myFactorial_searchNums(t *testing.T) {
	type args struct {
		ctx     context.Context
		mu      *sync.Mutex
		mu2     *sync.Mutex
		numInd  *int
		numLine *int
		numbers []int
	}

	a1, a2 := 0, 0
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	tests := []struct {
		name string
		args args
		want string
	}{
		{
			"Base",
			args{
				ctx,
				&sync.Mutex{},
				&sync.Mutex{},
				&a1,
				&a2,
				[]int{100, -17},
			},
			"line 1, 100 = 2 * 2 * 5 * 5\nline 2, -17 = -1 * 17\n",
		},
		{
			"OutBounds",
			args{
				ctx,
				&sync.Mutex{},
				&sync.Mutex{},
				&a1,
				&a2,
				[]int{100, -17},
			},
			"",
		},
		{
			"Canceled",
			args{
				ctx,
				&sync.Mutex{},
				&sync.Mutex{},
				&a1,
				&a2,
				[]int{100, -17},
			},
			"",
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.name == "Canceled" {
				cancel()
			}
			writer := &bytes.Buffer{}
			f := &myFactorial{}
			err := f.searchNums(tt.args.ctx, tt.args.mu, tt.args.mu2, tt.args.numInd, tt.args.numLine, tt.args.numbers, writer)
			if err != nil {
				t.Errorf("searchNums() ended with error")
			}

			got := writer.String()
			if got != tt.want {
				t.Errorf("searchNums() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_myFactorial_bookNum(t *testing.T) {
	type args struct {
		mu      *sync.Mutex
		numInd  *int
		numbers []int
	}
	a1 := 0
	tests := []struct {
		name string
		args args
		want int
	}{
		{
			"Norm work 1",
			args{&sync.Mutex{}, &a1, []int{1, 2}},
			0,
		},
		{
			"Continue norm work 2",
			args{&sync.Mutex{}, &a1, []int{1, 2}},
			1,
		},
		{
			"Out of bounds",
			args{&sync.Mutex{}, &a1, []int{1, 2}},
			-1,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			f := &myFactorial{}
			if got := f.bookNum(tt.args.mu, tt.args.numInd, tt.args.numbers); got != tt.want {
				t.Errorf("bookNum() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_myFactorial_factorization(t *testing.T) {
	type args struct {
		n int
	}
	tests := []struct {
		name string
		args args
		want []string
	}{
		{
			"100",
			args{100},
			[]string{"2", "2", "5", "5"},
		},
		{
			"97",
			args{97},
			[]string{"97"},
		},
		{
			"-17",
			args{-17},
			[]string{"-1", "17"},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			f := &myFactorial{}
			got := f.factorization(tt.args.n)
			if !reflect.DeepEqual(tt.want, got) {
				t.Errorf("factorization() got = %v, want %v", got, tt.want)
			}
		})
	}
}
