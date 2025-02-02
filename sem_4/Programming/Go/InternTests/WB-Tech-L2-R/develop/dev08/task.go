package main

import (
	"bufio"
	"fmt"
	"io"
	"net"
	"os"
	"os/exec"
	"strconv"
	"strings"
	"syscall"
)

/*
=== Взаимодействие с ОС ===

Необходимо реализовать собственный шелл

встроенные команды: cd/pwd/echo/kill/ps
поддержать fork/exec команды
конвеер на пайпах

Реализовать утилиту netcat (nc) клиент
принимать данные из stdin и отправлять в соединение (tcp/udp)
Программа должна проходить все тесты. Код должен проходить проверки go vet и golint.
*/

func main() {
	reader := bufio.NewReader(os.Stdin)
	for {
		fmt.Print("> ")
		input, _ := reader.ReadString('\n')
		input = strings.TrimSpace(input)
		args := strings.Split(input, " ")

		if len(args) == 0 {
			continue
		}

		switch args[0] {
		case "cd":
			handleCd(args)
		case "pwd":
			handlePwd()
		case "echo":
			handleEcho(args)
		case "kill":
			handleKill(args)
		case "ps":
			handlePs(args)
		case "nc":
			handleNetcat(args)
		case "exit":
			os.Exit(0)
		default:
			executeExternalCommand(args)
		}
	}
}

func handleCd(args []string) {
	if len(args) < 2 {
		fmt.Println("cd: too few arguments")
	} else {
		err := os.Chdir(args[1])
		if err != nil {
			fmt.Println("cd: ", err)
		}
	}
}

func handlePwd() {
	dir, err := os.Getwd()
	if err != nil {
		fmt.Println("pwd: ", err)
	} else {
		fmt.Println(dir)
	}
}

func handleEcho(args []string) {
	fmt.Println(strings.Join(args[1:], " "))
}

func handleKill(args []string) {
	if len(args) < 3 {
		fmt.Println("kill: not enough arguments")
		return
	}

	pid, err := strconv.Atoi(args[2])
	if err != nil {
		fmt.Println("kill: invalid process id")
		return
	}

	// Получаем процесс по PID
	process, err := os.FindProcess(pid)
	if err != nil {
		fmt.Println("kill: failed to find process")
		return
	}

	// Отправляем сигнал SIGTERM, можно изменить на другой сигнал
	err = process.Signal(syscall.SIGTERM)
	if err != nil {
		fmt.Println("kill: failed to send signal")
	}
}

func handlePs(args []string) {
	cmd := exec.Command("ps", args[1:]...)
	cmd.Stdout = os.Stdout
	cmd.Run()
}

func handleNetcat(args []string) {
	if len(args) < 3 {
		fmt.Println("nc: not enough arguments")
	} else {
		netcat(args[1], args[2])
	}
}

func executeExternalCommand(args []string) {
	cmd := exec.Command(args[0], args[1:]...)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.Run()
}

func netcat(host, port string) {
	conn, err := net.Dial("tcp", host+":"+port)
	if err != nil {
		fmt.Println("Failed to connect:", err)
		return
	}
	defer conn.Close()

	go io.Copy(conn, os.Stdin)
	io.Copy(os.Stdout, conn)
}
