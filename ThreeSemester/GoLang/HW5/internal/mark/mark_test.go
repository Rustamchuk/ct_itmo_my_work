package mark

import (
	"bytes"
	"io"
	"log"
	"os"
	"reflect"
	"testing"
)

func TestReadStudentsStatistic(t *testing.T) {
	type wants struct {
		statistic StudentsStatistic
		ok        bool
	}
	file2, err := os.Open("data_test/input_2.tsv")
	if err != nil {
		log.Fatal(err)
	}
	defer file2.Close()
	file3, err := os.Open("data_test/input_3.tsv")
	if err != nil {
		log.Fatal(err)
	}
	defer file3.Close()
	tests := []struct {
		name  string
		arg   io.Reader
		wants wants
	}{
		{
			"BigFile",
			file2,
			wants{
				NewStudentsStat([]Student{
					{"Назаров Рустам Русланович", 4},
					{"Назаров Петя Русланович", 5},
					{"Назаров Вася Русланович", 7},
					{"Назаров Коля Русланович", 9},
					{"Назаров Серега Русланович", 10},
					{"Назаров Рустам Русланович", 1},
					{"Назаров Петя Русланович", 2},
					{"Назаров Петя Русланович", 4},
					{"Назаров Рустам Русланович", 3},
					{"Вася Русланович", 5},
					{"Назаров Коля", 6},
				}),
				true,
			},
		},
		{
			"EmptyFile",
			file3,
			wants{
				NewStudentsStat([]Student{}),
				true,
			},
		},
		{
			"NilFile",
			nil,
			wants{
				nil,
				false,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, err := ReadStudentsStatistic(tt.arg)
			if (err != nil) == tt.wants.ok {
				t.Errorf("ReadStudentsStatistic() error = %v, wantErr %v", err, tt.wants.ok)
				return
			}
			if !reflect.DeepEqual(got, tt.wants.statistic) {
				t.Errorf("ReadStudentsStatistic() got = %v, want %v", got, tt.wants.statistic)
			}
		})
	}
}

func TestWriteStudentsStatistic(t *testing.T) {
	type wants struct {
		outText string
		ok      bool
	}
	testCases := []struct {
		name  string
		arg   StudentsStatistic
		wants wants
	}{
		{
			"BigFile",
			NewStudentsStat([]Student{
				{"Назаров Рустам Русланович", 4},
				{"Назаров Петя Русланович", 5},
				{"Назаров Вася Русланович", 7},
				{"Назаров Коля Русланович", 9},
				{"Назаров Серега Русланович", 10},
				{"Назаров Рустам Русланович", 1},
				{"Назаров Петя Русланович", 2},
				{"Назаров Петя Русланович", 4},
				{"Назаров Рустам Русланович", 3},
				{"Вася Русланович", 5},
				{"Назаров Коля", 6},
			}),
			wants{
				"56\t6\t5\n" +
					"Назаров Петя Русланович\t11\t3.67\n" +
					"Назаров Серега Русланович\t10\t10\n" +
					"Назаров Коля Русланович\t9\t9\n" +
					"Назаров Рустам Русланович\t8\t2.67\n" +
					"Назаров Вася Русланович\t7\t7\n" +
					"Назаров Коля\t6\t6\n" +
					"Вася Русланович\t5\t5",
				true,
			},
		},
		{
			"EmptyFile",
			NewStudentsStat([]Student{}),
			wants{
				"0\t0\t0\n",
				true,
			},
		},
		{
			"NilFile",
			nil,
			wants{
				"",
				false,
			},
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			writer := &bytes.Buffer{}
			err := WriteStudentsStatistic(writer, tc.arg)
			if (err != nil) == tc.wants.ok {
				t.Errorf("WriteStudentsStatistic() error = %v, wantErr %v", err, tc.wants.ok)
				return
			}
			gotWriter := writer.String()
			if gotWriter != tc.wants.outText {
				t.Errorf("WriteStudentsStatistic() gotWriter = %v, want %v", gotWriter, tc.wants.outText)
			}
		})
	}
}

