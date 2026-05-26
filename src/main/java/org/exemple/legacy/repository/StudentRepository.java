package org.exemple.legacy.repository;


import org.exemple.legacy.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);
    List<Student> findByFacultyId(Long facultyId);

    // Количество всех студентов
    @Query("SELECT COUNT(s) FROM Student s")
    Long getTotalCount();

    // Средний возраст студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    // Пять последних студентов (с максимальными id)
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findLastFive(org.springframework.data.domain.Pageable pageable);
}
