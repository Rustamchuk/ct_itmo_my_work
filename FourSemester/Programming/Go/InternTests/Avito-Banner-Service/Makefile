.PHONY: all exit create-network generate-api run-db migrate build run migrate-down stop-container stop-db remove-network

# Путь к схеме миграции
MIGRATION_PATH=./schema
# Строка подключения к базе данных
DATABASE_URL=postgres://postgres:admin@localhost:5432/postgres?sslmode=disable

# Общая команда для полного запуска проекта: генерация API, запуск БД, миграции, сборка и запуск приложения
all: generate-api create-network run-db migrate build run

# Выполнение операции отката миграций, остановка и удаление контейнеров приложения и базы данных и сети
exit: migrate-down stop-container stop-db remove-network

# Генерация кода из OpenAPI спецификации
generate-api:
	docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i /local/api.yaml -g go-server -o /local/pkg/generated/open_api_server && rm -f ${PWD}/pkg/generated/open_api_server/go.mod

# Создание сети Docker, если она еще не создана
create-network:
	docker network create my-network || true

# Запуск контейнера PostgreSQL
run-db:
	docker pull postgres
	docker run --name=banner-service-db -e POSTGRES_PASSWORD=admin -p 5432:5432 -d --rm --network=my-network postgres

# Применение миграций
migrate:
	@echo "Ожидание готовности базы данных..."
	@while ! docker exec banner-service-db pg_isready -h localhost -p 5432 > /dev/null 2>&1; do \
		sleep 1; \
		echo "Ожидание подключения к базе данных..."; \
	done
	@echo "База данных готова. Применение миграций..."
	migrate -path $(MIGRATION_PATH) -database '$(DATABASE_URL)' up

# Сборка Docker образа для приложения
build:
	docker build -t myapp .

# Запуск приложения
run:
	docker run --name=myapp -d -p 8080:8080 --network=my-network myapp

# Откат миграций
migrate-down:
	migrate -path $(MIGRATION_PATH) -database '$(DATABASE_URL)' down

# Остановка и удаление контейнера приложения
stop-container:
	docker stop myapp
	docker rm myapp

# Остановка и удаление контейнера базы данных (удаление автоматическое)
stop-db:
	docker stop banner-service-db

# Удаление сети Docker
remove-network:
	docker network rm my-network

