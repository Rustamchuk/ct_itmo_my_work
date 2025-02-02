package main

import "fmt"

func main() {
	var n int
	var cur string
	fmt.Scan(&n)
	for i := 0; i < n; i++ {
		fmt.Scan(&cur)
		fmt.Println(check(cur))
	}
}

func check(cur string) string {
	word := map[rune]int{
		'T': 1,
		'I': 1,
		'N': 1,
		'K': 1,
		'O': 1,
		'F': 2,
	}
	if len(cur) != 7 {
		return "No"
	}
	for _, c := range cur {
		word[c]--
	}
	for _, v := range word {
		if v != 0 {
			return "No"
		}
	}
	return "Yes"
}
