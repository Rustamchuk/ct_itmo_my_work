package adapters

import openapi "homework/dto/go"

type EventAdapter interface {
	openapi.EventsAPIServicer
}

type SensorAdapter interface {
	openapi.SensorsAPIServicer
}

type UserAdapter interface {
	openapi.UsersAPIServicer
}
