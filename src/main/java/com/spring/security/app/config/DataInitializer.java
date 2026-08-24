package com.spring.security.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.security.app.entity.Usuario;
import com.spring.security.app.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    // CommandLineRunner, su función es ejecutar código una vez cuando la aplicación arranca.
    @Bean
    public CommandLineRunner crearUsuario(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (usuarioRepository.findByUsername("ivan").isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setUsername("ivan");
                usuario.setPassword(
                    passwordEncoder.encode("123456")
                );
                usuario.setRole("USER");

                usuarioRepository.save(usuario);
            }
        };
    }
}
