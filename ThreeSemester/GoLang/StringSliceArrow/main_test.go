package main

import (
	"math"
	"testing"
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
