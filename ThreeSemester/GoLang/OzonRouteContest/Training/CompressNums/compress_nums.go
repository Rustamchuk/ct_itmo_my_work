package main

import (
	"fmt"
	"strconv"
	"strings"
)

func main() {
	var t, n int
	fmt.Scan(&t)
	for ; t > 0; t-- {
		fmt.Scan(&n)
		var st, ln, cur, mode, last int
		arr := make([]int, 0)
		fmt.Scan(&st)
		last = st
		ln = 1
		for ; n > 1; n-- {
			fmt.Scan(&cur)
			switch last - cur {
			case -1:
				if mode == -1 {
					arr = append(arr, st, -(ln - 1))
					mode = 0
					ln = 1
					st = cur
					break
				}
				mode = 1
				ln++
				break
			case 1:
				if mode == 1 {
					arr = append(arr, st, ln-1)
					mode = 0
					ln = 1
					st = cur
					break
				}
				mode = -1
				ln++
				break
			default:
				if mode == -1 {
					arr = append(arr, st, -(ln - 1))
				} else {
					arr = append(arr, st, ln-1)
				}
				mode = 0
				ln = 1
				st = cur
				break
			}
			last = cur
		}
		if mode == -1 {
			arr = append(arr, st, -(ln - 1))
		} else {
			arr = append(arr, st, ln-1)
		}
		fmt.Println(len(arr))
		str := make([]string, len(arr))
		for i := 0; i < len(arr); i++ {
			str[i] = strconv.Itoa(arr[i])
		}
		fmt.Println(strings.Join(str, " "))
	}
}
