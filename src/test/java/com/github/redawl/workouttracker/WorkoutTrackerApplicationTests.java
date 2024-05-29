package com.github.redawl.workouttracker;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class WorkoutTrackerApplicationTests {

	@Test
	void contextLoads() {
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

}
