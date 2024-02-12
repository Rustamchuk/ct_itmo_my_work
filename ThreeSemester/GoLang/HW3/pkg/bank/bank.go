package bank

import (
	"fmt"
	"strings"
	"time"
)

const (
	TopUpOp OperationType = iota
	WithdrawOp
)

type OperationType int64

type Clock interface {
	Now() time.Time
}

func NewRealTime() *RealClock {
	return &RealClock{}
}

type RealClock struct{}

func (c *RealClock) Now() time.Time {
	return time.Now()
}

type Operation struct {
	OpTime   time.Time
	OpType   OperationType
	OpAmount int
	Balance  int
}

func (o Operation) String() string {
	var format string
	if o.OpType == TopUpOp {
		format = `%s +%d %d`
	} else {
		format = `%s -%d %d`
	}
	return fmt.Sprintf(format, o.OpTime.String()[:19], o.OpAmount, o.Balance)
}

type Account interface {
	TopUp(amount int) bool
	Withdraw(amount int) bool
	Operations() []Operation
	Statement() string
	Balance() int
}

func NewAccount(clock Clock) Account {
	return &MyAccount{
		operations: []Operation{},
		balance:    0,
		clock:      clock,
	}
}

type MyAccount struct {
	operations []Operation
	balance    int
	clock      Clock
}

func (a *MyAccount) TopUp(amount int) bool {
	if a.Balance() < 0 || amount <= 0 {
		return false
	}
	a.balance += amount
	a.operations = append(a.operations, Operation{
		OpTime:   a.clock.Now(),
		OpType:   TopUpOp,
		OpAmount: amount,
		Balance:  a.balance,
	})
	return true
}

func (a *MyAccount) Withdraw(amount int) bool {
	if a.Balance() < amount || amount <= 0 {
		return false
	}
	a.balance -= amount
	a.operations = append(a.operations, Operation{
		OpTime:   a.clock.Now(),
		OpType:   WithdrawOp,
		OpAmount: amount,
		Balance:  a.balance,
	})
	return true
}

func (a *MyAccount) Operations() []Operation {
	return a.operations
}

func (a *MyAccount) Statement() string {
	n := len(a.operations)
	opers := make([]string, n)
	for i := 0; i < n; i++ {
		opers[i] = a.operations[i].String()
	}
	return strings.Join(opers, "\n")
}

func (a *MyAccount) Balance() int {
	return a.balance
}
