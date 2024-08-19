package main

import (
	"WB-Tech-L0/internal/gateways/http/api"
	"encoding/json"
	"log"
	"strconv"
	"time"

	"github.com/nats-io/stan.go"
)

const (
	natsURL = "nats-streaming:4222"
	channel = "PostOrderChannel"
)

func main() {
	time.Sleep(5 * time.Second)

	sc, err := stan.Connect("WB-L0", "publisher", stan.NatsURL(natsURL))
	if err != nil {
		log.Fatalf("failed NATS Streaming connecting: %v", err)
	}
	defer sc.Close()

	for i := 1; i < 10; i++ {
		order := GenerateOrder(i)

		data, err := json.Marshal(order)
		if err != nil {
			log.Fatalf("failed marshaling JSON: %v", err)
		}

		if err = sc.Publish(channel, data); err != nil {
			log.Fatalf("failed sending: %v", err)
		}

		log.Println("send successfully")
	}
}

func GenerateOrder(n int) api.Order {
	order := api.Order{
		OrderUID:    "b563feb7b2b84b6test" + strconv.Itoa(n),
		TrackNumber: "WBILMTESTTRACK" + strconv.Itoa(n),
		Entry:       "WBIL" + strconv.Itoa(n),
		Delivery: api.Delivery{
			Name:    "Test Testov" + strconv.Itoa(n),
			Phone:   "+9720000000" + strconv.Itoa(n),
			Zip:     "2639809" + strconv.Itoa(n),
			City:    "Kiryat Mozkin" + strconv.Itoa(n),
			Address: "Ploshad Mira 15" + strconv.Itoa(n),
			Region:  "Kraiot" + strconv.Itoa(n),
			Email:   "test@gmail.com" + strconv.Itoa(n),
		},
		Payment: api.Payment{
			Transaction:  "b563feb7b2b84b6test" + strconv.Itoa(n),
			RequestID:    "" + strconv.Itoa(n),
			Currency:     "USD" + strconv.Itoa(n),
			Provider:     "wbpay" + strconv.Itoa(n),
			Amount:       20 * n,
			PaymentDT:    12 * n,
			Bank:         "alpha" + strconv.Itoa(n),
			DeliveryCost: 1500 * n,
			GoodsTotal:   317 * n,
			CustomFee:    0 + n,
		},
		Items: []api.Item{
			{
				ChrtID:      n,
				TrackNumber: "WBILMTESTTRACK" + strconv.Itoa(n),
				Price:       453 * n,
				RID:         "ab4219087a764ae0btest" + strconv.Itoa(n),
				Name:        "Mascaras" + strconv.Itoa(n),
				Sale:        30 * n,
				Size:        "0",
				TotalPrice:  317 * n,
				NmID:        2389212 + n,
				Brand:       "Vivienne Sabo" + strconv.Itoa(n),
				Status:      202,
			},
			{
				ChrtID:      100 + n,
				TrackNumber: "WBILMTESTTRACK" + strconv.Itoa(n),
				Price:       453 * n,
				RID:         "ab4219087a764ae0btest" + strconv.Itoa(n),
				Name:        "Mascaras" + strconv.Itoa(n),
				Sale:        30 * n,
				Size:        strconv.Itoa(n),
				TotalPrice:  317 * n,
				NmID:        2389212 + n,
				Brand:       "Vivienne Sabo" + strconv.Itoa(n),
				Status:      202,
			},
		},
		Locale:            "en",
		InternalSignature: "",
		CustomerID:        "test" + strconv.Itoa(n),
		DeliveryService:   "meest" + strconv.Itoa(n),
		ShardKey:          "9",
		SmID:              99 + n,
		DateCreated:       time.Now(),
		OofShard:          strconv.Itoa(n),
	}

	return order
}
