package bank

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestBank(t *testing.T) {
	clock := NewMockTime()
	myAccount := NewAccount(clock)
	myAccount.TopUp(100)
	one := clock.t
	myAccount.TopUp(25)
	two := clock.t
	assert.Equal(t, myAccount.Balance(), 125)

	myAccount.Withdraw(124)
	three := clock.t
	myAccount.Withdraw(2)
	assert.Equal(t, myAccount.Balance(), 1)

	assert.Equal(t, myAccount.Operations(), []Operation{
		{one, TopUpOp, 100, 100},
		{two, TopUpOp, 25, 125},
		{three, WithdrawOp, 124, 1},
	})

	assert.Equal(t,
		"2023-03-18 12:34:07 +100 100\n"+
			"2023-03-18 12:34:37 +25 125\n"+
			"2023-03-18 12:35:07 -124 1",
		myAccount.Statement())
}
