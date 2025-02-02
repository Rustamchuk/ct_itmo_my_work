package main

import (
	"math"
	"math/rand"
	"testing"
	"time"
)

func Test_maxNum(t *testing.T) {
	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 100; i++ {
		// Генерация случайной строки
		cur := make([]int, rand.Intn(6))
		L := rand.Intn(100)
		var sum int = 0
		for i := 0; i < len(cur); i++ {
			cur[i] = rand.Intn(10) + 1
			sum += int(cur[i])
		}
		maxNumm := 0
		for i := 1; i <= L; i++ {
			temp := i
			for _, num := range cur {
				if temp >= num {
					temp -= num
				}
			}
			if temp > maxNumm {
				maxNumm = temp
			}
		}

		minn, lastmin := math.MaxInt32, math.MaxInt32
		sum, last, c, curr := 0, 0, 0, 0
		for i := 0; i < len(cur); i++ {
			c = cur[i]
			if c > L {
				continue
			}
			if minn > c {
				minn = c
			}
			if lastmin > c && c > minn {
				lastmin = c
			}
			if sum+c <= L {
				sum += c
			}
			l := last
			if l >= c {
				l = maxNum(last-c, last-(absNum(last-c)+1))
			}
			curr = maxNum(L-sum, maxNum(minn-1, l))
			last = curr
		}
		if minn > L {
			last = L
		}

		if maxNumm != minNum(last, L) {
			t.Errorf("Test failed for input %v - %v: expected %v, got %v", cur, L, minNum(last, L), maxNumm)
		}
	}
}
