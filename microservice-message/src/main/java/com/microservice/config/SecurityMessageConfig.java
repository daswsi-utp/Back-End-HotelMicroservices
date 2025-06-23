package com.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityMessageConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").authenticated() // solo admin necesita login
                .anyRequest().permitAll() // todo lo demás es público
            )
            .formLogin(form -> form // reemplaza httpBasic con login basado en formulario
                .loginPage("/login") // frontend o backend
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}
