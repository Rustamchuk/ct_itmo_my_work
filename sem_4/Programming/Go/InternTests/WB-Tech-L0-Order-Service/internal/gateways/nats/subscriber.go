package nats

import (
	"WB-Tech-L0/internal/adapters"
	"WB-Tech-L0/internal/gateways/http/api"
	"WB-Tech-L0/internal/service"
	"context"
	"encoding/json"
	"fmt"
	"github.com/nats-io/stan.go"
	"log"
	"sync"
	"time"
)

type SubscriberConfig struct {
	DurableName string
	SubjectPost string
}

type Subscriber interface {
	Subscribe(wg *sync.WaitGroup, ctx context.Context) error
	UnSubscribe() error
}

type subscriber struct {
	sub     stan.Subscription
	sc      stan.Conn
	service service.Service
	config  SubscriberConfig
}

func NewSubscriber(sc stan.Conn, service service.Service, cfg SubscriberConfig) Subscriber {
	return &subscriber{
		sc:      sc,
		service: service,
		config:  cfg,
	}
}

func (s *subscriber) Subscribe(wg *sync.WaitGroup, ctx context.Context) error {
	var err error
	s.sub, err = s.sc.Subscribe(s.config.SubjectPost, func(msg *stan.Msg) {
		wg.Add(1)
		defer wg.Done()

		select {
		case <-ctx.Done():
			return
		default:
			log.Printf("message received")
			if err := s.messageHandler(msg.Data, ctx); err != nil {
				log.Printf("failed to handle message: %v", err)
			}
			if err := msg.Ack(); err != nil {
				log.Printf("failed to acknowledge message: %v\n", err)
			}
		}
	},
		stan.AckWait(time.Second*time.Duration(60)),
		stan.DurableName(s.config.DurableName),
		stan.SetManualAckMode(),
		stan.MaxInflight(15))
	if err != nil {
		return fmt.Errorf("failed to subscribe: %w", err)
	}

	log.Printf("subscribe succesful")

	return nil
}

func (s *subscriber) UnSubscribe() error {
	if s.sub != nil {
		if err := s.sub.Unsubscribe(); err != nil {
			return fmt.Errorf("failed to unsubscribe: %w", err)
		}
	}

	return nil
}

func (s subscriber) messageHandler(data []byte, ctx context.Context) error {
	var receivedOrder api.Order
	if err := json.Unmarshal(data, &receivedOrder); err != nil {
		return fmt.Errorf("failed to unmarshal JSON: %w", err)
	}

	err := s.service.AddOrder(ctx, adapters.OrderRequestModel(receivedOrder))
	if err != nil {
		return fmt.Errorf("failed addOrder in data base: %w", err)
	}

	return nil
}
