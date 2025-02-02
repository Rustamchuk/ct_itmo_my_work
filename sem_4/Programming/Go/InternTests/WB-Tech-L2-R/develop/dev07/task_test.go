package main

import (
	"testing"
	"time"
)

// TestOrNoChannels проверяет поведение функции or с нулевым количеством каналов.
func TestOrNoChannels(t *testing.T) {
	result := or()
	select {
	case <-result:
		// Ожидаем, что канал будет немедленно закрыт.
	case <-time.After(1 * time.Second):
		t.Fatal("Expected immediate closure of the channel, but it was not closed")
	}
}

// TestOrOneChannel проверяет поведение функции or с одним каналом.
func TestOrOneChannel(t *testing.T) {
	c := make(chan interface{})
	go func() {
		time.Sleep(100 * time.Millisecond)
		close(c)
	}()

	start := time.Now()
	<-or(c)
	duration := time.Since(start)

	if duration >= 200*time.Millisecond {
		t.Fatal("Expected channel to close within 100ms, but it took longer")
	}
}

// TestOrMultipleChannels проверяет функцию or с несколькими каналами.
func TestOrMultipleChannels(t *testing.T) {
	c1 := make(chan interface{})
	c2 := make(chan interface{})
	c3 := make(chan interface{})

	go func() {
		time.Sleep(50 * time.Millisecond)
		close(c1)
	}()
	go func() {
		time.Sleep(100 * time.Millisecond)
		close(c2)
	}()
	go func() {
		time.Sleep(150 * time.Millisecond)
		close(c3)
	}()

	start := time.Now()
	<-or(c1, c2, c3)
	duration := time.Since(start)

	if duration >= 100*time.Millisecond {
		t.Fatal("Expected the or channel to close as soon as the first channel closes, but it took longer")
	}
}

// TestOrWithImmediateClose проверяет функцию or, когда один из каналов уже закрыт.
func TestOrWithImmediateClose(t *testing.T) {
	c1 := make(chan interface{})
	close(c1)
	c2 := sig(1 * time.Second)

	start := time.Now()
	<-or(c1, c2)
	duration := time.Since(start)

	if duration >= 50*time.Millisecond {
		t.Fatal("Expected the or channel to close immediately because one channel was already closed")
	}
}
