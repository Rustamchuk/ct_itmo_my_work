package pattern

import (
	"fmt"
	"log"
)

/*
	Реализовать паттерн «комманда».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Command_pattern
*/

/*
Применение:
	Применяется для объединения всех операций в отдельный объект.
	Параметризуем операции, получая возможность удобной отладки и менеджить операции,
	то есть добавлять их в очередь, например, для остановки операций или отмены какой-то операции.
Плюсы:
	Разделение логик запроса и выполнения
	Гибкость управления операциями
	Возможность отмены, повтора операций
Минусы:
	Большое количество классов (для каждой операции свой класс)
	Усложнение архитектуры (излишне для простых систем)
Применение:
	Прекрасно подходит для реализации операций по типу транзакций, которые еще можно отменять.
	В итоге получаем правильную последовательность транзакций. Возможность отмены и историю транзакций.
	Пример ниже:
*/

// Command interface
type Command interface {
	Execute() error
	Undo() error
}

// BankAccount - Receiver
type BankAccount struct {
	balance int
}

func (ba *BankAccount) Deposit(amount int) {
	ba.balance += amount
	fmt.Printf("Deposited %d$, balance now is %d$\n", amount, ba.balance)
}

func (ba *BankAccount) Withdraw(amount int) error {
	if ba.balance < amount {
		return fmt.Errorf("insufficient funds")
	}
	ba.balance -= amount
	fmt.Printf("Withdrew %d$, balance now is %d$\n", amount, ba.balance)
	return nil
}

// Transaction - Concrete Command
type Transaction struct {
	account *BankAccount
	amount  int
	success bool
}

func (t *Transaction) Execute() error {
	if err := t.account.Withdraw(t.amount); err != nil {
		log.Println("Failed to execute transaction:", err)
		t.success = false
		return err
	}
	t.success = true
	return nil
}

func (t *Transaction) Undo() error {
	if !t.success {
		return nil
	}
	t.account.Deposit(t.amount)
	return nil
}

// TransactionService - Invoker
type TransactionService struct {
	commands []Command
}

func (ts *TransactionService) AddCommand(cmd Command) {
	ts.commands = append(ts.commands, cmd)
}

func (ts *TransactionService) ExecuteCommands() {
	for _, cmd := range ts.commands {
		if err := cmd.Execute(); err != nil {
			log.Println("Error executing command:", err)
			break
		}
	}
}

func (ts *TransactionService) UndoLastCommand() {
	if len(ts.commands) == 0 {
		return
	}
	lastCmd := ts.commands[len(ts.commands)-1]
	if err := lastCmd.Undo(); err != nil {
		log.Println("Error undoing command:", err)
	}
}

func main() {
	account := &BankAccount{balance: 1000}
	ts := TransactionService{}

	ts.AddCommand(&Transaction{account: account, amount: 100})
	ts.AddCommand(&Transaction{account: account, amount: 200})

	ts.ExecuteCommands()
	ts.UndoLastCommand()
}
