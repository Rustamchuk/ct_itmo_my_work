package reposiory

import "WB-Tech-L2/develop/dev11/internal/reposiory/inmemory"

// Набор всех хранилищ
type Repository struct {
	inmemory.Event
}

func NewRepository() Repository {
	return Repository{
		Event: inmemory.NewEvent(),
	}
}
