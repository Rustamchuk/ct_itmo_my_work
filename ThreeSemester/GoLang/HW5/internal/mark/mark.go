package mark

import (
	"bufio"
	"fmt"
	"io"
	"math"
	"sort"
	"strconv"
	"strings"
)

type Student struct {
	Name string
	Mark int
}

type StudentsStatistic interface {
	SummaryByStudent(student string) (int, bool)     // default_value, false - если студента нет
	AverageByStudent(student string) (float32, bool) // default_value, false - если студента нет
	Students() []string
	Summary() int
	Median() int
	MostFrequent() int
}

func ReadStudentsStatistic(reader io.Reader) (StudentsStatistic, error) {
	if reader == nil {
		return nil, fmt.Errorf("reader nil")
	}
	students := make([]Student, 0)
	scanner := bufio.NewScanner(reader)
	for scanner.Scan() {
		line := scanner.Text()
		info := strings.Split(line, "\t")
		if len(info) != 2 {
			continue
		}
		mark, err := strconv.Atoi(info[1])
		if err != nil {
			return nil, err
		}
		if mark > 10 || mark < 1 {
			continue
		}

		students = append(students, Student{
			Name: info[0],
			Mark: mark,
		})
	}
	if scanner.Err() != nil {
		return nil, scanner.Err()
	}
	return NewStudentsStat(students), nil
}

func WriteStudentsStatistic(writer io.Writer, statistic StudentsStatistic) error {
	if statistic == nil {
		return fmt.Errorf("students nil")
	}
	_, err := writer.Write([]byte(fmt.Sprintf("%v\t%v\t%v\n",
		statistic.Summary(), statistic.Median(), statistic.MostFrequent())))
	if err != nil {
		return err
	}

	students := statistic.Students()
	strs := make([]string, 0, len(students))
	for _, pupil := range students {
		pupilSum, ok1 := statistic.SummaryByStudent(pupil)
		pupilAverage, ok2 := statistic.AverageByStudent(pupil)
		if !ok1 || !ok2 {
			continue
		}
		if pupilAverage != float32(math.Round(float64(pupilAverage))) {
			strs = append(strs, fmt.Sprintf("%v\t%v\t%.2f",
				pupil, pupilSum, pupilAverage))
			continue
		}
		strs = append(strs, fmt.Sprintf("%v\t%v\t%v",
			pupil, pupilSum, pupilAverage))
	}
	_, err = writer.Write([]byte(strings.Join(strs, "\n")))
	if err != nil {
		return err
	}

	return nil
}

type studentsStat struct {
	students []Student
}

func NewStudentsStat(students []Student) StudentsStatistic {
	return &studentsStat{
		students: students,
	}
}

func (s *studentsStat) SummaryByStudent(student string) (int, bool) {
	sum, cnt := s.sumStudent(student)
	return sum, cnt != 0
}

func (s *studentsStat) AverageByStudent(student string) (float32, bool) {
	sum, cnt := s.sumStudent(student)
	if cnt == 0 {
		return 0, false
	}
	return float32(math.Round(float64(sum)/float64(cnt)*100) / 100), true
}

func (s *studentsStat) Students() []string {
	sort.Slice(s.students, func(i, j int) bool {
		sumI, _ := s.SummaryByStudent(s.students[i].Name)
		sumJ, _ := s.SummaryByStudent(s.students[j].Name)
		return sumI > sumJ
	})
	strMap := make(map[string]int, len(s.students))
	strs := make([]string, 0, len(s.students))
	for _, pupil := range s.students {
		if _, ok := strMap[pupil.Name]; ok {
			continue
		}
		strs = append(strs, pupil.Name)
		strMap[pupil.Name] = 1
	}
	return strs
}

func (s *studentsStat) Summary() int {
	sum := 0
	for _, pupil := range s.students {
		sum += pupil.Mark
	}
	return sum
}

func (s *studentsStat) Median() int {
	sum := s.Summary()
	if sum == 0 {
		return 0
	}
	return int(math.Ceil(float64(sum) / float64(len(s.students))))
}

func (s *studentsStat) MostFrequent() int {
	markRate := make([]int, 11)
	for _, pupil := range s.students {
		markRate[pupil.Mark] += 1
	}

	maxMark, maxCnt := 0, 1
	for i := 1; i < 11; i++ {
		if markRate[i] > maxCnt {
			maxCnt = markRate[i]
			maxMark = i
		} else if markRate[i] == maxCnt {
			maxMark = i
		}
	}

	return maxMark
}

func (s *studentsStat) sumStudent(student string) (int, int) {
	sum, cnt := 0, 0
	for _, pupil := range s.students {
		if pupil.Name == student {
			sum += pupil.Mark
			cnt += 1
		}
	}
	return sum, cnt
}
