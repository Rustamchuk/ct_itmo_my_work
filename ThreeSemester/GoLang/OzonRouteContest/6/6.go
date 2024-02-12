package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()

	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)
	var t int
	fmt.Fscan(in, &t)
	for ; t > 0; t-- {
		var n, m int
		fmt.Fscan(in, &n, &m)
		book := make([][]int, n)
		rows := make([]int, n)
		rowsC := make([]int, n)
		columns := make([]int, m)
		columnsR := make([]int, m)
		supMin := 100
		xx, yy := 0, 0
		for i := 0; i < n; i++ {
			minR := 100
			var str string
			fmt.Fscan(in, &str)
			for _, c := range str {
				num, _ := strconv.Atoi(string(c))
				book[i] = append(book[i], num)
				if supMin > num {
					supMin = num
					xx, yy = i, len(book[i])-1
				}
				if minR > num {
					minR = num
					rowsC[i] = len(book[i]) - 1
				}
			}
			rows[i] = minR
		}
		for j := 0; j < m; j++ {
			minC := 100
			for i := 0; i < n; i++ {
				if book[i][j] < minC {
					minC = book[i][j]
					columnsR[j] = i
				}
			}
			columns[j] = minC
		}
		minn2 := 100
		x2, y2 := 0, 0
		for i := 0; i < n; i++ {
			for j := 0; j < m; j++ {
				if minn2 > book[i][j] && (i != xx || j != yy) {
					minn2 = book[i][j]
					x2, y2 = i, j
				}
			}
		}
		//maxx := 0
		x, y := 0, 0
		if x2 == xx {
			minC := 100
			y3 := 0
			for i := 0; i < m; i++ {
				if i == y2 || i == yy {
					continue
				}
				if minC > columns[i] {
					if columnsR[i] == xx {
						nw := 100
						for j := 0; j < n; j++ {
							if j == xx {
								continue
							}
							if nw > book[j][i] {
								nw = book[j][i]
							}
						}
						if nw < minC {
							minC = nw
							y3 = i
						}
						continue
					}
					minC = columns[i]
					y3 = i
				}
			}
			for i := 0; i < n; i++ {
				if i == x2 {
					continue
				}
				if minC > book[i][y2] {
					minC = book[i][y2]
					y3 = y2
				}
			}
			for i := 0; i < n; i++ {
				if i == xx {
					continue
				}
				if minC > book[i][yy] {
					minC = book[i][yy]
					y3 = yy
				}
			}
			x, y = xx, y3
			//for j := 0; j < m; j++ {
			//	minn := 100
			//	for l := 0; l < n; l++ {
			//		for k := 0; k < m; k++ {
			//			if minn > book[l][k] && l != xx && k != j {
			//				minn = book[l][k]
			//			}
			//		}
			//	}
			//	if minn > maxx {
			//		maxx = minn
			//		x, y = xx, j
			//	}
			//}
		} else if y2 == yy {
			minR := 100
			x3 := 0
			for i := 0; i < n; i++ {
				if i == x2 || i == xx {
					continue
				}
				if minR > rows[i] {
					if rowsC[i] == yy {
						nw := 100
						for j := 0; j < m; j++ {
							if j == yy {
								continue
							}
							if nw > book[i][j] {
								nw = book[i][j]
							}
						}
						if nw < minR {
							minR = nw
							x3 = i
						}
						continue
					}
					minR = rows[i]
					x3 = i
				}
			}
			for i := 0; i < m; i++ {
				if i == y2 {
					continue
				}
				if minR > book[x2][i] {
					minR = book[x2][i]
					x3 = x2
				}
			}
			for i := 0; i < m; i++ {
				if i == yy {
					continue
				}
				if minR > book[xx][i] {
					minR = book[xx][i]
					x3 = xx
				}
			}
			x, y = x3, yy
			//for i := 0; i < n; i++ {
			//	minn := 100
			//	for l := 0; l < n; l++ {
			//		for k := 0; k < m; k++ {
			//			if minn > book[l][k] && l != i && k != yy {
			//				minn = book[l][k]
			//			}
			//		}
			//	}
			//	if minn > maxx {
			//		maxx = minn
			//		x, y = i, yy
			//	}
			//}
		} else {
			nw := 100
			for i := 0; i < n; i++ {
				if i == xx {
					continue
				}
				if book[i][yy] < nw {
					nw = book[i][yy]
				}
			}
			columns[yy] = nw
			nw = 100
			for i := 0; i < n; i++ {
				if i == x2 {
					continue
				}
				if book[i][y2] < nw {
					nw = book[i][y2]
				}
			}
			columns[y2] = nw
			nw = 100
			for i := 0; i < m; i++ {
				if i == yy {
					continue
				}
				if book[xx][i] < nw {
					nw = book[xx][i]
				}
			}
			rows[xx] = nw
			nw = 100
			for i := 0; i < m; i++ {
				if i == y2 {
					continue
				}
				if book[x2][i] < nw {
					nw = book[x2][i]
				}
			}
			rows[x2] = nw

			if columns[yy] <= columns[y2] {
				if rows[x2] <= rows[xx] {
					x, y = x2, yy
				} else {
					x, y = xx, y2
				}
			} else {
				if rows[x2] >= rows[xx] {
					x, y = xx, y2
				} else {
					x, y = x2, yy
				}
			}
			//minn := 100
			//for l := 0; l < n; l++ {
			//	for k := 0; k < m; k++ {
			//		if minn > book[l][k] && l != x2 && k != yy {
			//			minn = book[l][k]
			//		}
			//	}
			//}
			//if minn > maxx {
			//	maxx = minn
			//	x, y = x2, yy
			//}
			//minn = 100
			//for l := 0; l < n; l++ {
			//	for k := 0; k < m; k++ {
			//		if minn > book[l][k] && l != xx && k != y2 {
			//			minn = book[l][k]
			//		}
			//	}
			//}
			//if minn > maxx {
			//	maxx = minn
			//	x, y = xx, y2
			//}
		}

		fmt.Fprint(out, x+1)
		fmt.Fprint(out, " ")
		fmt.Fprint(out, y+1)
		fmt.Fprintln(out)
	}
}
