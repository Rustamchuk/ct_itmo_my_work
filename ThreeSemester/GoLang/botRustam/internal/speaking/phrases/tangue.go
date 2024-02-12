package phrases

type Service struct {
}

func NewService() *Service {
	return &Service{}
}

func (s *Service) Answers() map[string]*Phrase {
	return answers
}

func (s *Service) Stories() []*Phrase {
	return stories
}

func (s *Service) AddAnswer(phrase, answer string) {
	answers[phrase] = NewPhrase(answer)
}

func (s *Service) DeleteAnswer(phrase string) {
	delete(answers, phrase)
}

func (s *Service) AddStory(story string) int {
	stories = append(stories, NewPhrase(story))
	return len(stories)
}

func (s *Service) DeleteStory(ind int) {
	stories = append(stories[:ind], stories[ind+1:]...)
}
