package org.exemple.legacy.controller;

import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/filter/age-between")
    public List<Student> filterByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable long id) {
        return studentService.getFacultyOfStudent(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudents() {
        return ResponseEntity.ok(studentService.getTotalCount());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-a")
    public List<String> getNamesStartingWithA() {
        return studentService.getNamesStartingWithA();
    }

    @GetMapping("/average-age-all")
    public double getAverageAgeOfAllStudents() {
        return studentService.getAveragesAge();
    }


    // Эндпоинт для параллельного вывода (без синхронизации)
    @GetMapping("/students/print-parallel")
    public void printStudentsParallel() {
        List<Student> students = studentService.getAllStudents();
        if (students == null || students.size() < 6) {
            System.out.println("Недостаточно студентов (нужно минимум 6)");
            return;
        }
        // Вывод первых двух студентов в основном потоке
        System.out.println("Main thread: " + students.get(0).getName());
        System.out.println("Main thread: " + students.get(1).getName());

        // Поток для 3-го и 4-го студента
        new Thread(() -> {
            System.out.println("Thread-1: " + students.get(2).getName());
            System.out.println("Thread-1: " + students.get(3).getName());
        }).start();

        // Поток для 5-го и 6-го студента
        new Thread(() -> {
            System.out.println("Thread-2: " + students.get(4).getName());
            System.out.println("Thread-2: " + students.get(5).getName());
        }).start();
    }

    // Синхронизированный метод для вывода одного имени
    private synchronized void printName(String name, String threadName) {
        System.out.println(threadName + ": " + name);
    }

    @GetMapping("/students/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = studentService.getAllStudents();
        if (students == null || students.size() < 6) {
            System.out.println("Недостаточно студентов (нужно минимум 6)");
            return;
        }
        // Вывод первых двух студентов в основном потоке
        printName(students.get(0).getName(), "main");
        printName(students.get(1).getName(), "main");

        // Поток для 3-го и 4-го студента
        new Thread(() -> {
            printName(students.get(2).getName(), "thread-1");
            printName(students.get(3).getName(), "thread-1");
        }).start();

        // Поток для 5-го и 6-го студента
        new Thread(() -> {
            printName(students.get(4).getName(), "thread-2");
            printName(students.get(5).getName(), "thread-2");
        }).start();
    }
}