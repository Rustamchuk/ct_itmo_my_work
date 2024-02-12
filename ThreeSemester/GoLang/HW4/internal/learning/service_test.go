package learning

import (
	"github.com/gojuno/minimock/v3"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestGetTutorsIDPreferIndividual(t *testing.T) {
	testCases := []struct {
		name          string
		studentID     int64
		stInfo        *studentInfo
		okInfo        bool
		individTutors []int64
		groupTutors   []int64
		resultTutors  []int64
		result        bool
	}{
		{
			"NotExistStudent",
			-1,
			&studentInfo{},
			false,
			nil,
			nil,
			nil,
			false,
		},
		{
			"NotExistTutors",
			1,
			&studentInfo{"Maxim", 12, "Math"},
			true,
			nil,
			nil,
			nil,
			false,
		},
		{
			"IndividualTutors",
			2,
			&studentInfo{"Maxim", 12, "Math"},
			true,
			[]int64{1, 2, 3},
			nil,
			[]int64{1, 2, 3},
			true,
		},
		{
			"GroupTutors",
			3,
			&studentInfo{"Maxim", 12, "Math"},
			true,
			nil,
			[]int64{1, 2, 3},
			[]int64{1, 2, 3},
			true,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			mc := minimock.NewController(t)
			serveMock := NewServeMock(mc)

			individTutor := NewTutorMock(mc)
			groupTutor := NewTutorMock(mc)
			repository := NewRepoMock(mc)
			service := NewService(individTutor, groupTutor, repository)

			repository.GetStudentInfoMock.
				Expect(tc.studentID).Return(tc.stInfo, tc.okInfo)

			individTutor.TutorsIDMock.
				Expect(tc.stInfo.Subject).Return(tc.individTutors)

			groupTutor.TutorsIDMock.
				Expect(tc.stInfo.Subject).Return(tc.groupTutors)

			serveMock.GetTutorsIDPreferIndividualMock.
				Expect(tc.studentID).Return(tc.resultTutors, tc.result)

			gotTeachers, gotOk := service.GetTutorsIDPreferIndividual(tc.studentID)
			assert.Equal(t, tc.resultTutors, gotTeachers)
			assert.Equal(t, tc.result, gotOk)
		})
	}
}

func TestGetTopSubjects(t *testing.T) {
	testCases := []struct {
		name         string
		topN         int
		subjectsInfo []subjectInfo
		okInfo       bool
		resSubjects  []string
		result       bool
	}{
		{
			"NotExistSubject",
			-1,
			nil,
			false,
			nil,
			false,
		},
		{
			"InvalidN",
			9,
			make([]subjectInfo, 8),
			true,
			nil,
			false,
		},
		{
			"Top-5",
			5,
			[]subjectInfo{
				{"Math", 10},
				{"Rus", 3},
				{"Eng", 15},
				{"Ger", 7},
				{"Fran", 1},
				{"PE", 9},
				{"Alg", 12},
				{"Geom", 11},
				{"Hist", 11},
			},
			true,
			[]string{"Fran", "Rus", "Ger", "PE", "Math"},
			true,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			mc := minimock.NewController(t)

			individTutor := NewTutorMock(mc)
			groupTutor := NewTutorMock(mc)
			repository := NewRepoMock(mc)
			service := NewService(individTutor, groupTutor, repository)

			repository.GetAllSubjectsInfoMock.
				Expect().Return(tc.subjectsInfo, tc.okInfo)

			gotStrs, gotOk := service.GetTopSubjects(tc.topN)
			assert.Equal(t, tc.resSubjects, gotStrs)
			assert.Equal(t, tc.result, gotOk)
		})
	}
}
