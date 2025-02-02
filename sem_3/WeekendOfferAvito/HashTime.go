package main

import (
	"fmt"
	"time"
)

type CommitInfo struct {
	Hash      string
	BuildTime int
}

func main() {
	//fmt.Println(FindTheBrokenOne([]CommitInfo{
	//	{"1", 2},
	//	{"2", 2},
	//	{"3", 4},
	//	{"4", 4},
	//	{"5", 6},
	//	{"6", 7},
	//}, 3))
	a, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-01T11:00:00Z")
	b, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-01T12:00:00Z")
	c, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-02T10:00:00Z")
	d, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-02T11:00:00Z")

	a1, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-01T08:00:00Z")
	a2, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-01T09:00:00Z")
	a3, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-02T08:00:00Z")
	a4, _ := time.Parse("2006-01-02T15:04:05Z", "2007-07-02T10:00:00Z")
	fmt.Println(BestInterestingShop(
		[][]Visit{
			{Visit{"B", a, b}},
			{Visit{"A", c, d}},
		},
		[][]Visit{
			{Visit{"B", a1, a2}},
			{Visit{"A", a3, a4}},
		}))
}

type Visit struct {
	ShopName    string
	VisitedFrom time.Time
	VisitedTo   time.Time
}

func BestInterestingShop(vasyaVisits [][]Visit, petyaVisits [][]Visit) string {
	maxDur := time.Duration(0)
	maxName := "None"
	for d := 0; d < len(vasyaVisits); d++ {
		for i, j := 0, 0; i < len(vasyaVisits[d]) && j < len(vasyaVisits[d]); {
			if vasyaVisits[d][i].ShopName == petyaVisits[d][j].ShopName {
				start := maxTime(vasyaVisits[d][i].VisitedFrom, petyaVisits[d][j].VisitedFrom)
				end := minTime(vasyaVisits[d][i].VisitedTo, petyaVisits[d][j].VisitedTo)
				if end.After(start) {
					dur := end.Sub(start)
					if dur > maxDur {
						maxDur = dur
						maxName = vasyaVisits[d][i].ShopName
					}
				}
				i++
				j++
				continue
			}

			if vasyaVisits[d][i].VisitedTo.Before(petyaVisits[d][j].VisitedTo) {
				i++
				continue
			}
			j++
		}
	}
	return maxName
}

func maxTime(a, b time.Time) time.Time {
	if a.Before(b) {
		return b
	}
	return a
}

func minTime(a, b time.Time) time.Time {
	if a.Before(b) {
		return a
	}
	return b
}

func FindTheBrokenOne(commits []CommitInfo, thresholdTime int) string {
	l := -1
	r := len(commits)
	for l < r-1 {
		m := (l + r) / 2
		if commits[m].BuildTime < thresholdTime {
			l = m
		} else {
			r = m
		}
	}
	if r == len(commits) {
		return ""
	}
	return commits[r].Hash
}
