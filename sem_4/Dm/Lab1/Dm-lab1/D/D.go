package main

import (
	"fmt"
)

const MAXSZ = 10

var (
	k, r  int
	P     []int64
	degsQ [][]int64
	coefs []Fraction
)

type Fraction struct {
	Numerator   int64
	Denominator int64
}

func NewFraction(numerator, denominator int64) Fraction {
	return Fraction{
		Numerator:   numerator,
		Denominator: denominator,
	}
}

func gcd(a, b int64) int64 {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func (f Fraction) Simplify() Fraction {
	g := gcd(f.Numerator, f.Denominator)
	f.Numerator /= g
	f.Denominator /= g
	if f.Denominator < 0 {
		f.Numerator = -f.Numerator
		f.Denominator = -f.Denominator
	}
	return f
}

func (f Fraction) Add(other Fraction) Fraction {
	lcm := f.Denominator / gcd(f.Denominator, other.Denominator) * other.Denominator
	return NewFraction(f.Numerator*(lcm/f.Denominator)+other.Numerator*(lcm/other.Denominator), lcm).Simplify()
}

func (f Fraction) Multiply(c int64) Fraction {
	return NewFraction(f.Numerator*c, f.Denominator).Simplify()
}

func (f Fraction) Divide(c int64) Fraction {
	return NewFraction(f.Numerator, f.Denominator*c).Simplify()
}

func evaluateDegsQ() {
	for kk := 2; kk < len(degsQ); kk++ {
		degsQ[kk] = make([]int64, 2*MAXSZ+1)
		for i := 0; i < 2*MAXSZ+1; i++ {
			mult := int64(0)
			for j := 0; j <= i; j++ {
				val1 := int64(0)
				if j < len(degsQ[1]) {
					val1 = degsQ[1][j]
				}
				val2 := int64(0)
				if i-j < len(degsQ[kk-1]) {
					val2 = degsQ[kk-1][i-j]
				}
				mult += val1 * val2
			}
			degsQ[kk][i] = mult
		}
	}
}

func findCoefs() {
	coefs = make([]Fraction, len(P))
	for i := len(P) - 1; i >= 0; i-- {
		sum := NewFraction(P[i], 1)
		for j := len(P) - 1; j > i; j-- {
			sum = sum.Add(coefs[j].Multiply(degsQ[j][i]))
		}
		coefs[i] = sum.Divide(degsQ[i][i])
	}
}

func main() {
	fmt.Scanf("%d %d", &r, &k)
	P = make([]int64, k+1)
	for i := 0; i <= k; i++ {
		fmt.Scanf("%d", &P[i])
	}
	degsQ = make([][]int64, k+2)
	for i := range degsQ {
		degsQ[i] = make([]int64, 2*MAXSZ+1)
		if i < 2 {
			degsQ[i][0] = 1
			if i == 1 {
				degsQ[i][1] = int64(-r)
			}
		}
	}
	evaluateDegsQ()
	findCoefs()

	for _, coef := range coefs {
		fmt.Printf("%d/%d ", coef.Numerator, coef.Denominator)
	}
	fmt.Println()
}
