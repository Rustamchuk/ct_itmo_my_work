## Домашняя работа №2

Реализуйте структуру с семантикой облака тегов.
- теги - это строки
- облако тегов запоминает все добавленные теги и их частотность
- облако тегов позволяет выбирать N самых частотных тегов.

Требования:
- Названия структур и публичных методов, а также их параметры должны совпадать со спецификацией в [./tagcloud/specification.go](./tagcloud/specification.go)
- Детальная спецификация на то, что должен делать каждый публичный метод описана в [./tagcloud/specification.go](./tagcloud/specification.go)
- Юнит-тесты, описанные в [./tagcloud/specification_test.go](./tagcloud/specification_test.go), должны проходить без модификаций.

Можно писать реализацию прямо в [./tagcloud/specification.go](./tagcloud/specification.go).

### Критерии оценивания

Всего можно получить 10 баллов за задание.
- полностью решенное задание оценивается в 8 баллов
- 2 балла начисляется за чистоту кода

### Запуск тестов

1. зайти в терминале в каталог homework
2. вызвать ```go test -v ./...```

### Линтер

Для линтинга используется [golangci-lint](https://golangci-lint.run/).
Инструкцию по установке можно найти [тут](https://golangci-lint.run/usage/install/).

Для запуска линтера нужно выполнить команду `golangci-lint run` в корне проекта.
Большую часть ошибок линтера можно поправить с использованием флага `--fix`.
