package rangeI

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestRange(t *testing.T) {
	myRange := NewRangeInt(-1, -1)
	get := myRange.Length()
	want := 1
	assert.Equal(t, want, get)
	myRange = NewRangeInt(-1, 10)
	get = myRange.Length()
	want = 12
	assert.Equal(t, want, get)

	myRange2 := NewRangeInt(5, 12)
	wantRange := NewRangeInt(5, 10)
	myRange.Intersect(myRange2)
	assert.Equal(t, myRange, wantRange)

	res := myRange.Union(myRange2)
	assert.Equal(t, myRange, myRange2)
	assert.Equal(t, res, true)

	assert.Equal(t, false, myRange.IsEmpty())

	assert.Equal(t, myRange.ContainsInt(12), true)

	assert.Equal(t, myRange.ContainsRange(NewRangeInt(6, 11)), true)

	assert.Equal(t, myRange.IsIntersect(NewRangeInt(12, 13)), true)

	assert.Equal(t, myRange.ToSlice(), myRange2.ToSlice())

	get, res = myRange.Minimum()
	assert.Equal(t, get, 5)
	assert.Equal(t, res, true)

	get, res = myRange.Maximum()
	assert.Equal(t, get, 12)
	assert.Equal(t, res, true)

	assert.Equal(t, myRange.String(), "[5,12]")
}
