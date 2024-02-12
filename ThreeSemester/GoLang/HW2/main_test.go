package main

import (
	"math"
	"testing"
	"github.com/stretchr/testify/require"
	"slices"
)

func TestMethods(t *testing.T) {
	str := "China丂Symbol"
	i := 0
	for _, r := range str {
		if r != getCharByIndex(str, i) {
			t.Errorf("Wrong rune by index [%d] != %q\n", i, r)
		}
		i++
	}

	strSlice := []int{1, 4, 0, 5, 8, 7}
	sliced := getStringBySliceOfIndexes(str, strSlice)
	correctSliced := "haC丂my"
	if sliced != correctSliced {
		t.Errorf("Wrong string by slise actual %s | expected %s\n", sliced, correctSliced)
	}

	complA := complex(1, 2)
	complB := complex(2, 1)
	complC := complex(1, 2)
	if isComplexEqual(complA, complB) || !isComplexEqual(complA, complC) {
		t.Errorf("Wrong complex equal check\n")
	}

	a1, b1, c1 := 2.0, 5.0, 2.0
	x1, x2 := complex(-1.0/2, 0), complex(-2.0, 0)
	y1, y2 := getRootsOfQuadraticEquation(a1, b1, c1)
	if !isComplexEqual(x1, y1) || !isComplexEqual(x2, y2) {
		t.Errorf("Wrong square roots for a, b, c = %f, %f, %f. "+
			"Expected: %g, %g | Actual: %g %g\n", a1, b1, c1, x1, x2, y1, y2)
	}
	a1, b1, c1 = 1, 1, 1
	x1, x2 = complex(-1.0/2, math.Sqrt(3)/2), complex(-1.0/2, -math.Sqrt(3)/2)
	y1, y2 = getRootsOfQuadraticEquation(a1, b1, c1)
	if !isComplexEqual(x1, y1) || !isComplexEqual(x2, y2) {
		t.Errorf("Wrong square roots for a, b, c = %f, %f, %f. "+
			"Expected: %g, %g | Actual: %g %g\n", a1, b1, c1, x1, x2, y1, y2)
	}

	unsorted := []int{8, 2, 5, 4, 6, 7, 1, 0, 3, 9, 10}
	sorted := []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	actual := mergeSort(unsorted)
	if !isSliceEqual(sorted, actual) {
		t.Errorf("Wrong sort expected %x, actual %x\n", sorted, actual)
	}

	reversed := []int{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
	reverseSliceOne(sorted)
	if !isSliceEqual(reversed, sorted) {
		t.Errorf("Wrong reverse 1 expected %x, actual %x\n", reversed, sorted)
	}
	reverseSliceOne(sorted)

	actual = reverseSliceTwo(sorted)
	if !isSliceEqual(reversed, actual) {
		t.Errorf("Wrong reverse 2 expected %x, actual %x\n", reversed, actual)
	}

	a, b := 0, 1
	swapPointers(&a, &b)
	if a == 0 || b == 1 {
		t.Errorf("Wrond swap pointers expected 1 0. Actual: %d %d\n", a, b)
	}

	deleteIndexes := []int{5, 9, 0, 3, 6, 0}
	for _, i := range deleteIndexes {
		reversed = deleteByIndex(reversed, i)
	}
	correct := []int{8, 7, 4, 3, 2}
	if !isSliceEqual(correct, reversed) {
		t.Errorf("Wrong delete expected %x, actual %x\n", correct, reversed)

	}
}

func TestAddPointers(t *testing.T) {
	require.Nil(t, addPointers(nil, nil))
}

func TestSwapPointers(t *testing.T) {
	a := 2
	b := 3

	swapPointers(&a, &b)

	require.Equal(t, a, 3)
	require.Equal(t, b, 2)
}

func TestGetCharByIndex(t *testing.T) {
	t.Run("simple get", func(t *testing.T) {
		inputString := "椅子摆得不整重新摆一下儿"

		result := getCharByIndex(inputString, 4)

		require.Equal(t, string(result), "不")
	})
}

func TestIsComplexEqual(t *testing.T) {
	require.True(t, isComplexEqual(complex(2, math.Pi), complex(2, 3.141592)))
}

func TestGetStringBySliceOfIndexes(t *testing.T) {
	t.Run("simple get", func(t *testing.T) {
		inputString := "椅子摆得不整重新摆一下儿"
		targetSlice := []int{0, 1, 3, 5}

		result := getStringBySliceOfIndexes(inputString, targetSlice)

		require.Equal(t, result, "椅子得整")
	})
}

func TestReverseSliceOne(t *testing.T) {
	t.Run("simple reverse", func(t *testing.T) {
		s := []int{1, 2, 3, 4, 5}

		reverseSliceOne(s)

		require.Equal(t, []int{5, 4, 3, 2, 1}, s)
	})

	t.Run("pass nil", func(t *testing.T) {
		reverseSliceOne(nil)
	})
}

func TestReverseSliceTwo(t *testing.T) {
	t.Run("simple reverse", func(t *testing.T) {
		s := []int{1, 2, 3, 4, 5}

		reversed := reverseSliceTwo(s)

		require.Equal(t, s, []int{1, 2, 3, 4, 5})
		require.Equal(t, reversed, []int{5, 4, 3, 2, 1})
	})

	t.Run("pass nil", func(t *testing.T) {
		reverseSliceTwo(nil)
	})
}
func TestGetCharByIndexSimple(t *testing.T) {
	str, idx := "abcdef", 4
	got := getCharByIndex(str, idx)
	want := "e"

	require.Equal(t, want, string(got))
}

func TestGetCharByIndexEmoji(t *testing.T) {
	str, idx := "a🙂🙃🌚😑😐z", 3
	got := getCharByIndex(str, idx)
	want := "🌚"

	require.Equal(t, want, string(got))
}

func TestGetStringBySliceOfIndexesSimple(t *testing.T) {
	str, indexes := "abcdef", []int{1, 3, 3, 3, 4, 2}
	got := getStringBySliceOfIndexes(str, indexes)
	want := "bdddec"

	require.Equal(t, want, got)
}

func TestGetStringBySliceOfIndexesEmoji(t *testing.T) {
	str, indexes := "a🙂🙃🌚😑😐z", []int{6, 3, 2, 5, 4, 4}
	got := getStringBySliceOfIndexes(str, indexes)
	want := "z🌚🙃😐😑😑"

	require.Equal(t, want, got)
}

func TestIsComplexEqualSimpleEqual(t *testing.T) {
	a, b := complex(1, 2), complex(1, 2)
	got := isComplexEqual(a, b)

	require.True(t, got)
}

func TestIsComplexEqualSimpleNotEqual(t *testing.T) {
	a, b := complex(1, 2), complex(2, 1)
	got := isComplexEqual(a, b)

	require.False(t, got)
}

func TestIsComplexEqualNotEqualByPrecision(t *testing.T) {
	a, b := complex(1, 2), complex(1.0001, 2.0001)
	got := isComplexEqual(a, b)

	require.False(t, got)
}

func TestIsComplexEqualByPrecision(t *testing.T) {
	a, b := complex(1, 2), complex(1.000000001, 2.000000001)
	got := isComplexEqual(a, b)

	require.True(t, got)
}

func TestGetRootsOfQuadraticEquationSimple(t *testing.T) {
	a, b, c := 1.0, -3.0, 2.0
	got1, got2 := getRootsOfQuadraticEquation(a, b, c)
	want1, want2 := complex(1, 0), complex(2, 0)

	if !(isComplexEqual(got1, want1) && isComplexEqual(got2, want2) || isComplexEqual(got1, want2) && isComplexEqual(got2, want1)) {
		t.Errorf("getRootsOfQuadraticEquation is bad, want (%v, %v) got (%v, %v)", want1, want2, got1, got2)
	}
}

func TestGetRootsOfQuadraticEquationSimpleSingleRoot(t *testing.T) {
	a, b, c := 1.0, -2.0, 1.0
	got1, got2 := getRootsOfQuadraticEquation(a, b, c)
	want1, want2 := complex(1, 0), complex(1, 0)

	if !(isComplexEqual(got1, want1) && isComplexEqual(got2, want2) || isComplexEqual(got1, want2) && isComplexEqual(got2, want1)) {
		t.Errorf("getRootsOfQuadraticEquation is bad, want (%v, %v) got (%v, %v)", want1, want2, got1, got2)
	}
}

func TestGetRootsOfQuadraticEquationComplexResult(t *testing.T) {
	a, b, c := 1.0, 0.0, 1.0
	got1, got2 := getRootsOfQuadraticEquation(a, b, c)
	want1, want2 := complex(0, -1), complex(0, 1)

	if !(isComplexEqual(got1, want1) && isComplexEqual(got2, want2) || isComplexEqual(got1, want2) && isComplexEqual(got2, want1)) {
		t.Errorf("getRootsOfQuadraticEquation is bad, want (%v, %v) got (%v, %v)", want1, want2, got1, got2)
	}
}

func TestMergeSortSimple(t *testing.T) {
	s := []int{2, 1, 3}
	got := mergeSort(s)
	want := []int{1, 2, 3}

	require.Equal(t, len(got), len(want))
	require.True(t, slices.Equal(want, got))
}

func TestMergeSortEqElements(t *testing.T) {
	s := []int{6, 3, 2, 5, 4, 4}
	got := mergeSort(s)
	want := []int{2, 3, 4, 4, 5, 6}

	require.Equal(t, len(want), len(got))
	require.True(t, slices.Equal(want, got))
}

func TestReverseSliceOneEmpty(t *testing.T) {
	s := []int{}
	reverseSliceOne(s)
	got := s
	want := []int{}

	require.Equal(t, len(want), len(got))
	require.True(t, slices.Equal(want, got))
}

func TestReverseSliceOneOddSize(t *testing.T) {
	s := []int{1, 2, 3, 4, 5}
	reverseSliceOne(s)
	got := s
	want := []int{5, 4, 3, 2, 1}

	require.Equal(t, len(want), len(got))
}

func TestReverseSliceOneEvenSize(t *testing.T) {
	s := []int{1, 2, 3, 4}
	reverseSliceOne(s)
	got := s
	want := []int{4, 3, 2, 1}

	require.Equal(t, len(want), len(got))
}

func TestReverseSliceTwoEmpty(t *testing.T) {
	s := []int{}
	sCopy := make([]int, len(s))
	copy(sCopy, s)
	got := reverseSliceTwo(s)
	want := []int{}

	require.Equal(t, want, got)

	if len(got) != len(want) {
		t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
	}
	for i, w := range want {
		if got[i] != w {
			t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
		}
	}

	for i := 0; i < len(got); i++ {
		got[i] = i + 42
	}

	for i, w := range sCopy {
		if s[i] != w {
			t.Errorf("reverseSliceTwo is bad, returned slice modification affects input")
		}
	}
}

func TestReverseSliceTwoOddSize(t *testing.T) {
	s := []int{1, 2, 3, 4, 5}
	sCopy := make([]int, len(s))
	copy(sCopy, s)
	got := reverseSliceTwo(s)
	want := []int{5, 4, 3, 2, 1}

	if len(got) != len(want) {
		t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
	}
	for i, w := range want {
		if got[i] != w {
			t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
		}
	}

	for i := 0; i < len(got); i++ {
		got[i] = i + 42
	}

	for i, w := range sCopy {
		if s[i] != w {
			t.Errorf("reverseSliceTwo is bad, returned slice modification affects input")
		}
	}
}

func TestReverseSliceTwoEvenSize(t *testing.T) {
	s := []int{1, 2, 3, 4}
	sCopy := make([]int, len(s))
	copy(sCopy, s)
	got := reverseSliceTwo(s)
	want := []int{4, 3, 2, 1}

	if len(got) != len(want) {
		t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
	}
	for i, w := range want {
		if got[i] != w {
			t.Errorf("reverseSliceTwo is bad, want %v got %v", want, got)
		}
	}

	for i := 0; i < len(got); i++ {
		got[i] = i + 42
	}

	for i, w := range sCopy {
		if s[i] != w {
			t.Errorf("reverseSliceTwo is bad, returned slice modification affects input")
		}
	}
}

func TestSwapPointersSimple(t *testing.T) {
	a, b := 1, 2
	swapPointers(&a, &b)
	wantA, wantB := 2, 1

	require.Equal(t, wantA, a)
	require.Equal(t, wantB, b)
}

func TestIsSliceEqualEmpty(t *testing.T) {
	a, b := []int{}, []int{}
	got := isSliceEqual(a, b)

	require.True(t, got)
}

func TestIsSliceEqualTrue(t *testing.T) {
	a, b := []int{1, 2, 3}, []int{1, 2, 3}
	got := isSliceEqual(a, b)

	require.True(t, got)
}

func TestIsSliceEqualFalse(t *testing.T) {
	a, b := []int{1, 2, 3}, []int{1, 4, 3}
	got := isSliceEqual(a, b)

	require.False(t, got)
}

func TestIsSliceEqualDifferentLength(t *testing.T) {
	a, b := []int{1, 2, 3}, []int{1, 2}
	got := isSliceEqual(a, b)

	require.False(t, got)
}

func TestDeleteByIndexSimple(t *testing.T) {
	s, idx := []int{0, 1, 2, 3, 4, 5, 6, 7, 8}, 3
	got := deleteByIndex(s, idx)
	want := []int{0, 1, 2, 4, 5, 6, 7, 8}

	require.Equal(t, want, got)
}

func TestDeleteByIndexFirst(t *testing.T) {
	s, idx := []int{0, 1, 2, 3, 4, 5, 6, 7, 8}, 0
	got := deleteByIndex(s, idx)
	want := []int{1, 2, 3, 4, 5, 6, 7, 8}

	require.Equal(t, want, got)
}

func TestDeleteByIndexLast(t *testing.T) {
	s, idx := []int{0, 1, 2, 3, 4, 5, 6, 7, 8}, 8
	got := deleteByIndex(s, idx)
	want := []int{0, 1, 2, 3, 4, 5, 6, 7}

	require.Equal(t, want, got)
}
