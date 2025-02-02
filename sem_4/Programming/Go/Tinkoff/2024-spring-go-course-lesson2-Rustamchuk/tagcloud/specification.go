package tagcloud

import "sort"

// TagCloud aggregates statistics about used tags
type TagCloud struct {
	tagMap   map[string]*TagStat
	tagNames []*TagStat
}

// TagStat represents statistics regarding single tag
type TagStat struct {
	Tag             string
	OccurrenceCount int
}

// New should create a valid TagCloud instance
func New() TagCloud {
	return TagCloud{
		tagMap:   make(map[string]*TagStat),
		tagNames: make([]*TagStat, 0),
	}
}

// AddTag should add a tag to the cloud if it wasn't present and increase tag occurrence count
// thread-safety is not needed
func (t *TagCloud) AddTag(tagName string) {
	if tag, ok := t.tagMap[tagName]; ok {
		tag.OccurrenceCount++
		return
	}
	tag := &TagStat{
		Tag:             tagName,
		OccurrenceCount: 1,
	}
	t.tagMap[tagName] = tag
	t.tagNames = append(t.tagNames, tag)
}

// TopN should return top N most frequent tags ordered in descending order by occurrence count
// if there are multiple tags with the same occurrence count then the order is defined by implementation
// if n is greater that TagCloud size then all elements should be returned
// thread-safety is not needed
// there are no restrictions on time complexity
func (t *TagCloud) TopN(n int) []TagStat {
	t.sort()
	n = min(n, len(t.tagNames))
	res := make([]TagStat, 0)
	for _, tag := range t.tagNames[0:n] {
		res = append(res, *tag)
	}
	return res
}

func (t *TagCloud) sort() {
	sort.Slice(t.tagNames, func(a, b int) bool {
		return t.tagNames[a].OccurrenceCount > t.tagNames[b].OccurrenceCount
	})
}
