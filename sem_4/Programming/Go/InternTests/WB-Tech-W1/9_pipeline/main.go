package main

import (
	"fmt"
	"sync"
)

/*
Разработать конвейер чисел.
Даны два канала: в первый пишутся числа (x) из массива,
во второй — результат операции x*2,
после чего данные из второго канала должны выводиться в stdout.
*/

// Пайплайн для заполнения канала
func sendPipeline(wg *sync.WaitGroup, x []int) <-chan int {
	/*
		Создаем канал и сразу возвращаем его.
		В отдельном потоке отправляем число из слайса в отправленный канал.
		Дальше, пока кто-то извне не возьмет наше число, стоим в блоке.
		Если снова можем отправить число, отправляем.
		По завершении закроем канал, чтобы извне, прекратили ждать новые значения.
	*/
	outChan := make(chan int)
	wg.Add(1)
	go func() {
		defer func() {
			close(outChan)
			wg.Done()
		}()
		for _, num := range x {
			outChan <- num
		}
	}()
	return outChan
}

// Пайплайн для возведения в квадрат
func squarePipeline(wg *sync.WaitGroup, numChan <-chan int) <-chan int {
	/*
		Создаем канал и сразу возвращаем его.
		В отдельном потоке.
		Если можем достать число из данного канала, то берем.
		Отправляем это число в новый канал.
		Дальше, пока кто-то извне не возьмет наше число, стоим в блоке.
		Если снова можем отправить число, отправляем.
		По завершении закроем канал, чтобы извне, прекратили ждать новые значения.
	*/
	squareChan := make(chan int)
	wg.Add(1)
	go func() {
		defer func() {
			close(squareChan)
			wg.Done()
		}()
		for num := range numChan {
			squareChan <- num * num
		}
	}()
	return squareChan
}

// Пайплайн для вывода результатов
func printPipeline(wg *sync.WaitGroup, numChan <-chan int) {
	/*
		Аналогично предыдущему, но выводим, не записываем в канал.
	*/
	wg.Add(1)
	go func() {
		defer wg.Done()
		for num := range numChan {
			fmt.Println(num)
		}
	}()
}

func main() {
	var (
		x  = []int{1, 2, 3, 4, 5, 6, 7, 8}
		wg sync.WaitGroup
	)

	// Записываем числа x в канал
	numChan := sendPipeline(&wg, x)
	// Как только появляется в канале число возводим в квадрат и записываем в следующий канал
	squareChan := squarePipeline(&wg, numChan)
	// Как только в канале квадратов появляется результат, выводим его
	printPipeline(&wg, squareChan)

	wg.Wait()
}
