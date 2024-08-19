package main

import (
	"fmt"
	"time"
)

/*
Реализовать собственную функцию sleep.
*/

// mySleep приостанавливает выполнение программы на заданное количество секунд
func mySleep(seconds int) {
	// Стоим в блокировке, пока не придет сигнал из канала time
	<-time.After(time.Duration(seconds) * time.Second)
}

func main() {
	fmt.Println("Начало сна...")
	mySleep(2) // Приостанавливаем выполнение на 2 секунды
	fmt.Println("Конец сна.")
}
