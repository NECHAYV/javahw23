package org.exemple.legacy.service;

import org.exemple.legacy.exeption.NotFoundException;
import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student get(long id) {
        logger.info("Was invoked method for get student with id = {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = {}", id);
                    return new NotFoundException("Студент с id=" + id + " не найден");
                });
    }

    public Student update(long id, Student student) {
        logger.info("Was invoked method for update student with id = {}", id);
        Student existing = get(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        if (student.getFaculty() != null) {
            existing.setFaculty(student.getFaculty());
        }
        return studentRepository.save(existing);
    }

    public void delete(long id) {
        logger.info("Was invoked method for delete student with id = {}", id);
        get(id); // Проверит существование, выбросит 404 если нет
        studentRepository.deleteById(id);
    }

    public List<Student> filterByAge(int age) {
        logger.debug("Was invoked method for filter students by age = {}", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.debug("Was invoked method for filter students by age between {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyOfStudent(long studentId) {
        logger.info("Was invoked method for get faculty of student with id = {}", studentId);
        Student student = get(studentId);
        return student.getFaculty();
    }

    // Методы с @Query
    public Long getTotalCount() {
        logger.info("Was invoked method for get total count of students");
        return studentRepository.getTotalCount();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFive(PageRequest.of(0, 5));
    }
}