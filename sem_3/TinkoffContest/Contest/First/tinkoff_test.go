package main

import (
	"math/rand"
	"slices"
	"sort"
	"testing"
	"time"
)

func Test_check(t *testing.T) {
	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 100; i++ {
		// Генерация случайной строки
		cur := []rune("TINKOFF")
		for i := len(cur) - 1; i > 0; i-- {
			j := rand.Intn(i + 1)
			cur[i], cur[j] = cur[j], cur[i]
		}
		word := string(cur)
		// Проверка результата
		expected := check(string(cur))
		sort.Slice(cur, func(i, j int) bool {
			return cur[i] < cur[j]
		})
		actual := "No"
		if slices.Equal(cur, []rune{'F', 'F', 'I', 'K', 'N', 'O', 'T'}) {
			actual = "Yes"
		}
		if expected != actual {
			t.Errorf("Test failed for input %s: expected %s, got %s", string(cur), expected, actual)
		}

		t.Errorf("Test failed for input %s: expected %s, got %s", word, expected, actual)
	}
}
