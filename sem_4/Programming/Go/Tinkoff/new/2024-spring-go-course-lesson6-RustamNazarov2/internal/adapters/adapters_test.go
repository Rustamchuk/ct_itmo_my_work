package adapters

import (
	"errors"
	"testing"

	openapi "homework/dto/go"

	"github.com/stretchr/testify/assert"
)

var someError = errors.New("error")

type testArgs struct {
	name             string
	method           func() (openapi.ImplResponse, error)
	setup            func()
	expectedResponse openapi.ImplResponse
	expectedError    error
}

func runTests(t *testing.T, tests []testArgs) {
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.setup != nil {
				tt.setup()
			}
			response, err := tt.method()
			assert.Equal(t, tt.expectedResponse, response)
			if tt.expectedError != nil {
				assert.ErrorIs(t, err, tt.expectedError)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}
