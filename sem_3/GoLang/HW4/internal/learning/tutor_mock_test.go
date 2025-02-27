package learning

// Code generated by http://github.com/gojuno/minimock (dev). DO NOT EDIT.

//go:generate minimock -i hw4/internal/learning.Tutor -o ./tutor_mock_test.go -n TutorMock

import (
	"sync"
	mm_atomic "sync/atomic"
	mm_time "time"

	"github.com/gojuno/minimock/v3"
)

// TutorMock implements Tutor
type TutorMock struct {
	t minimock.Tester

	funcSubjects          func() (sa1 []string)
	inspectFuncSubjects   func()
	afterSubjectsCounter  uint64
	beforeSubjectsCounter uint64
	SubjectsMock          mTutorMockSubjects

	funcTutorsID          func(subject string) (ia1 []int64)
	inspectFuncTutorsID   func(subject string)
	afterTutorsIDCounter  uint64
	beforeTutorsIDCounter uint64
	TutorsIDMock          mTutorMockTutorsID
}

// NewTutorMock returns a mock for Tutor
func NewTutorMock(t minimock.Tester) *TutorMock {
	m := &TutorMock{t: t}
	if controller, ok := t.(minimock.MockController); ok {
		controller.RegisterMocker(m)
	}

	m.SubjectsMock = mTutorMockSubjects{mock: m}

	m.TutorsIDMock = mTutorMockTutorsID{mock: m}
	m.TutorsIDMock.callArgs = []*TutorMockTutorsIDParams{}

	return m
}

type mTutorMockSubjects struct {
	mock               *TutorMock
	defaultExpectation *TutorMockSubjectsExpectation
	expectations       []*TutorMockSubjectsExpectation
}

// TutorMockSubjectsExpectation specifies expectation struct of the Tutor.Subjects
type TutorMockSubjectsExpectation struct {
	mock *TutorMock

	results *TutorMockSubjectsResults
	Counter uint64
}

// TutorMockSubjectsResults contains results of the Tutor.Subjects
type TutorMockSubjectsResults struct {
	sa1 []string
}

// Expect sets up expected params for Tutor.Subjects
func (mmSubjects *mTutorMockSubjects) Expect() *mTutorMockSubjects {
	if mmSubjects.mock.funcSubjects != nil {
		mmSubjects.mock.t.Fatalf("TutorMock.Subjects mock is already set by Set")
	}

	if mmSubjects.defaultExpectation == nil {
		mmSubjects.defaultExpectation = &TutorMockSubjectsExpectation{}
	}

	return mmSubjects
}

// Inspect accepts an inspector function that has same arguments as the Tutor.Subjects
func (mmSubjects *mTutorMockSubjects) Inspect(f func()) *mTutorMockSubjects {
	if mmSubjects.mock.inspectFuncSubjects != nil {
		mmSubjects.mock.t.Fatalf("Inspect function is already set for TutorMock.Subjects")
	}

	mmSubjects.mock.inspectFuncSubjects = f

	return mmSubjects
}

// Return sets up results that will be returned by Tutor.Subjects
func (mmSubjects *mTutorMockSubjects) Return(sa1 []string) *TutorMock {
	if mmSubjects.mock.funcSubjects != nil {
		mmSubjects.mock.t.Fatalf("TutorMock.Subjects mock is already set by Set")
	}

	if mmSubjects.defaultExpectation == nil {
		mmSubjects.defaultExpectation = &TutorMockSubjectsExpectation{mock: mmSubjects.mock}
	}
	mmSubjects.defaultExpectation.results = &TutorMockSubjectsResults{sa1}
	return mmSubjects.mock
}

// Set uses given function f to mock the Tutor.Subjects method
func (mmSubjects *mTutorMockSubjects) Set(f func() (sa1 []string)) *TutorMock {
	if mmSubjects.defaultExpectation != nil {
		mmSubjects.mock.t.Fatalf("Default expectation is already set for the Tutor.Subjects method")
	}

	if len(mmSubjects.expectations) > 0 {
		mmSubjects.mock.t.Fatalf("Some expectations are already set for the Tutor.Subjects method")
	}

	mmSubjects.mock.funcSubjects = f
	return mmSubjects.mock
}

