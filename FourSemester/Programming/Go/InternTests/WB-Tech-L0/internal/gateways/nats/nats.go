package nats

import (
	"github.com/nats-io/stan.go"
)

type Config struct {
	Host      string
	Port      string
	ClusterID string
	ClientID  string
}

func NewBroker(cfg Config) (stan.Conn, error) {
	conn, err := stan.Connect(
		cfg.ClusterID,
		cfg.ClientID,
		stan.NatsURL(cfg.Host+":"+cfg.Port))
	if err != nil {
		return nil, err
	}
	return conn, nil
}
