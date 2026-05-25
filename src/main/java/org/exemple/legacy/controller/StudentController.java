package org.exemple.legacy.controller;

import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Student student) {
        return studentService.create(student).getId();
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable long id) {
        return studentService.get(id);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }

    @GetMapping("/filter")
    public List<Student> filterByAge(@RequestParam int age) {
        return studentService.filterByAge(age);
    }
}