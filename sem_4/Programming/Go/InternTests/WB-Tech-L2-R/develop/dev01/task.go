package main

/*
=== Базовая задача ===

Создать программу печатающую точное время с использованием NTP библиотеки.Инициализировать как go module.
Использовать библиотеку https://github.com/beevik/ntp.
Написать программу печатающую текущее время / точное время с использованием этой библиотеки.

Программа должна быть оформлена с использованием как go module.
Программа должна корректно обрабатывать ошибки библиотеки: распечатывать их в STDERR и возвращать ненулевой код выхода в OS.
Программа должна проходить проверки go vet и golint.
*/

import (
	"fmt"
	"os"
	"time"

	"github.com/beevik/ntp"
)

// Адрес сервера
const timeHost = "0.beevik-ntp.pool.ntp.org"

func getNTPTime(host string) (time.Time, error) {
	ntpTime, err := ntp.Time(host)
	if err != nil {
		return time.Time{}, err
	}
	return ntpTime, nil
}

func main() {
	// Получаем точное время
	ntpTime, err := getNTPTime(timeHost)
	if err != nil {
		// Если не получилось, выводим ошибку и завершаем программу
		fmt.Fprintf(os.Stderr, "Ошибка при получении времени NTP: %v\n", err)
		os.Exit(1)
	}

	// Сравниваем текущее времся двух библиотек
	fmt.Printf("Текущее время: %v\n", time.Now())
	fmt.Printf("Точное время (NTP): %v\n", ntpTime)
}
