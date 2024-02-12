package commands

import (
	"fmt"
	"github.com/Rustamchuk/botRustam/internal/speaking/phrases"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"log"
	"strings"
)

var commands = map[string]func(c *Commander, inputMessage *tgbotapi.Message){}

type Commander struct {
	bot    *tgbotapi.BotAPI
	tongue *phrases.Service
}

func NewCommander(bot *tgbotapi.BotAPI, tongue *phrases.Service) *Commander {
	return &Commander{
		bot:    bot,
		tongue: tongue,
	}
}

func (c *Commander) ChooseMove(update *tgbotapi.Update) {
	defer func() {
		if panicValue := recover(); panicValue != nil {
			log.Printf("recover from panic: %v\n", panicValue)
		}
	}()

	if update.CallbackQuery != nil {
		args := strings.Split(update.CallbackQuery.Data, "_")
		msg := tgbotapi.NewMessage(
			update.CallbackQuery.Message.Chat.ID,
			fmt.Sprintf("Command: %s\n", args[0])+
				fmt.Sprintf("Offset: %s\n", args[1]),
		)
		c.bot.Send(msg)
		return
	}

	if update.Message == nil {
		return
	}

	command, ok := commands[update.Message.Command()]
	if ok {
		command(c, update.Message)
	} else {
		c.Default(update.Message)
	}
}
