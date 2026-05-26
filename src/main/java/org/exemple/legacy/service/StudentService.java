package org.exemple.legacy.service;

import org.exemple.legacy.exeption.NotFoundException;
import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.StudentRepository;
import org.springframework.data.domain.PageRequest;
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
                .orElseThrow(() -> new NotFoundException("Студент с id=" + id + " не найден"));
    }

    public Student update(long id, Student student) {
        Student existing = get(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        if (student.getFaculty() != null) {
            existing.setFaculty(student.getFaculty());
        }
        return studentRepository.save(existing);
    }

    public void delete(long id) {
        get(id);
        studentRepository.deleteById(id);
    }

    public List<Student> filterByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyOfStudent(long studentId) {
        Student student = get(studentId);
        return student.getFaculty();
    }

    public Long getTotalCount() {
        return studentRepository.getTotalCount();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFive(PageRequest.of(0, 5));
    }
}