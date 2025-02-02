package phrases

var stories = []*Phrase{
	NewPhrase("Животные показывают друг другу фокусы. " +
		"Хамелеон меняет цвет, все такие: 'вау, как круто, супер!' " +
		"Осьминог такой: Подержи мое пиво. Подержи мое пиво. Подержи мое пиво." +
		" Подержи мое пиво. Подержи мое пиво. Подержи мое пиво. Подержи мое пиво. Подержи мое пиво."),
	NewPhrase("Голэнг лучший язык в мире. Скала для лузеров"),
}

var answers = map[string]*Phrase{
	"привет":   NewPhrase("Здарова!"),
	"как дела": NewPhrase("Норм норм, у тебя?"),
	"пока":     NewPhrase("Давай 👋"),
}

type Phrase struct {
	Phrase string
}

func NewPhrase(phrase string) *Phrase {
	return &Phrase{Phrase: phrase}
}
