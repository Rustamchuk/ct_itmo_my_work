package main

import "fmt"

func main() {
	var t int
	fmt.Scan(&t)
	var s, out string
	var last int
	numsLib := map[rune]int{
		'0': 0,
		'1': 0,
		'2': 0,
		'3': 0,
		'4': 0,
		'5': 0,
		'6': 0,
		'7': 0,
		'8': 0,
		'9': 0,
	}
	mode := 0
	for ; t > 0; t-- {
		fmt.Scan(&s)
		last = 0
		out = ""
	inLoop:
		for i, ch := range s {
			_, ok := numsLib[ch]
			switch mode {
			case 0:
				if ok {
					out = "-"
					break inLoop
				}
				mode++
				break
			case 1:
				if !ok {
					out = "-"
					break inLoop
				}
				mode++
				break
			case 2:
				mode++
				if !ok {
					mode++
				}
				break
			case 3:
				if ok {
					out = "-"
					break inLoop
				}
				mode++
				break
			case 4:
				if ok {
					out = "-"
					break inLoop
				}
				mode = 0
				out += s[last:i+1] + " "
				last = i + 1
				break
			}
		}
		if mode != 0 {
			mode = 0
			out = "-"
		}
		fmt.Println(out)
	}
}
