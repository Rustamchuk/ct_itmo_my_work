package main

import (
	"fmt"
	"strings"
)

func main() {
	var t int
	var s string
	fmt.Scan(&t)
	for ; t > 0; t-- {
		fmt.Scan(&s)
		sArr := make([][]string, 1)
		sArr[0] = make([]string, 0)
		indLine := 0
		ind := 0
		for _, c := range s {
			switch c {
			case 'L':
				if ind > 0 {
					ind--
				}
				break
			case 'R':
				if ind < len(sArr[indLine]) {
					ind++
				}
				break
			case 'U':
				if indLine > 0 {
					indLine--
				}
				if len(sArr[indLine]) < ind {
					ind = len(sArr[indLine])
				}
				break
			case 'D':
				if indLine < len(sArr)-1 {
					indLine++
				}
				if len(sArr[indLine]) < ind {
					ind = len(sArr[indLine])
				}
				break
			case 'B':
				ind = 0
				break
			case 'E':
				ind = len(sArr[indLine])
				break
			case 'N':
				sArr = append(sArr[0:indLine+1], sArr[indLine:]...)
				sArr[indLine] = append(make([]string, 0), sArr[indLine][:ind]...)
				indLine++
				sArr[indLine] = append(make([]string, 0), sArr[indLine][ind:]...)
				ind = 0
				break
			default:
				if ind == len(sArr[indLine]) {
					sArr[indLine] = append(sArr[indLine], string(c))
					ind++
					break
				}
				sArr[indLine] = append(sArr[indLine][0:ind+1], sArr[indLine][ind:]...)
				sArr[indLine][ind] = string(c)
				ind++
				break
			}
		}
		for _, str := range sArr {
			fmt.Println(strings.Join(str, ""))
		}
		fmt.Println("-")
	}
}
