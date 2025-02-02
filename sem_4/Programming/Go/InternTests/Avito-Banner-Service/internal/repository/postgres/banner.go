package postgres

import (
	"context"
	"encoding/json"
	"fmt"
	openapi "github.com/Rustamchuk/Avito-Banner-Service/pkg/generated/open_api_server/go"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"net/http"
	"strings"
)

type BannerPostgres struct {
	db *sqlx.DB
}

func NewBannerPostgres(db *sqlx.DB) *BannerPostgres {
	return &BannerPostgres{db: db}
}

func (b *BannerPostgres) BannerGet(
	ctx context.Context,
	token string,
	featureId int32,
	tagId int32,
	limit int32,
	offset int32,
) (openapi.ImplResponse, error) {
	var banners []openapi.BannerGet200ResponseInner
	baseQuery := `SELECT banner_id, content, is_active, feature_id, tag_ids, created_at, updated_at FROM banners`

	whereClauses := []string{}
	args := []interface{}{}
	argCounter := 1

	if featureId != 0 {
		whereClauses = append(whereClauses, fmt.Sprintf("feature_id = $%d", argCounter))
		args = append(args, featureId)
		argCounter++
	}

	if tagId != 0 {
		whereClauses = append(whereClauses, fmt.Sprintf("$%d = ANY(tag_ids)", argCounter))
		args = append(args, tagId)
		argCounter++
	}

	if token != "admin" {
		whereClauses = append(whereClauses, fmt.Sprintf("is_active = true"))
	}

	if len(whereClauses) > 0 {
		baseQuery += " WHERE " + strings.Join(whereClauses, " AND ")
	}

	// Применение LIMIT и OFFSET только если limit > 0
	if limit > 0 {
		baseQuery += fmt.Sprintf(" LIMIT $%d", argCounter)
		args = append(args, limit)
		argCounter++

		if offset > 0 {
			baseQuery += fmt.Sprintf(" OFFSET $%d", argCounter)
			args = append(args, offset)
		}
	}

	rows, err := b.db.QueryContext(ctx, baseQuery, args...) // Используем args...
	if err != nil {
		return openapi.Response(500, nil), err
	}
	defer rows.Close()

	for rows.Next() {
		var banner openapi.BannerGet200ResponseInner
		var contentJSON []byte
		var tagIds pq.Int64Array

		err = rows.Scan(&banner.BannerId, &contentJSON, &banner.IsActive, &banner.FeatureId, &tagIds, &banner.CreatedAt, &banner.UpdatedAt)
		if err != nil {
			return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
		}

		if err = json.Unmarshal(contentJSON, &banner.Content); err != nil {
			return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
		}

		for _, id := range tagIds {
			banner.TagIds = append(banner.TagIds, int32(id))
		}

		banners = append(banners, banner)
	}

	if err = rows.Err(); err != nil {
		return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
	}

	if len(banners) == 0 {
		return openapi.ImplResponse{Code: http.StatusNoContent}, nil
	}

	return openapi.Response(http.StatusOK, banners), nil
}

func (b *BannerPostgres) BannerIdDelete(
	ctx context.Context,
	id int32,
	token string,
) (openapi.ImplResponse, error) {
	if token != "admin" {
		return openapi.ImplResponse{Code: http.StatusMethodNotAllowed}, nil
	}

	query := `DELETE FROM banners WHERE banner_id = $1`
	result, err := b.db.ExecContext(ctx, query, id)
	if err != nil {
		return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
	}
	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
	}
	if rowsAffected == 0 {
		return openapi.ImplResponse{Code: http.StatusNotFound}, nil
	}
	return openapi.ImplResponse{Code: http.StatusNoContent}, nil
}

func (b *BannerPostgres) BannerIdPatch(
	ctx context.Context,
	id int32,
	bannerIdPatchRequest openapi.BannerIdPatchRequest,
	token string,
) (openapi.ImplResponse, error) {
	if token != "admin" {
		return openapi.ImplResponse{Code: http.StatusMethodNotAllowed}, nil
	}

	query := `UPDATE banners SET `
	params := []interface{}{}
	paramId := 1

	if bannerIdPatchRequest.TagIds != nil {
		query += fmt.Sprintf("tag_ids = $%d, ", paramId)
		params = append(params, pq.Array(*bannerIdPatchRequest.TagIds))
		paramId++
	}
	if bannerIdPatchRequest.FeatureId != nil {
		query += fmt.Sprintf("feature_id = $%d, ", paramId)
		params = append(params, *bannerIdPatchRequest.FeatureId)
		paramId++
	}
	if bannerIdPatchRequest.Content != nil {
		contentJSON, err := json.Marshal(*bannerIdPatchRequest.Content)
		if err != nil {
			return openapi.Response(500, nil), err
		}
		query += fmt.Sprintf("content = $%d, ", paramId)
		params = append(params, string(contentJSON))
		paramId++
	}
	if bannerIdPatchRequest.IsActive != nil {
		query += fmt.Sprintf("is_active = $%d, ", paramId)
		params = append(params, *bannerIdPatchRequest.IsActive)
		paramId++
	}

	query = strings.TrimSuffix(query, ", ") + fmt.Sprintf(" WHERE banner_id = $%d", paramId)
	params = append(params, id)

	result, err := b.db.ExecContext(ctx, query, params...)
	if err != nil {
		return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
	}
	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return openapi.ImplResponse{Code: http.StatusInternalServerError}, err
	}
	if rowsAffected == 0 {
		return openapi.ImplResponse{Code: http.StatusNotFound}, nil
	}
	return openapi.Response(200, nil), nil
}

func (b *BannerPostgres) BannerPost(
	ctx context.Context,
	bannerPostRequest openapi.BannerPostRequest,
	token string,
) (openapi.ImplResponse, error) {
	if token != "admin" {
		return openapi.ImplResponse{Code: http.StatusMethodNotAllowed}, nil
	}

	jsonContent, err := json.Marshal(bannerPostRequest.Content)
	if err != nil {
		return openapi.Response(500, nil), err
	}
	query := `INSERT INTO banners (tag_ids, feature_id, content, is_active) VALUES ($1, $2, $3, $4) RETURNING banner_id`
	var newBannerId int32
	err = b.db.QueryRowContext(ctx, query, pq.Array(bannerPostRequest.TagIds), bannerPostRequest.FeatureId, jsonContent, bannerPostRequest.IsActive).Scan(&newBannerId)
	if err != nil {
		return openapi.Response(500, nil), err
	}
	return openapi.Response(http.StatusCreated, map[string]int32{"banner_id": newBannerId}), nil
}

func (b *BannerPostgres) UserBannerGet(
	ctx context.Context,
	tagId int32,
	featureId int32,
	token string,
) (openapi.ImplResponse, error) {
	banner, err := b.BannerGet(ctx, token, tagId, featureId, 1, 0)
	return banner, err
}
