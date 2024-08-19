FROM golang:1.22 AS build

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . .

RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o myapp ./cmd/server

FROM alpine:latest
RUN apk --no-cache add ca-certificates

WORKDIR /root/

COPY --from=build /app/myapp .
COPY migrations /root/migrations

EXPOSE 8080
EXPOSE 2112

CMD ["./myapp"]