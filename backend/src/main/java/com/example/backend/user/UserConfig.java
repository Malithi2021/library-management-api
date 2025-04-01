package com.example.backend.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    @Order(0) // Run at the same time as student and category configs
    CommandLineRunner userCommandLineRunner(
            UserRepository repository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {

        return args -> {
            // Create admin user
            User admin = new User(
                    "Admin User",
                    "admin",
                    bCryptPasswordEncoder.encode("admin123"),
                    UserRole.ADMIN
            );

            // Create librarian user
            User librarian = new User(
                    "Librarian User",
                    "librarian",
                    bCryptPasswordEncoder.encode("library123"),
                    UserRole.LIBRARIAN
            );

            repository.save(admin);
            repository.save(librarian);
        };
    }
}