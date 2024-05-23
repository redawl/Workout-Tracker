package com.github.redawl.workouttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WorkoutTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutTrackerApplication.class, args);
	}


	@Bean
	@SuppressWarnings("unused")
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
