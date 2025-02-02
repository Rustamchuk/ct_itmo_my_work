package homework

import (
	"errors"
	"fmt"
	"reflect"
	"strconv"
	"strings"
)

var (
	validateTag = "validate"
	lenValid    = "len"
	inValid     = "in"
	minValid    = "min"
	maxValid    = "max"
)

var (
	ErrNotStruct                   = errors.New("wrong argument given, should be a struct")
	ErrInvalidValidatorSyntax      = errors.New("invalid validator syntax")
	ErrValidateForUnexportedFields = errors.New("validation for unexported field is not allowed")
	ErrLenValidationFailed         = errors.New("len validation failed")
	ErrInValidationFailed          = errors.New("in validation failed")
	ErrMaxValidationFailed         = errors.New("max validation failed")
	ErrMinValidationFailed         = errors.New("min validation failed")
)

type ValidationType interface {
	int | string
}

type ValidationError struct {
	field string
	err   error
}

func NewValidationError(err error, field string) error {
	return &ValidationError{
		field: field,
		err:   err,
	}
}

func (e *ValidationError) Error() string {
	return fmt.Sprintf("%s: %s", e.field, e.err)
}

func (e *ValidationError) Unwrap() error {
	return e.err
}

func Validate(v any) error {
	typeV := reflect.TypeOf(v)
	if typeV.Kind() != reflect.Struct {
		return ErrNotStruct
	}

	valueV := reflect.ValueOf(v)
	errorArr := make([]error, 0)

	for i := 0; i < valueV.NumField(); i++ {
		fieldType := typeV.Field(i)
		fieldValue := valueV.Field(i)

		var curError *ValidationError

		if fieldValue.Kind() == reflect.Struct {
			if err := Validate(fieldValue.Interface()); err != nil {
				validationError := &ValidationError{
					field: fieldValue.Type().Name(),
					err:   err,
				}
				errorArr = append(errorArr, validationError)
			}
			continue
		}
		if fieldValue.Kind() == reflect.Slice && fieldType.Type.Elem().Kind() == reflect.Struct {
			for j := 0; j < fieldValue.Len(); j++ {
				if err := Validate(fieldValue.Index(j).Interface()); err != nil {
					validationError := &ValidationError{
						field: fieldValue.Type().Name(),
						err:   err,
					}
					errorArr = append(errorArr, validationError)
					continue
				}
			}
			continue
		}

		if _, ok := fieldType.Tag.Lookup(validateTag); !ok {
			continue
		}
		if !fieldType.IsExported() {
			errorArr = append(errorArr, ErrValidateForUnexportedFields)
			continue
		}

		switch valueField := fieldValue.Interface().(type) {
		case int:
			curError = validateTypes[int](i, typeV, valueField)
		case string:
			curError = validateTypes[string](i, typeV, valueField)
		case []int:
			curError = validateTypes[int](i, typeV, valueField...)
		case []string:
			curError = validateTypes[string](i, typeV, valueField...)
		}
		if curError == nil {
			continue
		}
		errorArr = append(errorArr, curError)
	}

	if len(errorArr) != 0 {
		return errors.Join(errorArr...)
	}
	return nil
}

func validateTypes[T ValidationType](ind int, typeV reflect.Type, objects ...T) *ValidationError {
	validation, err := getFieldTag[T](ind, typeV)
	if err != nil {
		return &ValidationError{
			field: typeV.Field(ind).Name,
			err:   ErrInvalidValidatorSyntax,
		}
	}

	for _, obj := range objects {
		err = validation(obj)
		if err != nil {
			return &ValidationError{
				field: typeV.Field(ind).Name,
				err:   err,
			}
		}
	}
	return nil
}

func getFieldTag[T ValidationType](ind int, typeV reflect.Type) (func(num T) error, error) {
	tag := typeV.Field(ind).Tag.Get(validateTag)
	tagSplit := strings.Split(tag, ":")
	validName, validArgs := tagSplit[0], tagSplit[1]
	if len(validArgs) == 0 {
		return nil, ErrInvalidValidatorSyntax
	}

	switch validName {
	case lenValid:
		numV, err := strconv.Atoi(validArgs)
		if numV < 0 {
			err = ErrInvalidValidatorSyntax
		}
		return lenValidator[T](numV), err
	case minValid:
		numV, err := strconv.Atoi(validArgs)
		return minValidator[T](numV), err
	case maxValid:
		numV, err := strconv.Atoi(validArgs)
		return maxValidator[T](numV), err
	case inValid:
		args := strings.Split(validArgs, ",")
		return inValidator[T](args...), nil
	}
	return nil, ErrInvalidValidatorSyntax
}

func minValidator[T ValidationType](minAllow int) func(obj T) error {
	return func(obj T) error {
		var num int

		switch value := reflect.ValueOf(obj).Interface().(type) {
		case string:
			num = len(value)
		case int:
			num = value
		}

		if num < minAllow {
			return ErrMinValidationFailed
		}
		return nil
	}
}

func maxValidator[T ValidationType](maxAllow int) func(obj T) error {
	return func(obj T) error {
		var num int

		switch value := reflect.ValueOf(obj).Interface().(type) {
		case string:
			if maxAllow < 0 {
				return ErrMaxValidationFailed
			}
			num = len(value)
		case int:
			num = value
		}

		if num > maxAllow {
			return ErrMaxValidationFailed
		}
		return nil
	}
}

func lenValidator[T ValidationType](lenAllow int) func(obj T) error {
	return func(obj T) error {
		switch value := reflect.ValueOf(obj).Interface().(type) {
		case string:
			if len(value) != lenAllow {
				return ErrLenValidationFailed
			}
		case int:
			return ErrLenValidationFailed
		}
		return nil
	}
}

func inValidator[T ValidationType](lib ...string) func(obj T) error {
	return func(obj T) error {
		var str string

		switch value := reflect.ValueOf(obj).Interface().(type) {
		case string:
			str = value
		case int:
			for _, s := range lib {
				_, err := strconv.Atoi(s)
				if err != nil {
					return ErrInValidationFailed
				}
			}
			str = strconv.Itoa(value)
		}

		for _, s := range lib {
			if s == str {
				return nil
			}
		}
		return ErrInValidationFailed
	}
}
