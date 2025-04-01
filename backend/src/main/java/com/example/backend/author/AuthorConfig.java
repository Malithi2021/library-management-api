package com.example.backend.author;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class AuthorConfig {

    @Bean
    @Order(0) // Run at the same time as other initial configs
    CommandLineRunner authorCommandLineRunner(AuthorRepository repository) {
        return args -> {
            Author rowling = new Author(
                    "J.K. Rowling",
                    "Joanne Rowling, better known by her pen name J. K. Rowling, is a British author and philanthropist. She wrote Harry Potter, which has won multiple awards and sold more than 500 million copies."
            );

            Author tolkien = new Author(
                    "J.R.R. Tolkien",
                    "John Ronald Reuel Tolkien was an English writer, poet, philologist, and academic. He is best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
            );

            Author martin = new Author(
                    "George R.R. Martin",
                    "George Raymond Richard Martin is an American novelist, screenwriter, television producer and short story writer. He is the author of the series of epic fantasy novels A Song of Ice and Fire."
            );

            Author christie = new Author(
                    "Agatha Christie",
                    "Dame Agatha Mary Clarissa Christie was an English writer known for her 66 detective novels and 14 short story collections, particularly those revolving around fictional detectives Hercule Poirot and Miss Marple."
            );

            Author orwell = new Author(
                    "George Orwell",
                    "Eric Arthur Blair, known by his pen name George Orwell, was an English novelist, essayist, journalist and critic. His work is characterised by lucid prose, biting social criticism, and awareness of social injustice."
            );

            repository.saveAll(
                    List.of(rowling, tolkien, martin, christie, orwell)
            );
        };
    }
}