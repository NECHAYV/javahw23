package org.exemple.legacy.service;


import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.FacultyRepository;
import org.exemple.legacy.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findByNameOrColor(String search) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(search, search);
    }

    public List<Student> getStudentsOfFaculty(long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }

    public Faculty get(long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Факультет с id=" + id + " не найден"));
    }

    public Faculty update(long id, Faculty faculty) {
        Faculty existing = get(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        return facultyRepository.save(existing);
    }

    public void delete(long id) {
        get(id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> filterByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }
}
