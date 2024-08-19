package pattern

import "fmt"

/*
	Реализовать паттерн «фасад».
Объяснить применимость паттерна, его плюсы и минусы,а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Facade_pattern
*/

/*
Применимость:
	Фасад удобен для упрощения работы с какой-то сложной системой, состоящей из нескольких подсистем.
	Чтобы обращаться к одному представителю главному, который уже сам будет менеджить свои подсистемы.
	Вся работа системы реализована внутри самой системы, от внешних сервисов требуется только инициализация.
Плюсы:
	Упрощенный интерфейс для пользователя нашим сервисом.
	Изоляция пользователя от компонентов системы -> независимость и переиспользуемость.
	Умменьшение зависимостей между системами и пользователями.
Минусы:
	Если много сервисов начнут пользоваться нашим фасадом, возникнет проблема поддержки и расширения,
	какому-то сервису понадобится поменять метод фасада, а другой будет против (грубо говоря).
Опыт работы:
	Часто использую Фасад при реализации модуля Service (иногда Repository). Тем самым объединяю сервисы в один сервис.
*/

/*
Пример из моего микросервиса: (для запуска придется добавить структуры и конструкторы)

type Authorization interface {
	CreateUser(user model.User) (int, error)
	GenerateToken(username, password string) (string, error)
	ParseToken(token string) (int, error)
}

type ActorService interface {
	CreateActor(ctx context.Context, actor *model.Actor) error
	UpdateActor(ctx context.Context, actor *model.Actor) error
	DeleteActor(ctx context.Context, id int64) error
	GetActors(ctx context.Context) ([]model.Actor, error)
}

type MovieService interface {
	CreateMovie(ctx context.Context, movie *model.Movie) error
	UpdateMovie(ctx context.Context, movie *model.Movie) error
	DeleteMovie(ctx context.Context, id int64) error
	GetMovies(ctx context.Context, sortBy string) ([]model.Movie, error)
	SearchMovies(ctx context.Context, titleFragment string) ([]model.Movie, error)
}

type Service struct {
	Authorization
	ActorService
	MovieService
	userRoles []string
}

func NewService(repos *repository.Repository) *Service {
	return &Service{
		Authorization: NewAuthService(repos.Authorization),
		ActorService:  NewActorServiceImplementation(repos.ActorRepo),
		MovieService:  NewMovieServiceImplementation(repos.MovieRepo),
		userRoles:     make([]string, 1),
	}
}
*/

// Работающий пример из головы

// Системы
type Monitor struct{}

func (m *Monitor) TurnOn() {
	fmt.Println("Монитор включен")
}

type Keyboard struct{}

func (k *Keyboard) Connect() {
	fmt.Println("Клавиатура подключена")
}

type Mouse struct{}

func (m *Mouse) Connect() {
	fmt.Println("Мышь подключена")
}

type SoundSystem struct{}

func (s *SoundSystem) TurnOn() {
	fmt.Println("Звуковая система включена")
}

// Фасад
type ComputerFacade struct {
	monitor     Monitor
	keyboard    Keyboard
	mouse       Mouse
	soundSystem SoundSystem
}

func (c *ComputerFacade) Start() {
	c.monitor.TurnOn()
	c.keyboard.Connect()
	c.mouse.Connect()
	c.soundSystem.TurnOn()
}

func main() {
	computer := ComputerFacade{}
	computer.Start()
}
