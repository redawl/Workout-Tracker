package com.github.redawl.workouttracker.config;

import com.github.redawl.workouttracker.security.JwtAuthFilter;
import com.github.redawl.workouttracker.security.JwtAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

@Configuration
public class SuperTokensSecurityConfig {
    JwtAuthFilter jwtAuthFilter;

    SuperTokensSecurityConfig(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Given: HttpSecurity configured
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**")
                        .authenticated()
                        .requestMatchers("/**").permitAll()
                        )
                .addFilterBefore(jwtAuthFilter, RequestCacheAwareFilter.class);

        // When: Accessing specific URLs
        // Then: Access is granted based on defined rules
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new JwtAuthenticationManager();
    }
}
