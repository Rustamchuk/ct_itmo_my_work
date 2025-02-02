package main

//import "fmt"
//
//type TreeNode struct {
//	Val   int
//	Left  *TreeNode
//	Right *TreeNode
//}
//
//func main() {
//	tree := TreeNode{5,
//		&TreeNode{4,
//			&TreeNode{11,
//				&TreeNode{7, nil, nil},
//				&TreeNode{2, nil, nil}},
//			nil},
//		&TreeNode{8,
//			&TreeNode{13, nil, nil},
//			&TreeNode{4, nil, nil}},
//	}
//	fmt.Println(hasPathSum(&tree, 22))
//}
//
//func hasPathSum(root *TreeNode, targetSum int) bool {
//	if root == nil {
//		return false
//	}
//	return walk(root, targetSum)
//}
//
//func walk(root *TreeNode, targetSum int) bool {
//	if root == nil {
//		if targetSum == 0 {
//			return true
//		}
//		return false
//	}
//	i := targetSum - root.Val
//	return hasPathSum(root.Left, i) || hasPathSum(root.Right, i)
//}
