# BannerPostRequest

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**TagIds** | Pointer to **[]int32** | Идентификаторы тэгов | [optional] 
**FeatureId** | Pointer to **int32** | Идентификатор фичи | [optional] 
**Content** | Pointer to **map[string]interface{}** | Содержимое баннера | [optional] 
**IsActive** | Pointer to **bool** | Флаг активности баннера | [optional] 

## Methods

### NewBannerPostRequest

`func NewBannerPostRequest() *BannerPostRequest`

NewBannerPostRequest instantiates a new BannerPostRequest object
This constructor will assign default values to properties that have it defined,
and makes sure properties required by API are set, but the set of arguments
will change when the set of required properties is changed

### NewBannerPostRequestWithDefaults

`func NewBannerPostRequestWithDefaults() *BannerPostRequest`

NewBannerPostRequestWithDefaults instantiates a new BannerPostRequest object
This constructor will only assign default values to properties that have it defined,
but it doesn't guarantee that properties required by API are set

### GetTagIds

`func (o *BannerPostRequest) GetTagIds() []int32`

GetTagIds returns the TagIds field if non-nil, zero value otherwise.

### GetTagIdsOk

`func (o *BannerPostRequest) GetTagIdsOk() (*[]int32, bool)`

GetTagIdsOk returns a tuple with the TagIds field if it's non-nil, zero value otherwise
and a boolean to check if the value has been set.

### SetTagIds

`func (o *BannerPostRequest) SetTagIds(v []int32)`

SetTagIds sets TagIds field to given value.

### HasTagIds

`func (o *BannerPostRequest) HasTagIds() bool`

HasTagIds returns a boolean if a field has been set.

### GetFeatureId

`func (o *BannerPostRequest) GetFeatureId() int32`

GetFeatureId returns the FeatureId field if non-nil, zero value otherwise.

### GetFeatureIdOk

`func (o *BannerPostRequest) GetFeatureIdOk() (*int32, bool)`

GetFeatureIdOk returns a tuple with the FeatureId field if it's non-nil, zero value otherwise
and a boolean to check if the value has been set.

### SetFeatureId

`func (o *BannerPostRequest) SetFeatureId(v int32)`

SetFeatureId sets FeatureId field to given value.

### HasFeatureId

`func (o *BannerPostRequest) HasFeatureId() bool`

HasFeatureId returns a boolean if a field has been set.

### GetContent

`func (o *BannerPostRequest) GetContent() map[string]interface{}`

GetContent returns the Content field if non-nil, zero value otherwise.

### GetContentOk

`func (o *BannerPostRequest) GetContentOk() (*map[string]interface{}, bool)`

GetContentOk returns a tuple with the Content field if it's non-nil, zero value otherwise
and a boolean to check if the value has been set.

### SetContent

`func (o *BannerPostRequest) SetContent(v map[string]interface{})`

SetContent sets Content field to given value.

### HasContent

`func (o *BannerPostRequest) HasContent() bool`

HasContent returns a boolean if a field has been set.

### GetIsActive

`func (o *BannerPostRequest) GetIsActive() bool`

GetIsActive returns the IsActive field if non-nil, zero value otherwise.

### GetIsActiveOk

`func (o *BannerPostRequest) GetIsActiveOk() (*bool, bool)`

GetIsActiveOk returns a tuple with the IsActive field if it's non-nil, zero value otherwise
and a boolean to check if the value has been set.

### SetIsActive

`func (o *BannerPostRequest) SetIsActive(v bool)`

SetIsActive sets IsActive field to given value.

### HasIsActive

`func (o *BannerPostRequest) HasIsActive() bool`

HasIsActive returns a boolean if a field has been set.


[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


