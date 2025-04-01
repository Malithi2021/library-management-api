package com.example.backend.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class StudentConfig {
    @Bean
    @Order(0) // Ensure this runs first
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student malithi = new Student(
                    "Malithi",
                    "malithirumalka@gmail.com",
                    LocalDate.of(2000, JANUARY, 5)
            );

            Student alex = new Student(
                    "Alex",
                    "alex@gmail.com",
                    LocalDate.of(2005, JANUARY, 5)
            );

            Student emma = new Student(
                    "Emma",
                    "emma@gmail.com",
                    LocalDate.of(2002, MARCH, 15)
            );

            Student michael = new Student(
                    "Michael",
                    "michael@gmail.com",
                    LocalDate.of(1998, JULY, 22)
            );

            Student sophia = new Student(
                    "Sophia",
                    "sophia@gmail.com",
                    LocalDate.of(2001, DECEMBER, 8)
            );

            repository.saveAll(
                    List.of(malithi, alex, emma, michael, sophia)
            );
        };
    }
}