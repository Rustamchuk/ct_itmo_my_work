package main

//
//import "fmt"
//
//type ListNode struct {
//	Val  int
//	Next *ListNode
//}
//
//func main() {
//	head := ListNode{Val: 1, Next: &ListNode{Val: 1, Next: &ListNode{Val: 2, Next: nil}}}
//	fmt.Println(deleteDuplicates(&head))
//}
//
//func deleteDuplicates(head *ListNode) *ListNode {
//	mp := make(map[int]int)
//	start := &ListNode{Val: head.Val, Next: nil}
//	cur := start
//	for head != nil {
//		_, ok := mp[head.Val]
//		if !ok {
//			mp[head.Val] = 1
//			cur.Next = &ListNode{Val: head.Val, Next: nil}
//			cur = cur.Next
//		}
//		head = head.Next
//	}
//	cur.Next = nil
//	return start.Next
//}
