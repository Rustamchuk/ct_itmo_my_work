package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"
)

/*
Реализовать постоянную запись данных в канал (главный поток).
Реализовать набор из N воркеров, которые читают произвольные
данные из канала и выводят в stdout. Необходима возможность
выбора количества воркеров при старте.

Программа должна завершаться по нажатию Ctrl+C.
Выбрать и обосновать способ завершения работы всех воркеров.
*/

// Worker реализуем структура для каждого воркера, чтобы следить, кто выводит данные.
type Worker struct {
	ID int // ID текущего воркера
}

// work метод Worker. Пока данный канал не закрыт берем data и выводим. При выходе уменьшим WaitGroup -1 процесс
func (w *Worker) work(wg *sync.WaitGroup, dataChan <-chan string) {
	defer wg.Done()

	for data := range dataChan {
		fmt.Println(fmt.Sprintf("[Worker № %d]\n%s", w.ID, data))
	}
}

func main() {
	var (
		workersNum  int                                        // Количество воркеров
		dataChan    chan string                                // Канал, куда будем отправлять текст
		ctx, cancel = context.WithCancel(context.Background()) // Контекст с отменой, чтобы остановить отправку текста
		wg          sync.WaitGroup                             // WaitGroup для подсчета количества процессов в работе
	)

	// Получаем количество воркеров
	fmt.Print("Enter workers count: ")
	fmt.Scan(&workersNum)

	// Сколько воркеров, такой же и буфер канала
	dataChan = make(chan string, workersNum)

	// Запускаем все воркеры
	for i := 1; i <= workersNum; i++ {
		worker := Worker{ID: i}
		wg.Add(1)                     // WaitGroup увеличиваем +1 процесс
		go worker.work(&wg, dataChan) // Запускаем горутину. Воркер будет в ожидании текста
	}

	// Горутина для отправки текста
	go func() {
		for { // Бeсконечный цикл
			select {
			case <-ctx.Done(): // Если контекст отменен, надо перестать запись в канал
				return
			case dataChan <- "Some data": // Пробуем записать в канал
			}
		}
		/*
			Почему тут не case + default, а case + case? Если будет default, то блокировка будет в нем.
			Будет ожидание, когда dataChan позволит записать данные.
			В моем же случае, если dataChan не готов принять данные, то благодаря select мы ждем не один канал.
			Мы ждем сигнал об отмене контекста и о разрешении на запись, смотрим на оба случая.
			На случай, если во время ожидания, контекст отменят.
		*/
	}()

	// Обработка сигнала завершения (Ctrl+C)
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt, syscall.SIGTERM)
	<-c

	// Отменяем контекст для остановки записи новых текстов
	cancel()

	// Ждем секунду, чтобы последний текст добавился
	time.Sleep(time.Second)

	// Закрытие канала, что приведет к завершению воркеров
	close(dataChan)

	// Ожидание завершения всех воркеров
	wg.Wait()
	fmt.Println("Program finished.")
}
