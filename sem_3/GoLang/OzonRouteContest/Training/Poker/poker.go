package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	var t, p int
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()

	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)

	deckNum := []string{"AS", "AC", "AD", "AH", "KS", "KC", "KD", "KH", "QS", "QC", "QD", "QH", "JS", "JC", "JD", "JH", "TS", "TC", "TD", "TH", "9S", "9C", "9D", "9H", "8S", "8C", "8D", "8H", "7S", "7C", "7D", "7H", "6S", "6C", "6D", "6H", "5S", "5C", "5D", "5H", "4S", "4C", "4D", "4H", "3S", "3C", "3D", "3H", "2S", "2C", "2D", "2H"}

	deck := map[string]int{
		"AS": 0,
		"AC": 1,
		"AD": 2,
		"AH": 3,
		"KS": 4,
		"KC": 5,
		"KD": 6,
		"KH": 7,
		"QS": 8,
		"QC": 9,
		"QD": 10,
		"QH": 11,
		"JS": 12,
		"JC": 13,
		"JD": 14,
		"JH": 15,
		"TS": 16,
		"TC": 17,
		"TD": 18,
		"TH": 19,
		"9S": 20,
		"9C": 21,
		"9D": 22,
		"9H": 23,
		"8S": 24,
		"8C": 25,
		"8D": 26,
		"8H": 27,
		"7S": 28,
		"7C": 29,
		"7D": 30,
		"7H": 31,
		"6S": 32,
		"6C": 33,
		"6D": 34,
		"6H": 35,
		"5S": 36,
		"5C": 37,
		"5D": 38,
		"5H": 39,
		"4S": 40,
		"4C": 41,
		"4D": 42,
		"4H": 43,
		"3S": 44,
		"3C": 45,
		"3D": 46,
		"3H": 47,
		"2S": 48,
		"2C": 49,
		"2D": 50,
		"2H": 51,
	}

	fmt.Fscan(in, &t)
	for ; t > 0; t-- {
		fmt.Fscan(in, &p)
		players := make([][]string, p+1)
		used := make([]int, 52)
		greatest := 60
		pair, pairI := 60, 0
		for i := 1; i <= p; i++ {
			var a, b string
			fmt.Fscan(in, &a, &b)
			players[i] = make([]string, 2)
			players[i][0], players[i][1] = a, b
			used[deck[a]] = i
			used[deck[b]] = i
			if greatest/4*4 > deck[a]/4*4 {
				greatest = deck[a]
			}
			if greatest/4*4 > deck[b]/4*4 {
				greatest = deck[b]
			}
			if deck[a]/4*4 == deck[b]/4*4 {
				if pair/4*4 > deck[a]/4*4 {
					pair = deck[a]
					pairI = i
				}
			}
		}
		output := make([]string, 0)
		for i, c := range used {
			if c != 0 {
				continue
			}
			st := i / 4 * 4
			who := make([]int, p+1)
			for j := 0; j < 4; j++ {
				who[used[st+j]] += 1
			}
			mx, mxI := 0, 0
			for j := 1; j < p+1; j++ {
				if mx < who[j] {
					mx = who[j]
					mxI = j
				}
			}
			if pairI != 0 && mx < 2 {
				if mxI == 0 && pairI == 1 {
					output = append(output, deckNum[i])
				} else if mxI == 1 && pair/4*4 >= i/4*4 {
					output = append(output, deckNum[i])
				} else if mxI > 1 && pair/4*4 <= i/4*4 && pairI == 1 {
					output = append(output, deckNum[i])
				}
				continue
			}
			if mxI == 1 {
				output = append(output, deckNum[i])
				continue
			}
			if mxI == 0 {
				if used[greatest] == 1 || greatest/4*4 >= i/4*4 {
					output = append(output, deckNum[i])
				}
			}
		}
		fmt.Fprintln(out, len(output))
		for i := len(output) - 1; i >= 0; i-- {
			fmt.Fprintln(out, output[i])
		}
	}
}
