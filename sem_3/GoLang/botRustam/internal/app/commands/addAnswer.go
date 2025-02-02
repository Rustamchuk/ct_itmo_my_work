package commands

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"strings"
)

func (c *Commander) AddAnswer(inputMessage *tgbotapi.Message) {
	args := inputMessage.CommandArguments()
	res := strings.Split(args, "||")
	c.tongue.AddAnswer(res[0], res[1])
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, fmt.Sprintf("Добавил ответ\n"))
	c.bot.Send(msg)
}

func init() {
	commands["addAnswer"] = (*Commander).AddAnswer
}
