package commands

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"log"
	"strconv"
)

func (c *Commander) DeleteStory(inputMessage *tgbotapi.Message) {
	args := inputMessage.CommandArguments()
	arg, err := strconv.Atoi(args)
	if err != nil {
		log.Println("wrong arg", args)
		return
	}
	c.tongue.DeleteStory(arg)
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, "Удалил")
	c.bot.Send(msg)
}

func init() {
	commands["deleteStory"] = (*Commander).DeleteStory
}
