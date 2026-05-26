package org.exemple.legacy;

import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class FacultyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void createFacultyTest() {

        Faculty faculty = new Faculty(1L, "Math", "Red");

        when(facultyService.create(faculty)).thenReturn(faculty);

        webTestClient.post()
                .uri("/faculty")
                .bodyValue(faculty)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Long.class)
                .isEqualTo(1L);
    }

    @Test
    void getFacultyTest() {

        Faculty faculty = new Faculty(1L, "Math", "Red");

        when(facultyService.get(1L)).thenReturn(faculty);

        webTestClient.get()
                .uri("/faculty/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Math");
    }

    @Test
    void updateFacultyTest() {

        Faculty faculty = new Faculty(1L, "Physics", "Blue");

        when(facultyService.update(1L, faculty)).thenReturn(faculty);

        webTestClient.put()
                .uri("/faculty/1")
                .bodyValue(faculty)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.color").isEqualTo("Blue");
    }

    @Test
    void filterByColorTest() {

        when(facultyService.filterByColor("Red"))
                .thenReturn(List.of(
                        new Faculty(1L, "Math", "Red")
                ));

        webTestClient.get()
                .uri("/faculty/filter/color?color=Red")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void getStudentsTest() {

        when(facultyService.getStudentsOfFaculty(1L))
                .thenReturn(List.of(
                        new Student(1L, "Alex", 20)
                ));

        webTestClient.get()
                .uri("/faculty/1/students")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void notFoundTest() {

        webTestClient.get()
                .uri("/wrong/url")
                .exchange()
                .expectStatus().isNotFound();
    }
}