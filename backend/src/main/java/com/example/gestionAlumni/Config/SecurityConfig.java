package com.example.gestionAlumni.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()) // Désactive CSRF pour faciliter les tests via Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/alumni/**").permitAll() 
                .requestMatchers("/api/students/**").permitAll() 
                .requestMatchers("/api/admin/**").permitAll() 
                .requestMatchers("/api/offers/**").permitAll()
                .requestMatchers("/api/messages/**").permitAll()
                .requestMatchers("/api/recruiters/**").permitAll()
                .requestMatchers("/api/matching/**").permitAll()
                .requestMatchers("/api/mentorship/**").permitAll()
                .requestMatchers("/api/experience/**").permitAll()
                .requestMatchers("/api/events/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/seed/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