func Test_studentsStat_SummaryByStudent(t *testing.T) {
	type wants struct {
		sum int
		ok  bool
	}
	testCases := []struct {
		name  string
		field []Student
		arg   string
		wants wants
	}{
		{
			"TwoMarks",
			[]Student{
				{"Rustam", 6},
				{"Rostik", 8},
				{"Rustam", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{10, true},
		},
		{
			"Name-Surname",
			[]Student{
				{"Rustam", 6},
				{"Rostik", 8},
				{"Rustam Nazarov", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{6, true},
		},
		{
			"NotFound",
			[]Student{
				{"Rustam Zhukov", 6},
				{"Rostik", 8},
				{"Rustam Nazarov", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{0, false},
		},
		{
			"Empty List",
			[]Student{},
			"Rustam",
			wants{0, false},
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			gotSum, gotOk := s.SummaryByStudent(tc.arg)
			if gotSum != tc.wants.sum {
				t.Errorf("SummaryByStudent() Int got = %v, want %v", gotSum, tc.wants.sum)
			}
			if gotOk != tc.wants.ok {
				t.Errorf("SummaryByStudent() Bool got = %v, want %v", gotOk, tc.wants.ok)
			}
		})
	}
}

func Test_studentsStat_AverageByStudent(t *testing.T) {
	type wants struct {
		aver float32
		ok   bool
	}
	testCases := []struct {
		name  string
		field []Student
		arg   string
		wants wants
	}{
		{
			"TwoMarks",
			[]Student{
				{"Rustam", 6},
				{"Rostik", 8},
				{"Rustam", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{5.00, true},
		},
		{
			"Name-Surname",
			[]Student{
				{"Rustam", 6},
				{"Rostik", 8},
				{"Rustam Nazarov", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{6, true},
		},
		{
			"NotFound",
			[]Student{
				{"Rustam Zhukov", 6},
				{"Rostik", 8},
				{"Rustam Nazarov", 4},
				{"Rostik", 3},
			},
			"Rustam",
			wants{0, false},
		},
		{
			"Empty List",
			[]Student{},
			"Rustam",
			wants{0, false},
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			aver, ok := s.AverageByStudent(tc.arg)
			if aver != tc.wants.aver {
				t.Errorf("AverageByStudent() Float got = %v, want %v", aver, tc.wants.aver)
			}
			if ok != tc.wants.ok {
				t.Errorf("AverageByStudent() Bool got = %v, want %v", ok, tc.wants.ok)
			}
		})
	}
}

func Test_studentsStat_Median(t *testing.T) {
	testCases := []struct {
		name  string
		field []Student
		want  int
	}{
		{
			"Ceil 21/6",
			[]Student{
				{"", 2},
				{"", 6},
				{"", 7},
				{"", 1},
				{"", 1},
				{"", 4},
			},
			4,
		},
		{
			"EmptyList",
			[]Student{},
			0,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			got := s.Median()
			if got != tc.want {
				t.Errorf("Median() = %v, want %v", got, tc.want)
			}
		})
	}
}

func Test_studentsStat_MostFrequent(t *testing.T) {
	testCases := []struct {
		name  string
		field []Student
		want  int
	}{
		{
			"OneAnswer",
			[]Student{
				{"", 2},
				{"", 6},
				{"", 7},
				{"", 1},
				{"", 1},
				{"", 4},
			},
			1,
		},
		{
			"GreaterOne",
			[]Student{
				{"", 2},
				{"", 6},
				{"", 7},
				{"", 1},
				{"", 5},
				{"", 4},
			},
			7,
		},
		{
			"EmptyList",
			[]Student{},
			0,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			got := s.MostFrequent()
			if got != tc.want {
				t.Errorf("MostFrequent() = %v, want %v", got, tc.want)
			}
		})
	}
}

func Test_studentsStat_Students(t *testing.T) {
	testCases := []struct {
		name  string
		field []Student
		want  []string
	}{
		{
			"Sort",
			[]Student{
				{"A", 2},
				{"B", 6},
				{"C", 7},
				{"D", 1},
				{"E", 5},
				{"F", 4},
			},
			[]string{"C", "B", "E", "F", "A", "D"},
		},
		{
			"EmptyList",
			[]Student{},
			[]string{},
		}, {
			"EqualMarks",
			[]Student{
				{"A", 2},
				{"B", 6},
				{"C", 7},
				{"D", 2},
				{"E", 6},
				{"F", 4},
			},
			[]string{"C", "B", "E", "F", "A", "D"},
		}, {
			"EqualStudent",
			[]Student{
				{"A", 2},
				{"A", 6},
				{"B", 7},
				{"B", 1},
				{"A", 5},
				{"B", 4},
			},
			[]string{"A", "B"},
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			got := s.Students()
			if !reflect.DeepEqual(got, tc.want) {
				t.Errorf("Students() = %v, want %v", got, tc.want)
			}
		})
	}
}

func Test_studentsStat_Summary(t *testing.T) {
	testCases := []struct {
		name  string
		field []Student
		want  int
	}{
		{
			"Different",
			[]Student{
				{"A", 2},
				{"B", 6},
				{"C", 7},
				{"D", 1},
				{"E", 5},
				{"F", 4},
			},
			25,
		},
		{
			"EmptyList",
			[]Student{},
			0,
		}, {
			"EqualMarks",
			[]Student{
				{"A", 2},
				{"B", 6},
				{"C", 7},
				{"D", 2},
				{"E", 6},
				{"F", 4},
			},
			27,
		}, {
			"EqualStudent",
			[]Student{
				{"A", 2},
				{"A", 6},
				{"B", 7},
				{"B", 1},
				{"A", 5},
				{"B", 4},
			},
			25,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			s := &studentsStat{
				students: tc.field,
			}
			got := s.Summary()
			if got != tc.want {
				t.Errorf("Summary() = %v, want %v", got, tc.want)
			}
		})
	}
}
