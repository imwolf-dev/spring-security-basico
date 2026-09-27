package com.spring.security.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain oauth2ClientSecurityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .securityMatcher(
                "/",
                "/oauth2/authorization/**",
                "/login/**",
                "/oauth2/token-info" // ruta del controller para ver el JWT
            )
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // permite cualquier solicitud de la url definida arriba
            )
            .formLogin(Customizer.withDefaults()) // activa el login para autenticacion
            .oauth2Client(Customizer.withDefaults()) // activa el soporte de OAuth2 Client de Spring Security
            //.oauth2Login(Customizer.withDefaults()); // agrega específicamente el mecanismo de login mediante OAuth2
            ;
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                //.anyRequest().authenticated() // Cualquier petición necesita que el usuario esté autenticado
                //.anyRequest().permitAll() // Puede acceder cualquiera
                //.requestMatchers("/api/productos/privado").hasRole("USER")
                .requestMatchers("/api/productos/privado")
                    .hasAuthority("SCOPE_read")
                .requestMatchers("/api/productos/admin")
                    .hasRole("ADMIN")
                .anyRequest()
                    .authenticated()
            )
            //.httpBasic(httpBasic -> {}); // Utiliza HTTP Basic como mecanismo de autenticación
            .oauth2ResourceServer( // autenticacion mediante JWT
                oauth2 -> oauth2.jwt(jwt -> {})
            );

        return http.build();
    }

    // este bean indica a Spring Security cómo debe cifrar/verificar las contraseñas de los usuarios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
