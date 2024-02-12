package commands

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func (c *Commander) Links(inputMessage *tgbotapi.Message) {

	msg := tgbotapi.NewMessage(inputMessage.Chat.ID, "Вот вот")

	msg.ReplyMarkup = tgbotapi.NewInlineKeyboardMarkup(
		tgbotapi.NewInlineKeyboardRow(
			tgbotapi.NewInlineKeyboardButtonURL("VK", "https://vk.com/rustamchik1"),
		),
		tgbotapi.NewInlineKeyboardRow(
			tgbotapi.NewInlineKeyboardButtonURL("INSTA", "https://instagram.com/rustikspb?igshid=YTQwZjQ0NmI0OA=="),
		),
		tgbotapi.NewInlineKeyboardRow(
			tgbotapi.NewInlineKeyboardButtonURL("Strava", "https://www.strava.com/athletes/113559955"),
		),
		tgbotapi.NewInlineKeyboardRow(
			tgbotapi.NewInlineKeyboardButtonURL("Boosty", "https://boosty.to/rustamchik/donate"),
		),
		tgbotapi.NewInlineKeyboardRow(
			tgbotapi.NewInlineKeyboardButtonURL("OnlyFans", "https://www.youtube.com/watch?v=3wQiyOmAUOA"),
		),
	)

	c.bot.Send(msg)
}

func init() {
	commands["links"] = (*Commander).Links
}
