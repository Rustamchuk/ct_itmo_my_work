package main

import (
	"bytes"
	"os"
	"strings"
	"testing"
)

func TestHandleCd(t *testing.T) {
	// Сохраняем текущую директорию для восстановления после теста
	originalDir, err := os.Getwd()
	if err != nil {
		t.Fatalf("Failed to get current directory: %v", err)
	}
	defer os.Chdir(originalDir)

	// Получаем платформонезависимый временный каталог
	testDir := os.TempDir()
	handleCd([]string{"cd", testDir})
	currentDir, err := os.Getwd()
	if err != nil {
		t.Fatalf("Failed to get current directory: %v", err)
	}
	if currentDir != testDir {
		t.Errorf("Expected current directory to be %s, got %s", testDir, currentDir)
	}
}

func TestHandlePwd(t *testing.T) {
	// Сохраняем текущую директорию для проверки вывода
	expectedDir, err := os.Getwd()
	if err != nil {
		t.Fatalf("Failed to get current directory: %v", err)
	}

	// Перехватываем вывод
	old := os.Stdout
	r, w, _ := os.Pipe()
	os.Stdout = w

	handlePwd()

	// Восстанавливаем стандартный вывод
	w.Close()
	os.Stdout = old

	var buf bytes.Buffer
	buf.ReadFrom(r)
	output := strings.TrimSpace(buf.String())

	if output != expectedDir {
		t.Errorf("Expected output to be %s, got %s", expectedDir, output)
	}
}

func TestHandleEcho(t *testing.T) {
	args := []string{"echo", "hello", "world"}
	expected := "hello world"

	// Перехватываем вывод
	old := os.Stdout
	r, w, _ := os.Pipe()
	os.Stdout = w

	handleEcho(args)

	// Восстанавливаем стандартный вывод
	w.Close()
	os.Stdout = old

	var buf bytes.Buffer
	buf.ReadFrom(r)
	output := strings.TrimSpace(buf.String())

	if output != expected {
		t.Errorf("Expected output to be %s, got %s", expected, output)
	}
}
