package com.github.redawl.workouttracker.controller.impl;

import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.security.JwtAuthFilter;
import com.github.redawl.workouttracker.service.WorkoutService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;


@WebMvcTest(value = WorkoutControllerImpl.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutControllerImplTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WorkoutService workoutService;

    @MockBean
    Authentication authentication;

    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    void setupTests(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled
    void getWorkoutByDateShouldReturn404ForBadDate() throws Exception {
        LocalDate date = Instancio.create(LocalDate.class);
        String userJwt = Instancio.create(String.class);
        when(workoutService.getWorkoutByDate(date, userJwt))
                .thenThrow(NotFoundException.class);
        when(authentication.getCredentials()).thenReturn(userJwt);


        mockMvc.perform(get("/api/workout")
                .requestAttr("date", date.toString())
                )
                .andExpect(
                        status().isNotFound()
                );
    }
}
