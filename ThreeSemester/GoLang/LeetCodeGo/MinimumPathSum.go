package main

//func main() {
//	grid := [][]int{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}
//
//	n := len(grid)
//	k := len(grid[0])
//	for i := 1; i < n; i++ {
//		grid[i][0] += grid[i-1][0]
//	}
//	for i := 1; i < k; i++ {
//		grid[0][i] += grid[0][i-1]
//	}
//	for i := 1; i < n; i++ {
//		for j := 1; j < k; j++ {
//			grid[i][j] += min(grid[i-1][j], grid[i][j-1])
//		}
//	}
//	fmt.Println(grid[n-1][k-1])
//}

func min(a int, b int) int {
	if a > b {
		return b
	}
	return a
}
