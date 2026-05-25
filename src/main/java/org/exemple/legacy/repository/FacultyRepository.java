package org.exemple.legacy.repository;

import org.exemple.legacy.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCase(String color);
}