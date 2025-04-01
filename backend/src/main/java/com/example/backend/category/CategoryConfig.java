package com.example.backend.category;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class CategoryConfig {

    @Bean
    @Order(0) // Run at the same time as student config
    CommandLineRunner categoryCommandLineRunner(CategoryRepository repository) {
        return args -> {
            Category fiction = new Category(
                    "Fiction",
                    "Literary works created from the imagination, not based on fact"
            );

            Category nonFiction = new Category(
                    "Non-Fiction",
                    "Literary works based on facts, real events, and real people"
            );

            Category scienceFiction = new Category(
                    "Science Fiction",
                    "Speculative fiction dealing with advanced science and technology"
            );

            Category mystery = new Category(
                    "Mystery",
                    "Fiction dealing with the solution of a crime or the unraveling of secrets"
            );

            Category biography = new Category(
                    "Biography",
                    "Non-fiction account of a person's life written by someone else"
            );

            Category history = new Category(
                    "History",
                    "Study of past events, particularly in human affairs"
            );

            repository.saveAll(
                    List.of(fiction, nonFiction, scienceFiction, mystery, biography, history)
            );
        };
    }
}