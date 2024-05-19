package com.github.redawl.workouttracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value("${swagger.name}")
    private String name;
    @Value("${swagger.email}")
    private String email;
    @Value("${swagger.site}")
    private String site;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Workout Tracker API")
                        .description("Workout Tracker App API")
                        .version("v1")
                        .contact(new Contact().name(name).email(email).url(site))
                );
    }
}
