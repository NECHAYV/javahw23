package org.exemple.legacy.service;


import org.exemple.legacy.exeption.NotFoundException;
import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.FacultyRepository;
import org.exemple.legacy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;


    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id) {
        logger.info("Was invoked method for get faculty with id = {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = {}", id);
                    return new NotFoundException("Факультет с id=" + id + " не найден");
                });
    }

    public Faculty update(long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with id = {}", id);
        Faculty existing = get(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        return facultyRepository.save(existing);
    }

    public void delete(long id) {
        logger.info("Was invoked method for delete faculty with id = {}", id);
        get(id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> filterByColor(String color) {
        logger.debug("Was invoked method for filter faculties by color = {}", color);
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public List<Faculty> findByNameOrColor(String search) {
        logger.info("Was invoked method for search faculties by name or color = {}", search);
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(search, search);
    }

    public List<Student> getStudentsOfFaculty(long facultyId) {
        logger.info("Was invoked method for get students of faculty with id = {}", facultyId);
        get(facultyId); // проверит существование, иначе выбросит NotFoundException
        return studentRepository.findByFacultyId(facultyId);
    }
}
