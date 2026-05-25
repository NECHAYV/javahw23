package org.exemple.legacy.service;

import org.exemple.legacy.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    public Student create(Student student) {
        student.setId(++lastId);
        students.put(student.getId(), student);
        return student;
    }

    public Student get(long id) {
        return Optional.ofNullable(students.get(id))
                .orElseThrow(() -> new NoSuchElementException("Студент с id=" + id + " не найден"));
    }

    public Student update(long id, Student student) {
        Student existing = get(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        return existing;
    }

    public void delete(long id) {
        if (students.remove(id) == null) {
            throw new NoSuchElementException("Студент с id=" + id + " не найден");
        }
    }

    public List<Student> filterByAge(int age) {
        return students.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }
}