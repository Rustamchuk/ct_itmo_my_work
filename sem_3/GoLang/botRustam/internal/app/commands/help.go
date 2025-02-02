package commands

import tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"

func (c *Commander) Help(inputMessage *tgbotapi.Message) {
	msg := tgbotapi.NewMessage(inputMessage.Chat.ID,
		"/help - список команд\n"+
			"/stories - список историй\n"+
			"/answers - список ответов\n"+
			"/addStory - добавить историю <текст>\n"+
			"/deleteStory - удалить историю по номеру\n"+
			"/addAnswer - добавить ответ <текст вопроса>||<текст ответа>\n"+
			"/deleteAnswer - удалить ответ <текст вопроса>\n"+
			"/links - мои контакты")
	c.bot.Send(msg)
}

func init() {
	commands["help"] = (*Commander).Help
}
