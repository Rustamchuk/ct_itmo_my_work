package main

//
//func main() {
//	s := "MCMXCIV"
//
//	n := len(s)
//	runes := []rune(s)
//	numbers := map[rune]int{
//		'I': 1,
//		'V': 5,
//		'X': 10,
//		'L': 50,
//		'C': 100,
//		'D': 500,
//		'M': 1000,
//	}
//
//	ans := 0
//	for i := 1; i < n; i++ {
//		if numbers[runes[i-1]] < numbers[runes[i]] {
//			ans -= numbers[runes[i-1]]
//		} else {
//			ans += numbers[runes[i-1]]
//		}
//	}
//	ans += numbers[runes[n-1]]
//	fmt.Println(ans)
//}
