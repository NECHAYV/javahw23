package org.exemple.legacy;

import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudentTest() {

        Student student = new Student(1L, "Alex", 20);

        when(studentService.create(student)).thenReturn(student);

        webTestClient.post()
                .uri("/student")
                .bodyValue(student)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Long.class)
                .isEqualTo(1L);
    }

    @Test
    void getStudentTest() {

        Student student = new Student(1L, "Alex", 20);

        when(studentService.get(1L)).thenReturn(student);

        webTestClient.get()
                .uri("/student/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Alex");
    }

    @Test
    void updateStudentTest() {

        Student student = new Student(1L, "Alex", 21);

        when(studentService.update(1L, student)).thenReturn(student);

        webTestClient.put()
                .uri("/student/1")
                .bodyValue(student)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.age").isEqualTo(21);
    }

    @Test
    void filterByAgeTest() {

        when(studentService.filterByAge(20))
                .thenReturn(List.of(
                        new Student(1L, "Alex", 20)
                ));

        webTestClient.get()
                .uri("/student/filter?age=20")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void getFacultyTest() {

        Faculty faculty = new Faculty(1L, "Math", "Red");

        when(studentService.getFacultyOfStudent(1L))
                .thenReturn(faculty);

        webTestClient.get()
                .uri("/student/1/faculty")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Math");
    }

    @Test
    void notFoundTest() {

        webTestClient.get()
                .uri("/wrong/url")
                .exchange()
                .expectStatus().isNotFound();
    }
}