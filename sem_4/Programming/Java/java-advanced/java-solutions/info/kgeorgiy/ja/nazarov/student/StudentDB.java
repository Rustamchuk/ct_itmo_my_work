package info.kgeorgiy.ja.nazarov.student;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {
    private enum StudentComparators implements Comparator<Student> {
        BY_NAME(Comparator
                .comparing(Student::getLastName)
                .thenComparing(Student::getFirstName)
                .thenComparing(Student::getId, Comparator.reverseOrder())
        ),
        By_ID(Comparator.comparingInt(Student::getId));

        private final Comparator<Student> comparator;

        StudentComparators(Comparator<Student> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Student o1, Student o2) {
            return comparator.compare(o1, o2);
        }
    }

    private final Function<Student, String> getFullName =
            student -> student.getFirstName() + " " + student.getLastName();

    private <T> List<T> getStudentInfo(List<Student> students, Function<Student, T> getInfo) {
        return students.stream().map(getInfo).toList();
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getStudentInfo(
                students,
                Student::getFirstName
        );
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getStudentInfo(
                students,
                Student::getLastName
        );
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getStudentInfo(
                students,
                Student::getGroup
        );
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getStudentInfo(
                students,
                getFullName
        );
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(StudentComparators.By_ID)
                .map(Student::getFirstName)
                .orElse("");
    }

    private List<Student> sortStudentsByInfo(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).toList();
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsByInfo(
                students,
                StudentComparators.By_ID
        );
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsByInfo(
                students,
                StudentComparators.BY_NAME
        );
    }

    private <T> List<Student> findStudentInfo(Collection<Student> students, T info, Function<Student, T> getInfo) {
        return sortStudentsByName(
                students.stream()
                        .filter(student -> getInfo.apply(student).equals(info))
                        .toList()
        );
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentInfo(
                students,
                name,
                Student::getFirstName
        );
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentInfo(
                students,
                name,
                Student::getLastName
        );
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudentInfo(
                students,
                group,
                Student::getGroup
        );
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByGroup(students, group).stream()
                .collect(Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)
                ));
    }
}
