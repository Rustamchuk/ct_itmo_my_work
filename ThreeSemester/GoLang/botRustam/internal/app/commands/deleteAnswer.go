package commands

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func (c *Commander) DeleteAnswer(inputMessage *tgbotapi.Message) {
	args := inputMessage.CommandArguments()
	c.tongue.DeleteAnswer(args)
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, "Удалил")
	c.bot.Send(msg)
}

func init() {
	commands["deleteAnswer"] = (*Commander).DeleteAnswer
}
