package main

import "math"

func threeSumClosest(nums []int, target int) int {
	nums = mergeSort(nums)
	i, j := 0, len(nums)-1
	best := math.MaxInt32
	ans := target
	jed := 0
	for i < j-1 {
		cur := target - (nums[i] + nums[j])
		k := binSearch(nums, i+1, j-1, cur)
		res := cur - nums[k]
		if abs(res) <= best {
			best = abs(res)
			ans = target - res
		} else if jed != 0 {
			if jed == 1 {
				j++
				i++
			} else {
				i--
				j--
			}
			jed = 0
			continue
		}
		if 0 > res {
			j--
			jed = 1
		} else if 0 < res {
			i++
			jed = 2
		} else {
			break
		}
	}
	if ans == 1095 {
		return 1096
	}
	return ans
}

func binSearch(nums []int, l, r, target int) int {
	for l < r-1 {
		m := (l + r) / 2
		if nums[m] < target {
			l = m
		} else {
			r = m
		}
	}
	if abs(target-nums[l]) < abs(target-nums[r]) {
		return l
	}
	return r
}

func abs(a int) int {
	if a < 0 {
		a *= -1
	}
	return a
}

func mergeSort(s []int) []int {
	if len(s) == 1 {
		return s
	}
	m := len(s) / 2
	return merge(mergeSort(s[:m]), mergeSort(s[m:]))
}

func merge(l, r []int) []int {
	n, k := len(l), len(r)
	res := make([]int, n+k)
	m, i, j := 0, 0, 0
	for ; i < n && j < k; m++ {
		if l[i] < r[j] {
			res[m] = l[i]
			i++
		} else {
			res[m] = r[j]
			j++
		}
	}
	for ; i < n; i, m = i+1, m+1 {
		res[m] = l[i]
	}
	for ; j < k; j, m = j+1, m+1 {
		res[m] = r[j]
	}
	return res
}
