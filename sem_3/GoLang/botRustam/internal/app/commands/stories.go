package commands

import (
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func (c *Commander) Stories(inputMessage *tgbotapi.Message) {
	ans := "Список историй: \n\n"
	for i, txt := range c.tongue.Stories() {
		ans += fmt.Sprintf("%d) %s\n\n", i+1, txt.Phrase)
	}
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, ans)
	c.bot.Send(msg)
}

func init() {
	commands["stories"] = (*Commander).Stories
}
