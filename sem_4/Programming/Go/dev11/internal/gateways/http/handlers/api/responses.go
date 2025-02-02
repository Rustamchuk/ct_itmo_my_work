package api

import (
	"encoding/json"
	"net/http"
)

type Response struct {
	Code int         `json:"code"`
	Body interface{} `json:"body"`
}

func WriteJSONResponse(w http.ResponseWriter, response Response) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(response.Code)
	json.NewEncoder(w).Encode(response.Body)
}
