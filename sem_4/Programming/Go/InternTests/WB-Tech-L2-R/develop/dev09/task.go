package main

import (
	"fmt"
	"io"
	"net/http"
	"net/url"
	"os"
	"path/filepath"
	"strings"
)

/*
=== Утилита wget ===

Реализовать утилиту wget с возможностью скачивать сайты целиком

Программа должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Usage: wget <URL>")
		return
	}

	downloadURL := os.Args[1]
	err := downloadPage(downloadURL)
	if err != nil {
		fmt.Printf("Error downloading page: %v\n", err)
	}
}

func downloadPage(pageURL string) error {
	resp, err := http.Get(pageURL)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	parsedURL, err := url.Parse(pageURL)
	if err != nil {
		return err
	}
	filename := filepath.Base(parsedURL.Path)
	if filename == "" || strings.HasSuffix(filename, "/") {
		filename = "index.html"
	}

	// Создаем файл в текущей директории
	file, err := os.Create(filepath.Join(".", filename))
	if err != nil {
		return err
	}
	defer file.Close()

	_, err = io.Copy(file, resp.Body)
	if err != nil {
		return err
	}

	return nil
}
