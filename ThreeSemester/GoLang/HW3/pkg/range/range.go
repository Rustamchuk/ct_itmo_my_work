package rangeI

import (
	"fmt"
	"math"
)

type RangeInt interface {
	Length() int
	Intersect(other RangeInt)
	Union(other RangeInt) bool
	IsEmpty() bool
	ContainsInt(i int) bool
	ContainsRange(other RangeInt) bool
	IsIntersect(other RangeInt) bool
	ToSlice() []int
	Minimum() (int, bool)
	Maximum() (int, bool)
	String() string
}

func NewRangeInt(a, b int) RangeInt {
	return &MyRangeInt{
		min: a,
		max: b,
	}
}

type MyRangeInt struct {
	min int
	max int
}

func (r *MyRangeInt) Length() int {
	if r.IsEmpty() {
		return 0
	}
	return r.max - r.min + 1
}

func (r *MyRangeInt) Intersect(other RangeInt) {
	if !r.IsIntersect(other) {
		r.min, r.max = 1, 0
		return
	}
	minO, _ := other.Minimum()
	maxO, _ := other.Maximum()
	if r.ContainsInt(minO) {
		r.min = minO
	}
	if r.ContainsInt(maxO) {
		r.max = maxO
	}
}

func (r *MyRangeInt) Union(other RangeInt) bool {
	if other.IsEmpty() {
		return true
	}
	minO, _ := other.Minimum()
	maxO, _ := other.Maximum()
	if r.IsEmpty() {
		r.min = minO
		r.max = maxO
		return true
	}
	if !r.IsIntersect(other) {
		if math.Abs(float64(r.max-minO)) == 1 {
			r.max = maxO
			return true
		} else if math.Abs(float64(maxO-r.min)) == 1 {
			r.min = minO
			return true
		}
		return false
	}
	if !r.ContainsInt(minO) {
		r.min = minO
	}
	if !r.ContainsInt(maxO) {
		r.max = maxO
	}
	return true
}

func (r *MyRangeInt) IsEmpty() bool {
	return r.max < r.min
}

func (r *MyRangeInt) ContainsInt(i int) bool {
	return r.min <= i && i <= r.max
}

func (r *MyRangeInt) ContainsRange(other RangeInt) bool {
	if other.IsEmpty() {
		return true
	}
	minO, _ := other.Minimum()
	maxO, _ := other.Maximum()
	return r.ContainsInt(minO) && r.ContainsInt(maxO) && !r.IsEmpty()
}

func (r *MyRangeInt) IsIntersect(other RangeInt) bool {
	minO, _ := other.Minimum()
	maxO, _ := other.Maximum()
	return !other.IsEmpty() && !r.IsEmpty() &&
		(r.ContainsInt(minO) || r.ContainsInt(maxO) ||
			other.ContainsInt(r.min) || other.ContainsInt(r.max))
}

func (r *MyRangeInt) ToSlice() []int {
	n := r.Length()
	nums := make([]int, n)
	for i, j := 0, r.min; i < n; i, j = i+1, j+1 {
		nums[i] = j
	}
	return nums
}

func (r *MyRangeInt) Minimum() (int, bool) {
	if r.IsEmpty() {
		return math.MaxInt32, false
	}
	return r.min, true
}

func (r *MyRangeInt) Maximum() (int, bool) {
	if r.IsEmpty() {
		return math.MinInt32, false
	}
	return r.max, true
}

func (r *MyRangeInt) String() string {
	if r.IsEmpty() {
		return ""
	}
	return fmt.Sprintf("[%d,%d]", r.min, r.max)
}