// Subjects implements Tutor
func (mmSubjects *TutorMock) Subjects() (sa1 []string) {
	mm_atomic.AddUint64(&mmSubjects.beforeSubjectsCounter, 1)
	defer mm_atomic.AddUint64(&mmSubjects.afterSubjectsCounter, 1)

	if mmSubjects.inspectFuncSubjects != nil {
		mmSubjects.inspectFuncSubjects()
	}

	if mmSubjects.SubjectsMock.defaultExpectation != nil {
		mm_atomic.AddUint64(&mmSubjects.SubjectsMock.defaultExpectation.Counter, 1)

		mm_results := mmSubjects.SubjectsMock.defaultExpectation.results
		if mm_results == nil {
			mmSubjects.t.Fatal("No results are set for the TutorMock.Subjects")
		}
		return (*mm_results).sa1
	}
	if mmSubjects.funcSubjects != nil {
		return mmSubjects.funcSubjects()
	}
	mmSubjects.t.Fatalf("Unexpected call to TutorMock.Subjects.")
	return
}

// SubjectsAfterCounter returns a count of finished TutorMock.Subjects invocations
func (mmSubjects *TutorMock) SubjectsAfterCounter() uint64 {
	return mm_atomic.LoadUint64(&mmSubjects.afterSubjectsCounter)
}

// SubjectsBeforeCounter returns a count of TutorMock.Subjects invocations
func (mmSubjects *TutorMock) SubjectsBeforeCounter() uint64 {
	return mm_atomic.LoadUint64(&mmSubjects.beforeSubjectsCounter)
}

// MinimockSubjectsDone returns true if the count of the Subjects invocations corresponds
// the number of defined expectations
func (m *TutorMock) MinimockSubjectsDone() bool {
	for _, e := range m.SubjectsMock.expectations {
		if mm_atomic.LoadUint64(&e.Counter) < 1 {
			return false
		}
	}

	// if default expectation was set then invocations count should be greater than zero
	if m.SubjectsMock.defaultExpectation != nil && mm_atomic.LoadUint64(&m.afterSubjectsCounter) < 1 {
		return false
	}
	// if func was set then invocations count should be greater than zero
	if m.funcSubjects != nil && mm_atomic.LoadUint64(&m.afterSubjectsCounter) < 1 {
		return false
	}
	return true
}

// MinimockSubjectsInspect logs each unmet expectation
func (m *TutorMock) MinimockSubjectsInspect() {
	for _, e := range m.SubjectsMock.expectations {
		if mm_atomic.LoadUint64(&e.Counter) < 1 {
			m.t.Error("Expected call to TutorMock.Subjects")
		}
	}

	// if default expectation was set then invocations count should be greater than zero
	if m.SubjectsMock.defaultExpectation != nil && mm_atomic.LoadUint64(&m.afterSubjectsCounter) < 1 {
		m.t.Error("Expected call to TutorMock.Subjects")
	}
	// if func was set then invocations count should be greater than zero
	if m.funcSubjects != nil && mm_atomic.LoadUint64(&m.afterSubjectsCounter) < 1 {
		m.t.Error("Expected call to TutorMock.Subjects")
	}
}

type mTutorMockTutorsID struct {
	mock               *TutorMock
	defaultExpectation *TutorMockTutorsIDExpectation
	expectations       []*TutorMockTutorsIDExpectation

	callArgs []*TutorMockTutorsIDParams
	mutex    sync.RWMutex
}

// TutorMockTutorsIDExpectation specifies expectation struct of the Tutor.TutorsID
type TutorMockTutorsIDExpectation struct {
	mock    *TutorMock
	params  *TutorMockTutorsIDParams
	results *TutorMockTutorsIDResults
	Counter uint64
}

// TutorMockTutorsIDParams contains parameters of the Tutor.TutorsID
type TutorMockTutorsIDParams struct {
	subject string
}

// TutorMockTutorsIDResults contains results of the Tutor.TutorsID
type TutorMockTutorsIDResults struct {
	ia1 []int64
}

// Expect sets up expected params for Tutor.TutorsID
func (mmTutorsID *mTutorMockTutorsID) Expect(subject string) *mTutorMockTutorsID {
	if mmTutorsID.mock.funcTutorsID != nil {
		mmTutorsID.mock.t.Fatalf("TutorMock.TutorsID mock is already set by Set")
	}

	if mmTutorsID.defaultExpectation == nil {
		mmTutorsID.defaultExpectation = &TutorMockTutorsIDExpectation{}
	}

	mmTutorsID.defaultExpectation.params = &TutorMockTutorsIDParams{subject}
	for _, e := range mmTutorsID.expectations {
		if minimock.Equal(e.params, mmTutorsID.defaultExpectation.params) {
			mmTutorsID.mock.t.Fatalf("Expectation set by When has same params: %#v", *mmTutorsID.defaultExpectation.params)
		}
	}

	return mmTutorsID
}

