package com.github.redawl.workouttracker.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.security.JwtAuthFilter;
import com.github.redawl.workouttracker.service.ExerciseReferenceService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ExerciseControllerImpl.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseControllerImplTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ExerciseReferenceService exerciseReferenceService;

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
    void getExerciseByNameShouldReturnNotFoundIfNotExists() throws Exception {
        String name = Instancio.create(String.class);
        when(exerciseReferenceService.getExerciseByName(name, getUserJwt())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/exercise")
                .with(csrf())
                .param("name", name))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExerciseByNameShouldReturnExerciseIfExists() throws Exception {
        String name = Instancio.create(String.class);
        Exercise exercise = Instancio.create(Exercise.class);
        when(exerciseReferenceService.getExerciseByName(name, getUserJwt())).thenReturn(exercise);

        mockMvc.perform(get("/api/exercise")
                .with(csrf())
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().json(toJsonString(exercise)));
    }

    @Test
    void getExercisesShouldAlwaysReturn200() throws Exception {
        mockMvc.perform(get("/api/exercise/all")
                .with(csrf()))
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
