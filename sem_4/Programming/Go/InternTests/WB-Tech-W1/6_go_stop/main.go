package main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

/*
Реализовать все возможные способы остановки выполнения горутины.
*/

// Остановка горутины по контексту
func stopByContext(ctx context.Context, wg *sync.WaitGroup) {
	/*
		Раз в секунду, если контекст не отменен, выводим данные, иначе выходим из метода.
	*/
	defer wg.Done()
	for i := 1; ; i++ {
		select {
		case <-ctx.Done(): // Пробуем получить сигнал из канала Done
			fmt.Println("Остановлено по контексту")
			return
		default:
			fmt.Printf("Работает: контекст %d\n", i)
		}
		time.Sleep(1 * time.Second)
	}
}

// Остановка горутины по таймеру
func stopByTimer(wg *sync.WaitGroup, seconds time.Duration) {
	/*
		Раз в секунду, если таймер не окончен, выводим данные, иначе выходим из метода.
	*/
	defer wg.Done()
	timer := time.NewTimer(seconds)
	for i := 1; ; i++ {
		select {
		case <-timer.C: // Пробуем получить сигнал из канала таймера
			fmt.Println("Остановлено по таймеру")
			return
		default:
			fmt.Printf("Работает: таймер %d\n", i)
		}
		time.Sleep(1 * time.Second)
	}
}

// Остановка горутины по флагу
func stopByFlag(flag *bool, wg *sync.WaitGroup) {
	/*
		Раз в секунду, если флаг не поднят, выводим данные, иначе выходим из метода.
		Флаг передаем по ссылке, чтобы его подняли удаленно.
	*/
	defer wg.Done()
	for i := 1; ; i++ {
		if *flag { // Проверка флага
			fmt.Println("Остановлено по флагу")
			return
		}
		fmt.Printf("Работает: флаг %d\n", i)
		time.Sleep(1 * time.Second)
	}
}

// Остановка горутины по каналу
func stopByChannel(stopChan <-chan struct{}, wg *sync.WaitGroup) {
	/*
		Раз в секунду, если в канале stop нет данных, выводим данные, иначе выходим из метода.
	*/
	defer wg.Done()
	for i := 1; ; i++ {
		select {
		case <-stopChan: // Пробуем получить сигнал из канала остановки
			fmt.Println("Остановлено по каналу")
			return
		default:
			fmt.Println(fmt.Sprintf("Работает: канал %d", i))
		}
		time.Sleep(1 * time.Second)
	}
}

func main() {
	var (
		wg          sync.WaitGroup    // WaitGroup для отслеживания работающих случаев
		waitForNext = 4 * time.Second // Будем отменять горутину через 4 сек работы
	)

	// Контекст
	// Добавляем 1 процесс
	wg.Add(1)

	ctx, cancel := context.WithCancel(context.Background()) // Контекст с отменой
	go stopByContext(ctx, &wg)                              // Начинаем горутину
	time.Sleep(waitForNext)                                 // Ждем 4 секунды
	cancel()                                                // Отменяем контекст

	// Ожидание фактического завершения горутины
	wg.Wait()

	// Таймер
	wg.Add(1)

	go stopByTimer(&wg, waitForNext) // Запуск горутины

	// Ожидание фактического завершения горутины
	wg.Wait()

	// Флаг
	wg.Add(1)

	stopFlag := false // Флаг опущенный
	go stopByFlag(&stopFlag, &wg)
	time.Sleep(waitForNext)
	stopFlag = true // Подняли флаг для отмены

	// Ожидание фактического завершения горутины
	wg.Wait()

	// Канал
	wg.Add(1)

	stopChan := make(chan struct{}) // Канал для отмены
	go stopByChannel(stopChan, &wg) // Запуск горутины
	time.Sleep(waitForNext)         // Ждем 4 секунды
	close(stopChan)                 // Закрываем канал

	// Ожидание фактического завершения горутины
	wg.Wait()
	fmt.Println("Все горутины остановлены")
}
