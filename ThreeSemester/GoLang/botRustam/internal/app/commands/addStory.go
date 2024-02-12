package commands

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func (c *Commander) AddStory(inputMessage *tgbotapi.Message) {
	args := inputMessage.CommandArguments()
	n := c.tongue.AddStory(args)
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, fmt.Sprintf("Добавил историю номер %d\n", n))
	c.bot.Send(msg)
}

func init() {
	commands["addStory"] = (*Commander).AddStory
}
