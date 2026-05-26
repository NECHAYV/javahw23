package org.exemple.legacy;

import org.exemple.legacy.controller.StudentController;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void getStudentTest() throws Exception {

        Student student = new Student(1L, "Alex", 20);

        when(studentService.get(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"));
    }

    @Test
    void createStudentTest() throws Exception {

        Student student = new Student(1L, "Alex", 20);

        when(studentService.create(org.mockito.ArgumentMatchers.any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Alex",
                                    "age":20
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void filterByAgeTest() throws Exception {

        when(studentService.filterByAge(20))
                .thenReturn(List.of(
                        new Student(1L, "Alex", 20)
                ));

        mockMvc.perform(get("/student/filter?age=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alex"));
    }

    @Test
    void ageBetweenTest() throws Exception {

        when(studentService.findByAgeBetween(18, 25))
                .thenReturn(List.of(
                        new Student(1L, "Alex", 20)
                ));

        mockMvc.perform(get("/student/filter/age-between?min=18&max=25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    void notFoundTest() throws Exception {

        mockMvc.perform(get("/wrong/url"))
                .andExpect(status().isNotFound());
    }
}
