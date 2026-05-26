package org.exemple.legacy;

import org.exemple.legacy.controller.FacultyController;
import org.exemple.legacy.model.Faculty;
import org.exemple.legacy.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void getFacultyTest() throws Exception {

        Faculty faculty = new Faculty(1L, "Math", "Red");

        when(facultyService.get(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Math"));
    }

    @Test
    void filterByColorTest() throws Exception {

        when(facultyService.filterByColor("Red"))
                .thenReturn(List.of(
                        new Faculty(1L, "Math", "Red")
                ));

        mockMvc.perform(get("/faculty/filter/color?color=Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void searchTest() throws Exception {

        when(facultyService.findByNameOrColor("Math"))
                .thenReturn(List.of(
                        new Faculty(1L, "Math", "Red")
                ));

        mockMvc.perform(get("/faculty/filter/search?search=Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Math"));
    }

    @Test
    void notFoundTest() throws Exception {

        mockMvc.perform(get("/wrong/url"))
                .andExpect(status().isNotFound());
    }
}