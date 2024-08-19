package main

import (
	"encoding/json"
	"fmt"
)

/*
Реализовать паттерн «адаптер» на любом примере.
*/

// StringDataProvider GetData возвращает список строк
type StringDataProvider interface {
	GetData() []string
}

// JsonDataProvider GetData возвращает строку в формате JSON
type JsonDataProvider interface {
	GetData() string
}

// OldSystem старая система, возвращает данные в виде списка строк
type OldSystem struct{}

func (os *OldSystem) GetData() []string {
	return []string{"Rustam", "Vova", "Ivan"}
}

// Adapter преобразует данные из StringDataProvider в JsonDataProvider
type Adapter struct {
	StringData StringDataProvider
}

func (a *Adapter) GetData() string {
	data := a.StringData.GetData()      // Вызываем метод OldSystem
	jsonData, err := json.Marshal(data) // Преобразуем в строку (массив байт) в формате JSON
	if err != nil {
		return ""
	}
	return string(jsonData)
}

func main() {
	// Преобразую в JsonDataProvider
	// Между старой и новой реализацией Adapter
	// Кладу StringDataProvider
	adapter := JsonDataProvider(
		&Adapter{
			StringData: &OldSystem{},
		},
	)

	fmt.Println("Данные в формате JSON:", adapter.GetData())
}
