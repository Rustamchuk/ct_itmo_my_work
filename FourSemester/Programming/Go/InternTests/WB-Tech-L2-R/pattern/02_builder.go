package pattern

import "fmt"

/*
	Реализовать паттерн «строитель».
Объяснить применимость паттерна, его плюсы и минусы, а также реальные примеры использования данного примера на практике.
	https://en.wikipedia.org/wiki/Builder_pattern
*/

/*
Применение:
	Строитель подходит для создания сложных объектов с множеством параметров, особенно при пошаговом создании.
	Это позволяет создавать разные варианты объекта, используя один и тот же интерфейс или даже весь код строительства.
Плюсы:
	Изоляция создания объекта от остальной логики проекта
	Простой конструктор объектов со множеством параметров
	Общий контроль над конструированием экземпляров объекта
Минусы:
	Усложнение кода за счет множества новых структур, строителей
	Перебор в простых сценариях, не подходит для небольших сервисов
Опыт применения:
	Использовал в проекте, где была работа с несколькими базами данных. В данной ситуации идеально подходит,
	упрощает работы с хранилищем. Ниже реализовал этот пример. (Две БД PostgreSQL и MongoDB)
*/

// QueryBuilder interface
type QueryBuilder interface {
	Select(fields ...string) QueryBuilder
	From(table string) QueryBuilder
	Where(condition string) QueryBuilder
	Build() string
}

// PostgreSQL Builder
type PostgresQueryBuilder struct {
	query string
}

func NewPostgresQueryBuilder() QueryBuilder {
	return &PostgresQueryBuilder{query: ""}
}

func (b *PostgresQueryBuilder) Select(fields ...string) QueryBuilder {
	b.query += "SELECT " + join(fields, ", ") + " "
	return b
}

func (b *PostgresQueryBuilder) From(table string) QueryBuilder {
	b.query += "FROM " + table + " "
	return b
}

func (b *PostgresQueryBuilder) Where(condition string) QueryBuilder {
	b.query += "WHERE " + condition + " "
	return b
}

func (b *PostgresQueryBuilder) Build() string {
	return b.query
}

// MongoDB Builder
type MongoDBQueryBuilder struct {
	query string
}

func NewMongoDBQueryBuilder() QueryBuilder {
	return &MongoDBQueryBuilder{query: ""}
}

func (b *MongoDBQueryBuilder) Select(fields ...string) QueryBuilder {
	b.query += "{find: '" + join(fields, ", ") + "', "
	return b
}

func (b *MongoDBQueryBuilder) From(table string) QueryBuilder {
	b.query += "collection: '" + table + "', "
	return b
}

func (b *MongoDBQueryBuilder) Where(condition string) QueryBuilder {
	b.query += "filter: {" + condition + "}}"
	return b
}

func (b *MongoDBQueryBuilder) Build() string {
	return b.query
}

// Helper function to join fields
func join(items []string, delimiter string) string {
	result := ""
	for i, item := range items {
		result += item
		if i < len(items)-1 {
			result += delimiter
		}
	}
	return result
}

func main() {
	postgresBuilder := NewPostgresQueryBuilder()
	postgresQuery := postgresBuilder.Select("name", "age").From("users").Where("age > 30").Build()
	fmt.Println("PostgreSQL Query:", postgresQuery)

	mongoBuilder := NewMongoDBQueryBuilder()
	mongoQuery := mongoBuilder.Select("name", "age").From("users").Where("age: {$gt: 30}").Build()
	fmt.Println("MongoDB Query:", mongoQuery)
}
