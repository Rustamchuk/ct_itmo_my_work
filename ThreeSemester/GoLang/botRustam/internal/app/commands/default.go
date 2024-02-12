package commands

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"math/rand"
)

func (c *Commander) Default(inputMessage *tgbotapi.Message) {
	ans := ""

	defer func() {
		msg := tgbotapi.NewMessage(inputMessage.Chat.ID, ans)
		c.bot.Send(msg)
	}()

	res, ok := c.tongue.Answers()[inputMessage.Text]
	if ok {
		ans = res.Phrase
		return
	}

	randomNumber := rand.Intn(2)
	if randomNumber == 1 {
		ans = "Здесь была гифка но меня забанили. Вот тебе шутка\n\n" +
			"https://randstuff.ru/joke/"
		return
	}
	stories := c.tongue.Stories()
	randomNumber = rand.Intn(len(stories))

	ans = stories[randomNumber].Phrase
}
