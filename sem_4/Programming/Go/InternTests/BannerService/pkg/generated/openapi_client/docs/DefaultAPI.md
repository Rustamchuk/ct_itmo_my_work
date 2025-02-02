# \DefaultAPI

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**BannerGet**](DefaultAPI.md#BannerGet) | **Get** /banner | Получение всех баннеров c фильтрацией по фиче и/или тегу
[**BannerIdDelete**](DefaultAPI.md#BannerIdDelete) | **Delete** /banner/{id} | Удаление баннера по идентификатору
[**BannerIdPatch**](DefaultAPI.md#BannerIdPatch) | **Patch** /banner/{id} | Обновление содержимого баннера
[**BannerPost**](DefaultAPI.md#BannerPost) | **Post** /banner | Создание нового баннера
[**UserBannerGet**](DefaultAPI.md#UserBannerGet) | **Get** /user_banner | Получение баннера для пользователя



## BannerGet

> []BannerGet200ResponseInner BannerGet(ctx).Token(token).FeatureId(featureId).TagId(tagId).Limit(limit).Offset(offset).Execute()

Получение всех баннеров c фильтрацией по фиче и/или тегу

### Example

```go
package main

import (
	"context"
	"fmt"
	"os"
	openapiclient "github.com/GIT_USER_ID/GIT_REPO_ID"
)

func main() {
	token := "admin_token" // string | Токен админа (optional)
	featureId := int32(56) // int32 |  (optional)
	tagId := int32(56) // int32 |  (optional)
	limit := int32(56) // int32 |  (optional)
	offset := int32(56) // int32 |  (optional)

	configuration := openapiclient.NewConfiguration()
	apiClient := openapiclient.NewAPIClient(configuration)
	resp, r, err := apiClient.DefaultAPI.BannerGet(context.Background()).Token(token).FeatureId(featureId).TagId(tagId).Limit(limit).Offset(offset).Execute()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error when calling `DefaultAPI.BannerGet``: %v\n", err)
		fmt.Fprintf(os.Stderr, "Full HTTP response: %v\n", r)
	}
	// response from `BannerGet`: []BannerGet200ResponseInner
	fmt.Fprintf(os.Stdout, "Response from `DefaultAPI.BannerGet`: %v\n", resp)
}
```

### Path Parameters



### Other Parameters

Other parameters are passed through a pointer to a apiBannerGetRequest struct via the builder pattern


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **token** | **string** | Токен админа | 
 **featureId** | **int32** |  | 
 **tagId** | **int32** |  | 
 **limit** | **int32** |  | 
 **offset** | **int32** |  | 

### Return type

[**[]BannerGet200ResponseInner**](BannerGet200ResponseInner.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## BannerIdDelete

> BannerIdDelete(ctx, id).Token(token).Execute()

Удаление баннера по идентификатору

### Example

```go
package main

import (
	"context"
	"fmt"
	"os"
	openapiclient "github.com/GIT_USER_ID/GIT_REPO_ID"
)

func main() {
	id := int32(56) // int32 | 
	token := "admin_token" // string | Токен админа (optional)

	configuration := openapiclient.NewConfiguration()
	apiClient := openapiclient.NewAPIClient(configuration)
	r, err := apiClient.DefaultAPI.BannerIdDelete(context.Background(), id).Token(token).Execute()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error when calling `DefaultAPI.BannerIdDelete``: %v\n", err)
		fmt.Fprintf(os.Stderr, "Full HTTP response: %v\n", r)
	}
}
```

### Path Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**id** | **int32** |  | 

### Other Parameters

Other parameters are passed through a pointer to a apiBannerIdDeleteRequest struct via the builder pattern


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------

 **token** | **string** | Токен админа | 

### Return type

 (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## BannerIdPatch

> BannerIdPatch(ctx, id).BannerIdPatchRequest(bannerIdPatchRequest).Token(token).Execute()

Обновление содержимого баннера

### Example

```go
package main

import (
	"context"
	"fmt"
	"os"
	openapiclient "github.com/GIT_USER_ID/GIT_REPO_ID"
)

func main() {
	id := int32(56) // int32 | 
	bannerIdPatchRequest := *openapiclient.NewBannerIdPatchRequest() // BannerIdPatchRequest | 
	token := "admin_token" // string | Токен админа (optional)

	configuration := openapiclient.NewConfiguration()
	apiClient := openapiclient.NewAPIClient(configuration)
	r, err := apiClient.DefaultAPI.BannerIdPatch(context.Background(), id).BannerIdPatchRequest(bannerIdPatchRequest).Token(token).Execute()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error when calling `DefaultAPI.BannerIdPatch``: %v\n", err)
		fmt.Fprintf(os.Stderr, "Full HTTP response: %v\n", r)
	}
}
```

### Path Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**id** | **int32** |  | 

### Other Parameters

Other parameters are passed through a pointer to a apiBannerIdPatchRequest struct via the builder pattern


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------

 **bannerIdPatchRequest** | [**BannerIdPatchRequest**](BannerIdPatchRequest.md) |  | 
 **token** | **string** | Токен админа | 

### Return type

 (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## BannerPost

> BannerPost201Response BannerPost(ctx).BannerPostRequest(bannerPostRequest).Token(token).Execute()

Создание нового баннера

### Example

```go
package main

import (
	"context"
	"fmt"
	"os"
	openapiclient "github.com/GIT_USER_ID/GIT_REPO_ID"
)

func main() {
	bannerPostRequest := *openapiclient.NewBannerPostRequest() // BannerPostRequest | 
	token := "admin_token" // string | Токен админа (optional)

	configuration := openapiclient.NewConfiguration()
	apiClient := openapiclient.NewAPIClient(configuration)
	resp, r, err := apiClient.DefaultAPI.BannerPost(context.Background()).BannerPostRequest(bannerPostRequest).Token(token).Execute()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error when calling `DefaultAPI.BannerPost``: %v\n", err)
		fmt.Fprintf(os.Stderr, "Full HTTP response: %v\n", r)
	}
	// response from `BannerPost`: BannerPost201Response
	fmt.Fprintf(os.Stdout, "Response from `DefaultAPI.BannerPost`: %v\n", resp)
}
```

### Path Parameters



### Other Parameters

Other parameters are passed through a pointer to a apiBannerPostRequest struct via the builder pattern


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **bannerPostRequest** | [**BannerPostRequest**](BannerPostRequest.md) |  | 
 **token** | **string** | Токен админа | 

### Return type

[**BannerPost201Response**](BannerPost201Response.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## UserBannerGet

> map[string]interface{} UserBannerGet(ctx).TagId(tagId).FeatureId(featureId).UseLastRevision(useLastRevision).Token(token).Execute()

Получение баннера для пользователя

### Example

```go
package main

import (
	"context"
	"fmt"
	"os"
	openapiclient "github.com/GIT_USER_ID/GIT_REPO_ID"
)

func main() {
	tagId := int32(56) // int32 | 
	featureId := int32(56) // int32 | 
	useLastRevision := true // bool |  (optional) (default to false)
	token := "user_token" // string | Токен пользователя (optional)

	configuration := openapiclient.NewConfiguration()
	apiClient := openapiclient.NewAPIClient(configuration)
	resp, r, err := apiClient.DefaultAPI.UserBannerGet(context.Background()).TagId(tagId).FeatureId(featureId).UseLastRevision(useLastRevision).Token(token).Execute()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error when calling `DefaultAPI.UserBannerGet``: %v\n", err)
		fmt.Fprintf(os.Stderr, "Full HTTP response: %v\n", r)
	}
	// response from `UserBannerGet`: map[string]interface{}
	fmt.Fprintf(os.Stdout, "Response from `DefaultAPI.UserBannerGet`: %v\n", resp)
}
```

### Path Parameters



### Other Parameters

Other parameters are passed through a pointer to a apiUserBannerGetRequest struct via the builder pattern


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tagId** | **int32** |  | 
 **featureId** | **int32** |  | 
 **useLastRevision** | **bool** |  | [default to false]
 **token** | **string** | Токен пользователя | 

### Return type

**map[string]interface{}**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)

