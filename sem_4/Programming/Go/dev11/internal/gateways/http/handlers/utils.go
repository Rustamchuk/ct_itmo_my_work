package handlers

import (
	"WB-Tech-L2/develop/dev11/internal/gateways/http/handlers/api"
	"encoding/json"
	"net/http"
	"strconv"
)

func decodeJSON(w http.ResponseWriter, r *http.Request, dst interface{}) error {
	if err := json.NewDecoder(r.Body).Decode(dst); err != nil {
		return err
	}
	return nil
}

func decodeQuery(r *http.Request, dst *api.GetEventsRequest) error {
	userID := r.URL.Query().Get("user_id")
	date := r.URL.Query().Get("date")

	inpDate := api.Date{}
	err := inpDate.UnmarshalJSON([]byte(`"` + date + `"`))
	if err != nil {
		return err
	}

	dst.UserID, err = strconv.Atoi(userID)
	if err != nil {
		return err
	}
	dst.Date = inpDate
	return nil
}

func validateMethod(w http.ResponseWriter, r *http.Request, method string) bool {
	if r.Method != method {
		return false
	}
	return true
}
