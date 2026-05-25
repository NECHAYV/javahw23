package org.exemple.legacy.service;

import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student get(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Студент с id=" + id + " не найден"));
    }

    public Student update(long id, Student student) {
        Student existing = get(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        return studentRepository.save(existing);
    }

    public void delete(long id) {
        get(id); // проверим существование
        studentRepository.deleteById(id);
    }

    public List<Student> filterByAge(int age) {
        return studentRepository.findByAge(age);
    }
}