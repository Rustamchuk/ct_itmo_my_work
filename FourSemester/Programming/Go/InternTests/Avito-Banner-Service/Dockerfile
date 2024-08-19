# Используйте официальный образ Go как базовый
FROM golang:1.20 AS build

# Установите рабочий каталог в контейнере
WORKDIR /app

# Копируйте модули Go и их зависимости
COPY go.mod ./
COPY go.sum ./
RUN go mod download

# Копируйте исходный код проекта
COPY . .

# Соберите ваше приложение
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o myapp ./cmd/server

# Используйте образ alpine для финального образа
FROM alpine:latest  
RUN apk --no-cache add ca-certificates

WORKDIR /root/

# Копируйте скомпилированный бинарный файл из предыдущего шага
COPY --from=build /app/myapp .

# Откройте порт, который использует ваше приложение
EXPOSE 8080

# Запустите приложение
CMD ["./myapp"]