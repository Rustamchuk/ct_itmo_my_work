package main

//func main() {
//	nums := []int{-2, 1, -3, 4, -1, 2, 1, -5, 4}
//
//	maxSum := nums[0]
//	curSum := nums[0]
//	for i := 1; i < len(nums); i++ {
//		curSum = max(nums[i], curSum+nums[i])
//		maxSum = max(curSum, maxSum)
//	}
//	fmt.Println(maxSum)
//}

func max(a int, b int) int {
	if a > b {
		return a
	}
	return b
}
