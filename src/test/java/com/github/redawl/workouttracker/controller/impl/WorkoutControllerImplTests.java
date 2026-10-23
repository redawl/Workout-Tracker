package com.github.redawl.workouttracker.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.security.JwtAuthFilter;
import com.github.redawl.workouttracker.service.WorkoutService;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setupTests(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        MockitoAnnotations.openMocks(this);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(null, Instancio.create(String.class), null);

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Test
    void getWorkoutByDateShouldReturn404ForBadDate() throws Exception {
        LocalDate date = Instancio.create(LocalDate.class);
        when(workoutService.getWorkoutByDate(date, getUserJwt()))
                .thenThrow(NotFoundException.class);


        mockMvc.perform(get("/api/workout")
                        .param("date", date.toString())
                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void getWorkoutByDateShouldReturnWorkoutIfExists() throws Exception {
        LocalDate date = Instancio.create(LocalDate.class);
        Workout workout = Instancio.create(Workout.class);
        String workoutString = toJsonString(workout);
        when(workoutService.getWorkoutByDate(date, getUserJwt())).thenReturn(workout);

        mockMvc.perform(get("/api/workout")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(workoutString));
    }

    @Test
    void removeWorkoutByDateShouldReturn404IfNotExists() throws Exception {
        LocalDate date = Instancio.create(LocalDate.class);
        doThrow(NotFoundException.class).when(workoutService).removeWorkoutByDate(date, getUserJwt());

        mockMvc.perform(delete("/api/workout")
                .with(csrf())
                .param("date", date.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeWorkoutByDateShouldReturn204IfExists() throws Exception {
        LocalDate date = Instancio.create(LocalDate.class);

        mockMvc.perform(delete("/api/workout")
                .with(csrf())
                .param("date", date.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void addWorkoutShouldReturn400IfAlreadyExists() throws Exception {
        Workout workout = Instancio.create(Workout.class);
        Mockito.doThrow(ExistsException.class).when(workoutService).addWorkout(workout, getUserJwt());

        mockMvc.perform(post("/api/workout")
                        .with(csrf())
                .content(toJsonString(workout))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addWorkoutShouldReturn201IfNotExists() throws Exception {
        Workout workout = Instancio.create(Workout.class);

        mockMvc.perform(post("/api/workout")
                .with(csrf())
                .content(toJsonString(workout))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void updateWorkoutShouldReturn404IfNotExists() throws Exception {
        Workout workout = Instancio.create(Workout.class);
        Mockito.doThrow(NotFoundException.class).when(workoutService).updateWorkout(workout, getUserJwt());

        mockMvc.perform(put("/api/workout")
                .with(csrf())
                .content(toJsonString(workout))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWorkoutShouldReturnOkIfExists() throws Exception {
        Workout workout = Instancio.create(Workout.class);

        mockMvc.perform(put("/api/workout")
                .with(csrf())
                .content(toJsonString(workout))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllWorkoutsShouldAlwaysReturnOk() throws Exception {
        mockMvc.perform(get("/api/workout/all"))
                .andExpect(status().isOk());
    }

    String getUserJwt(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    String toJsonString(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex){
            return null;
        }
    }
}
