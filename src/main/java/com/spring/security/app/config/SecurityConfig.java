package com.spring.security.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(
                auth -> auth
                        //.anyRequest().authenticated() // Cualquier petición necesita que el usuario esté autenticado
                        //.anyRequest().permitAll() // Puede acceder cualquiera
                        .requestMatchers("/api/productos/privado").hasRole("USER")
                        .requestMatchers("/api/productos/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Utiliza HTTP Basic como mecanismo de autenticación

        return http.build();
    }

    // este bean indica a Spring Security cómo debe cifrar/verificar las contraseñas de los usuarios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
