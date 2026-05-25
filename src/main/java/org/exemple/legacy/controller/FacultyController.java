package org.exemple.legacy.controller;


import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty).getId();
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable long id) {
        return facultyService.get(id);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        facultyService.delete(id);
    }

    @GetMapping("/filter/color")
    public List<Faculty> filterByColor(@RequestParam String color) {
        return facultyService.filterByColor(color);
    }

    @GetMapping("/filter/search")
    public List<Faculty> filterByNameOrColor(@RequestParam String search) {
        return facultyService.findByNameOrColor(search);
    }

    @GetMapping("/{id}/students")
    public List<Student> getFacultyStudents(@PathVariable long id) {
        return facultyService.getStudentsOfFaculty(id);
    }
}