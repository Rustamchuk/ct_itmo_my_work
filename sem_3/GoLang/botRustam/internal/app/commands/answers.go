package commands

import tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"

func (c *Commander) Answers(inputMessage *tgbotapi.Message) {
	ans := "Список ответов на вопросы: \n\n"
	for key, txt := range c.tongue.Answers() {
		ans += key
		ans += " -- "
		ans += txt.Phrase
		ans += "\n\n"
	}
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, ans)
	c.bot.Send(msg)
}

func init() {
	commands["answers"] = (*Commander).Answers
}