// Inspect accepts an inspector function that has same arguments as the Tutor.TutorsID
func (mmTutorsID *mTutorMockTutorsID) Inspect(f func(subject string)) *mTutorMockTutorsID {
	if mmTutorsID.mock.inspectFuncTutorsID != nil {
		mmTutorsID.mock.t.Fatalf("Inspect function is already set for TutorMock.TutorsID")
	}

	mmTutorsID.mock.inspectFuncTutorsID = f

	return mmTutorsID
}

// Return sets up results that will be returned by Tutor.TutorsID
func (mmTutorsID *mTutorMockTutorsID) Return(ia1 []int64) *TutorMock {
	if mmTutorsID.mock.funcTutorsID != nil {
		mmTutorsID.mock.t.Fatalf("TutorMock.TutorsID mock is already set by Set")
	}

	if mmTutorsID.defaultExpectation == nil {
		mmTutorsID.defaultExpectation = &TutorMockTutorsIDExpectation{mock: mmTutorsID.mock}
	}
	mmTutorsID.defaultExpectation.results = &TutorMockTutorsIDResults{ia1}
	return mmTutorsID.mock
}

// Set uses given function f to mock the Tutor.TutorsID method
func (mmTutorsID *mTutorMockTutorsID) Set(f func(subject string) (ia1 []int64)) *TutorMock {
	if mmTutorsID.defaultExpectation != nil {
		mmTutorsID.mock.t.Fatalf("Default expectation is already set for the Tutor.TutorsID method")
	}

	if len(mmTutorsID.expectations) > 0 {
		mmTutorsID.mock.t.Fatalf("Some expectations are already set for the Tutor.TutorsID method")
	}

	mmTutorsID.mock.funcTutorsID = f
	return mmTutorsID.mock
}

// When sets expectation for the Tutor.TutorsID which will trigger the result defined by the following
// Then helper
func (mmTutorsID *mTutorMockTutorsID) When(subject string) *TutorMockTutorsIDExpectation {
	if mmTutorsID.mock.funcTutorsID != nil {
		mmTutorsID.mock.t.Fatalf("TutorMock.TutorsID mock is already set by Set")
	}

	expectation := &TutorMockTutorsIDExpectation{
		mock:   mmTutorsID.mock,
		params: &TutorMockTutorsIDParams{subject},
	}
	mmTutorsID.expectations = append(mmTutorsID.expectations, expectation)
	return expectation
}

// Then sets up Tutor.TutorsID return parameters for the expectation previously defined by the When method
func (e *TutorMockTutorsIDExpectation) Then(ia1 []int64) *TutorMock {
	e.results = &TutorMockTutorsIDResults{ia1}
	return e.mock
}

// TutorsID implements Tutor
func (mmTutorsID *TutorMock) TutorsID(subject string) (ia1 []int64) {
	mm_atomic.AddUint64(&mmTutorsID.beforeTutorsIDCounter, 1)
	defer mm_atomic.AddUint64(&mmTutorsID.afterTutorsIDCounter, 1)

	if mmTutorsID.inspectFuncTutorsID != nil {
		mmTutorsID.inspectFuncTutorsID(subject)
	}

	mm_params := &TutorMockTutorsIDParams{subject}

	// Record call args
	mmTutorsID.TutorsIDMock.mutex.Lock()
	mmTutorsID.TutorsIDMock.callArgs = append(mmTutorsID.TutorsIDMock.callArgs, mm_params)
	mmTutorsID.TutorsIDMock.mutex.Unlock()

	for _, e := range mmTutorsID.TutorsIDMock.expectations {
		if minimock.Equal(e.params, mm_params) {
			mm_atomic.AddUint64(&e.Counter, 1)
			return e.results.ia1
		}
	}

	if mmTutorsID.TutorsIDMock.defaultExpectation != nil {
		mm_atomic.AddUint64(&mmTutorsID.TutorsIDMock.defaultExpectation.Counter, 1)
		mm_want := mmTutorsID.TutorsIDMock.defaultExpectation.params
		mm_got := TutorMockTutorsIDParams{subject}
		if mm_want != nil && !minimock.Equal(*mm_want, mm_got) {
			mmTutorsID.t.Errorf("TutorMock.TutorsID got unexpected parameters, want: %#v, got: %#v%s\n", *mm_want, mm_got, minimock.Diff(*mm_want, mm_got))
		}

		mm_results := mmTutorsID.TutorsIDMock.defaultExpectation.results
		if mm_results == nil {
			mmTutorsID.t.Fatal("No results are set for the TutorMock.TutorsID")
		}
		return (*mm_results).ia1
	}
	if mmTutorsID.funcTutorsID != nil {
		return mmTutorsID.funcTutorsID(subject)
	}
	mmTutorsID.t.Fatalf("Unexpected call to TutorMock.TutorsID. %v", subject)
	return
}

