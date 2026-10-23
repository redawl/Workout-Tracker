package com.github.redawl.workouttracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("unused")
public class OpenApiConfig {
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.name}")
    private String name;
    @Value("${swagger.email}")
    private String email;
    @Value("${swagger.site}")
    private String site;
    @Value("${swagger.url}")
    private String url;
    @Value("${swagger.url.description}")
    private String urlDescription;


    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                    .name(name)
                                    .email(email)
                                    .url(site)
                        )
                )
                .addServersItem(
                        new Server()
                            .url(url)
                            .description(urlDescription)
                );
    }
}
