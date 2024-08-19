package homework

import (
	"errors"
	"slices"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestValidate2(t *testing.T) {
	var targetErrs interface{ Unwrap() []error }
	var targetErr interface{ Unwrap() error }
	nestedTestData := []struct {
		Str1 struct {
			Str1 struct {
				Len string `validate:"len:5"`
				Min string `validate:"min:4"`
				Max int    `validate:"max:24"`
			}
			Len string `validate:"len:5"`
			Min string `validate:"min:4"`
			Max int    `validate:"max:24"`
		}
	}{
		{
			Str1: struct {
				Str1 struct {
					Len string `validate:"len:5"`
					Min string `validate:"min:4"`
					Max int    `validate:"max:24"`
				}
				Len string `validate:"len:5"`
				Min string `validate:"min:4"`
				Max int    `validate:"max:24"`
			}{
				Str1: struct {
					Len string `validate:"len:5"`
					Min string `validate:"min:4"`
					Max int    `validate:"max:24"`
				}{
					Len: "12345",
					Min: "12345",
					Max: 10,
				},
				Len: "12345",
				Min: "12345",
				Max: 10,
			},
		},
		{
			Str1: struct {
				Str1 struct {
					Len string `validate:"len:5"`
					Min string `validate:"min:4"`
					Max int    `validate:"max:24"`
				}
				Len string `validate:"len:5"`
				Min string `validate:"min:4"`
				Max int    `validate:"max:24"`
			}{
				Str1: struct {
					Len string `validate:"len:5"`
					Min string `validate:"min:4"`
					Max int    `validate:"max:24"`
				}{
					Len: "12345",
					Min: "12345",
					Max: 100,
				},
				Len: "12345",
				Min: "12345",
				Max: 10,
			},
		},
	}

	type args struct {
		v any
	}
	tests := []struct {
		name     string
		args     args
		wantErr  bool
		checkErr func(err error) bool
	}{
		{
			name: "string slice",
			args: args{
				v: struct {
					Len  []string `validate:"len:24"`
					Len2 []string `validate:"len:5"`
					Len3 []string `validate:"len:-1"`
					Min  []string `validate:"min:%12"`
					Min2 []string `validate:"min:-6"`
					Min3 []string `validate:"min:10"`
					Max  []string `validate:"max:-6"`
					Max2 []string `validate:"max:4"`
					Max3 []string `validate:"max:20"`
					In   []string `validate:"in:abc,def,ghi"`
					In2  []string `validate:"in:"`
					In3  []string `validate:"in:a,b,c"`
				}{
					Len:  []string{"aaaaaaaaaaa", "b", ""},
					Len2: []string{"a", "bbbbb", "c"},
					Len3: []string{"a", "b", "ccc"},
					Min:  []string{"aaaaaaaaaaa", "b", ""},
					Min2: []string{"a", "bbbbb", "c"},
					Min3: []string{"a", "b", "ccc"},
					Max:  []string{"aaaaaaaaaaa", "b", ""},
					Max2: []string{"a", "bbbbb", "c"},
					Max3: []string{"a", "b", "ccc"},
					In:   []string{"aaaaaaaaaaa", "def", ""},
					In2:  []string{"a", "bbbbb", "c"},
					In3:  []string{"a", "b", "c"},
				},
			},
			wantErr: true,
			checkErr: func(err error) bool {
				expectedErrors := []struct {
					err   error
					field string
				}{
					{
						err:   ErrLenValidationFailed,
						field: "Len",
					},
					{
						err:   ErrLenValidationFailed,
						field: "Len2",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "Len3",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "Min",
					},
					{
						err:   ErrMinValidationFailed,
						field: "Min3",
					},
					{
						err:   ErrMaxValidationFailed,
						field: "Max",
					},
					{
						err:   ErrMaxValidationFailed,
						field: "Max2",
					},
					{
						err:   ErrInValidationFailed,
						field: "In",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "In2",
					},
				}
				var errs []error
				if errors.As(err, &targetErrs) {
					errs = targetErrs.Unwrap()
				} else {
					assert.Fail(t, "err should be created with errors.Join(err...) function")
					return false
				}
				assert.Len(t, errs, 9)

				foundErrors := expectedErrors
				for i := range errs {
					actualErr := &ValidationError{}
					if errors.As(errs[i], &actualErr) {
						for ei := range expectedErrors {
							if errors.Is(actualErr, expectedErrors[ei].err) && actualErr.field == expectedErrors[ei].field {
								foundErrors = slices.Delete(foundErrors, ei, ei+1)
							}
						}
					}
				}

				assert.Empty(t, foundErrors, "unexpected errors found")
				return true
			},
		},
		{
			name: "int slice",
			args: args{
				v: struct {
					Len  []int `validate:"len:24"`
					Len2 []int `validate:"len:5"`
					Len3 []int `validate:"len:-1"`
					Min  []int `validate:"min:%12"`
					Min2 []int `validate:"min:-6"`
					Min3 []int `validate:"min:10"`
					Max  []int `validate:"max:-6"`
					Max2 []int `validate:"max:4"`
					Max3 []int `validate:"max:20"`
					In   []int `validate:"in:10,20,30"`
					In2  []int `validate:"in:"`
					In3  []int `validate:"in:1,2,3"`
				}{
					Len:  []int{10, 5, 2},
					Len2: []int{},
					Len3: []int{2},
					Min:  []int{1, 2},
					Min2: []int{10, 20, 30},
					Min3: []int{10, 2, 20},
					Max:  []int{1, 2, 3},
					Max2: []int{1, 2, 3},
					Max3: []int{1, 2, 20},
					In:   []int{},
					In2:  []int{1, 2, 3},
					In3:  []int{1, 2, 3},
				},
			},
			wantErr: true,
			checkErr: func(err error) bool {
				expectedErrors := []struct {
					err   error
					field string
				}{
					{
						err:   ErrLenValidationFailed,
						field: "Len",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "Len3",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "Min",
					},
					{
						err:   ErrMinValidationFailed,
						field: "Min3",
					},
					{
						err:   ErrMaxValidationFailed,
						field: "Max",
					},
					{
						err:   ErrInvalidValidatorSyntax,
						field: "In2",
					},
				}
				var errs []error
				if errors.As(err, &targetErrs) {
					errs = targetErrs.Unwrap()
				} else {
					assert.Fail(t, "err should be created with errors.Join(err...) function")
					return false
				}
				assert.Len(t, errs, 6)

				foundErrors := expectedErrors
				for i := range errs {
					actualErr := &ValidationError{}
					if errors.As(errs[i], &actualErr) {
						for ei := range expectedErrors {
							if errors.Is(actualErr, expectedErrors[ei].err) && actualErr.field == expectedErrors[ei].field {
								foundErrors = slices.Delete(foundErrors, ei, ei+1)
							}
						}
					}
				}

				assert.Empty(t, foundErrors, "unexpected errors found")
				return true
			},
		},
		{
			name: "nested",
			args: args{
				v: struct {
					Str1 struct {
						Str1 struct {
							Len string `validate:"len:5"`
							Min string `validate:"min:4"`
							Max int    `validate:"max:24"`
						}
						Len string `validate:"len:5"`
						Min string `validate:"min:4"`
						Max int    `validate:"max:24"`
					}
					Str2 []struct {
						Str1 struct {
							Str1 struct {
								Len string `validate:"len:5"`
								Min string `validate:"min:4"`
								Max int    `validate:"max:24"`
							}
							Len string `validate:"len:5"`
							Min string `validate:"min:4"`
							Max int    `validate:"max:24"`
						}
					}
				}{
					Str1: struct {
						Str1 struct {
							Len string `validate:"len:5"`
							Min string `validate:"min:4"`
							Max int    `validate:"max:24"`
						}
						Len string `validate:"len:5"`
						Min string `validate:"min:4"`
						Max int    `validate:"max:24"`
					}{
						Str1: struct {
							Len string `validate:"len:5"`
							Min string `validate:"min:4"`
							Max int    `validate:"max:24"`
						}{
							Len: "afaaa",
							Min: "afaa",
							Max: 10,
						},
						Len: "afaaa",
						Min: "afaa",
						Max: 25,
					},
					Str2: nestedTestData,
				},
			},
			wantErr: true,
			checkErr: func(err error) bool {
				expectedErrors := []struct {
					err   error
					field string
				}{
					{
						err:   ErrMaxValidationFailed,
						field: "Str1",
					},
					{
						err:   ErrMaxValidationFailed,
						field: "Str2",
					},
				}
				var errs []error
				if errors.As(err, &targetErrs) {
					errs = targetErrs.Unwrap()
				} else {
					assert.Fail(t, "err should be created with errors.Join(err...) function")
					return false
				}
				assert.Len(t, errs, 2)

				foundErrors := expectedErrors
				for i := range errs {
					for {
						if errors.As(errs[i], &targetErrs) {
							errs[i] = targetErrs.Unwrap()[0]
						} else if errors.As(errs[i], &targetErr) {
							errs[i] = targetErr.Unwrap()
						} else {
							break
						}
					}
					errs[i] = &ValidationError{expectedErrors[0].field, errs[i]}
					actualErr := &ValidationError{}
					if errors.As(errs[i], &actualErr) {
						for ei := range expectedErrors {
							if errors.Is(actualErr, expectedErrors[ei].err) && actualErr.field == expectedErrors[ei].field {
								foundErrors = slices.Delete(foundErrors, ei, ei+1)
							}
						}
					}
				}

				assert.Empty(t, foundErrors, "unexpected errors found")
				return true
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := Validate(tt.args.v)
			if tt.wantErr {
				assert.Error(t, err)
				assert.True(t, tt.checkErr(err), "test expect an error, but got wrong error type")
			} else {
				assert.NoError(t, err)
			}
		})
	}
}
