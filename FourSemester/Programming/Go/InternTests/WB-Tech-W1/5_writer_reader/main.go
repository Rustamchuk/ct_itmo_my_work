package main

import (
	"context"
	"fmt"
	"time"
)

/*
Разработать программу, которая будет последовательно отправлять значения в канал,
а с другой стороны канала — читать.
По истечению N секунд программа должна завершаться.
*/

func main() {
	var (
		N           int                                        // N секунд
		dataChan    = make(chan int)                           // Канал, куда отправляем текст и откуда читаем
		ctx, cancel = context.WithCancel(context.Background()) // Контекст с отменой, чтобы остановить Reader Writer
	)

	fmt.Print("Enter timer, N seconds: ")
	fmt.Scan(&N)

	// Горутина для отправки данных в канал (Writer)
	go func() {
		counter := 0 // Будем отправлять значение счетчика для анализа
		for {
			select {
			case <-ctx.Done(): // Если контекст отменили, то прекращаем
				return
			default:
				dataChan <- counter
				counter++
				time.Sleep(1 * time.Second) // Имитация задержки
			}
		}
	}()

	// Горутина для чтения данных из канала (Reader)
	go func() {
		for {
			select {
			case <-ctx.Done(): // Если контекст отменили, то прекращаем
				return
			case data := <-dataChan: // Пробуем получить значение из канала
				fmt.Println("Получено:", data)
			}
		}
	}()

	// Таймер для завершения программы
	time.AfterFunc(time.Duration(N)*time.Second, func() {
		fmt.Println("Время вышло")
		cancel() // Отправляем сигнал для завершения горутин
	})

	<-ctx.Done()            // Сами ждем, когда счетчик закончится
	time.Sleep(time.Second) // Надо дать Writer время для завершения
	close(dataChan)         // Закрываем канал после завершения работы
	fmt.Println("Программа завершена.")
}
