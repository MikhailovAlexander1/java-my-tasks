package info.kgeorgiy.ja.mikhailov.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class StudentDB implements StudentQuery, GroupQuery {
    private static final Comparator<Student> STUDENT_COMPARATOR =
            Comparator.comparing(Student::getLastName, Comparator.reverseOrder())
            .thenComparing(Student::getFirstName, Comparator.reverseOrder())
            .thenComparing(Student::getId);

    private <R> List<R> getListByFunction(List<Student> students, Function<Student, R> function) {
        return students.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    private List<Student> getSortedStudentsByComparator(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private Map<GroupName, List<Student>> getMapGroupNameToStudents(Collection<Student> collection) {
        return collection.stream()
                .collect(groupingBy(Student::getGroup,
                        Collectors.toList()));
    }

    private List<Group> getGroupList(Collection<Student> collection, Comparator<Student> comparatorForStudentsInGroups) {
        return getMapGroupNameToStudents(collection)
                .entrySet().stream()
                .map(item -> new Group(item.getKey(),
                        item.getValue().stream().sorted(comparatorForStudentsInGroups)
                                .collect(toList())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(toList());
    }

    private List<Student> findStudentsByPredicate(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream()
                .filter(predicate)
                .sorted(STUDENT_COMPARATOR)
                .collect(Collectors.toList());
    }

    private Predicate<Student> distinctByKey(Function<Student, String> function) {
        Set<Object> uniqueElements = new HashSet<>();
        return t -> uniqueElements.add(function.apply(t));
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getListByFunction(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getListByFunction(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getListByFunction(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getListByFunction(students, student -> student.getFirstName() + " " + student.getLastName());
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
                        .max(Comparator.comparing(Student::getId))
                        .map(Student::getFirstName)
                        .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return getSortedStudentsByComparator(students, Comparator.comparing(Student::getId));
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return getSortedStudentsByComparator(students, STUDENT_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsByPredicate(students, student -> student.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsByPredicate(students, student -> student.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByPredicate(students, student -> student.getGroup().equals(group));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students.stream()
                .filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)));
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> collection) {
        return getGroupList(collection, STUDENT_COMPARATOR);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> collection) {
        return getGroupList(collection, Comparator.comparing(Student::getId));
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> collection) {
        return getMapGroupNameToStudents(collection).entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                        .max(Comparator.comparing(item -> item.getValue().size()))
                        .map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> collection) {
        return getMapGroupNameToStudents(collection).entrySet().stream()
                        .map(item -> new Group(item.getKey(),
                                        item.getValue().stream().filter(distinctByKey(Student::getFirstName))
                                        .collect(toList())))
                        .sorted(Comparator.comparing(Group::getName))
                        .max(Comparator.comparing(item -> item.getStudents().size()))
                        .map(Group::getName).orElse(null);
    }
}
