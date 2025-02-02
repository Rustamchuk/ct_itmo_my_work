package learning

import (
	"sort"
)

//go:generate minimock

type Serve interface {
	GetTutorsIDPreferIndividual(studentID int64) ([]int64, bool)
	GetTopSubjects(topN int) ([]string, bool)
}

type Repo interface {
	GetStudentInfo(id int64) (*studentInfo, bool)
	GetAllSubjects() ([]string, bool)
	GetAllSubjectsInfo() ([]subjectInfo, bool)
}

type Tutor interface {
	TutorsID(subject string) []int64
	Subjects() []string
}

type Service struct {
	individualTutorService Tutor
	groupTutorService      Tutor
	repository             Repo
}

func NewService(
	individualTutorService Tutor,
	groupTutorService Tutor,
	repository Repo,
) *Service {
	return &Service{
		individualTutorService: individualTutorService,
		groupTutorService:      groupTutorService,
		repository:             repository,
	}
}

func (s *Service) GetTutorsIDPreferIndividual(studentID int64) ([]int64, bool) {
	studentInfo, ok := s.repository.GetStudentInfo(studentID)
	if !ok {
		return nil, ok
	}

	tutorsID := s.individualTutorService.TutorsID(studentInfo.Subject)
	if len(tutorsID) == 0 {
		tutorsID = s.groupTutorService.TutorsID(studentInfo.Subject)
		if len(tutorsID) == 0 {
			return nil, false
		}
		return tutorsID, true
	}

	return tutorsID, true
}

func (s *Service) GetTopSubjects(topN int) ([]string, bool) {
	subjects, ok := s.repository.GetAllSubjectsInfo()
	if !ok {
		return nil, ok
	}
	if len(subjects) < topN {
		return nil, false
	}

	sort.SliceStable(
		subjects,
		func(i, j int) bool {
			return subjects[i].numberOfTutors < subjects[j].numberOfTutors
		},
	)

	return fromSubject(subjects[:topN]), true
}
