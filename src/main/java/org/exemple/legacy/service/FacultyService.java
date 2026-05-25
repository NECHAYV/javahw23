package org.exemple.legacy.service;


import org.exemple.legacy.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty create(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty get(long id) {
        return Optional.ofNullable(faculties.get(id))
                .orElseThrow(() -> new NoSuchElementException("Факультет с id=" + id + " не найден"));
    }

    public Faculty update(long id, Faculty faculty) {
        Faculty existing = get(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        return existing;
    }

    public void delete(long id) {
        if (faculties.remove(id) == null) {
            throw new NoSuchElementException("Факультет с id=" + id + " не найден");
        }
    }

    public List<Faculty> filterByColor(String color) {
        return faculties.values().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