// TutorsIDAfterCounter returns a count of finished TutorMock.TutorsID invocations
func (mmTutorsID *TutorMock) TutorsIDAfterCounter() uint64 {
	return mm_atomic.LoadUint64(&mmTutorsID.afterTutorsIDCounter)
}

// TutorsIDBeforeCounter returns a count of TutorMock.TutorsID invocations
func (mmTutorsID *TutorMock) TutorsIDBeforeCounter() uint64 {
	return mm_atomic.LoadUint64(&mmTutorsID.beforeTutorsIDCounter)
}

// Calls returns a list of arguments used in each call to TutorMock.TutorsID.
// The list is in the same order as the calls were made (i.e. recent calls have a higher index)
func (mmTutorsID *mTutorMockTutorsID) Calls() []*TutorMockTutorsIDParams {
	mmTutorsID.mutex.RLock()

	argCopy := make([]*TutorMockTutorsIDParams, len(mmTutorsID.callArgs))
	copy(argCopy, mmTutorsID.callArgs)

	mmTutorsID.mutex.RUnlock()

	return argCopy
}

// MinimockTutorsIDDone returns true if the count of the TutorsID invocations corresponds
// the number of defined expectations
func (m *TutorMock) MinimockTutorsIDDone() bool {
	for _, e := range m.TutorsIDMock.expectations {
		if mm_atomic.LoadUint64(&e.Counter) < 1 {
			return false
		}
	}

	// if default expectation was set then invocations count should be greater than zero
	if m.TutorsIDMock.defaultExpectation != nil && mm_atomic.LoadUint64(&m.afterTutorsIDCounter) < 1 {
		return false
	}
	// if func was set then invocations count should be greater than zero
	if m.funcTutorsID != nil && mm_atomic.LoadUint64(&m.afterTutorsIDCounter) < 1 {
		return false
	}
	return true
}

// MinimockTutorsIDInspect logs each unmet expectation
func (m *TutorMock) MinimockTutorsIDInspect() {
	for _, e := range m.TutorsIDMock.expectations {
		if mm_atomic.LoadUint64(&e.Counter) < 1 {
			m.t.Errorf("Expected call to TutorMock.TutorsID with params: %#v", *e.params)
		}
	}

	// if default expectation was set then invocations count should be greater than zero
	if m.TutorsIDMock.defaultExpectation != nil && mm_atomic.LoadUint64(&m.afterTutorsIDCounter) < 1 {
		if m.TutorsIDMock.defaultExpectation.params == nil {
			m.t.Error("Expected call to TutorMock.TutorsID")
		} else {
			m.t.Errorf("Expected call to TutorMock.TutorsID with params: %#v", *m.TutorsIDMock.defaultExpectation.params)
		}
	}
	// if func was set then invocations count should be greater than zero
	if m.funcTutorsID != nil && mm_atomic.LoadUint64(&m.afterTutorsIDCounter) < 1 {
		m.t.Error("Expected call to TutorMock.TutorsID")
	}
}

// MinimockFinish checks that all mocked methods have been called the expected number of times
func (m *TutorMock) MinimockFinish() {
	if !m.minimockDone() {
		m.MinimockSubjectsInspect()

		m.MinimockTutorsIDInspect()
		m.t.FailNow()
	}
}

// MinimockWait waits for all mocked methods to be called the expected number of times
func (m *TutorMock) MinimockWait(timeout mm_time.Duration) {
	timeoutCh := mm_time.After(timeout)
	for {
		if m.minimockDone() {
			return
		}
		select {
		case <-timeoutCh:
			m.MinimockFinish()
			return
		case <-mm_time.After(10 * mm_time.Millisecond):
		}
	}
}

func (m *TutorMock) minimockDone() bool {
	done := true
	return done &&
		m.MinimockSubjectsDone() &&
		m.MinimockTutorsIDDone()
}
