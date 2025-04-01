package com.example.backend.book;

import com.example.backend.author.Author;
import com.example.backend.author.AuthorRepository;
import com.example.backend.category.Category;
import com.example.backend.category.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class BookConfig {

    @Bean
    @Order(1) // Ensure this runs after authors and categories are created
    @Transactional
    CommandLineRunner bookCommandLineRunner(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            CategoryRepository categoryRepository) {

        return args -> {
            // Get authors
            List<Author> rowlingList = authorRepository.findByNameContainingIgnoreCase("Rowling");
            List<Author> tolkienList = authorRepository.findByNameContainingIgnoreCase("Tolkien");
            List<Author> martinList = authorRepository.findByNameContainingIgnoreCase("Martin");
            List<Author> christieList = authorRepository.findByNameContainingIgnoreCase("Christie");
            List<Author> orwellList = authorRepository.findByNameContainingIgnoreCase("Orwell");

            // Get categories
            List<Category> fictionList = categoryRepository.findByNameContainingIgnoreCase("Fiction");
            List<Category> nonFictionList = categoryRepository.findByNameContainingIgnoreCase("Non-Fiction");
            List<Category> scienceFictionList = categoryRepository.findByNameContainingIgnoreCase("Science Fiction");
            List<Category> mysteryList = categoryRepository.findByNameContainingIgnoreCase("Mystery");

            // Create books
            Book harryPotter = new Book(
                    "Harry Potter and the Philosopher's Stone",
                    "9780747532743",
                    LocalDate.of(1997, 6, 26),
                    10,
                    8
            );

            Book lordOfRings = new Book(
                    "The Lord of the Rings",
                    "9780618640157",
                    LocalDate.of(1954, 7, 29),
                    7,
                    5
            );

            Book gameOfThrones = new Book(
                    "A Game of Thrones",
                    "9780553103540",
                    LocalDate.of(1996, 8, 1),
                    5,
                    3
            );

            Book murderOnOrient = new Book(
                    "Murder on the Orient Express",
                    "9780062693662",
                    LocalDate.of(1934, 1, 1),
                    8,
                    6
            );

            Book nineteenEightyFour = new Book(
                    "1984",
                    "9780451524935",
                    LocalDate.of(1949, 6, 8),
                    12,
                    10
            );

            // Add authors to books
            if (!rowlingList.isEmpty()) {
                Author author = authorRepository.save(rowlingList.get(0)); // Reattach to session
                Set<Author> authors = new HashSet<>();
                authors.add(author);
                harryPotter.setAuthors(authors);
            }

            if (!tolkienList.isEmpty()) {
                Author author = authorRepository.save(tolkienList.get(0)); // Reattach to session
                Set<Author> authors = new HashSet<>();
                authors.add(author);
                lordOfRings.setAuthors(authors);
            }

            if (!martinList.isEmpty()) {
                Author author = authorRepository.save(martinList.get(0)); // Reattach to session
                Set<Author> authors = new HashSet<>();
                authors.add(author);
                gameOfThrones.setAuthors(authors);
            }

            if (!christieList.isEmpty()) {
                Author author = authorRepository.save(christieList.get(0)); // Reattach to session
                Set<Author> authors = new HashSet<>();
                authors.add(author);
                murderOnOrient.setAuthors(authors);
            }

            if (!orwellList.isEmpty()) {
                Author author = authorRepository.save(orwellList.get(0)); // Reattach to session
                Set<Author> authors = new HashSet<>();
                authors.add(author);
                nineteenEightyFour.setAuthors(authors);
            }

            // Save books first before adding categories
            harryPotter = bookRepository.save(harryPotter);
            lordOfRings = bookRepository.save(lordOfRings);
            gameOfThrones = bookRepository.save(gameOfThrones);
            murderOnOrient = bookRepository.save(murderOnOrient);
            nineteenEightyFour = bookRepository.save(nineteenEightyFour);

            // Add categories to books (safer way without bidirectional relationships)
            if (!fictionList.isEmpty()) {
                Category fiction = categoryRepository.save(fictionList.get(0)); // Reattach to session
                harryPotter.getCategories().add(fiction);
                lordOfRings.getCategories().add(fiction);
                gameOfThrones.getCategories().add(fiction);
                murderOnOrient.getCategories().add(fiction);
                nineteenEightyFour.getCategories().add(fiction);
            }

            if (!scienceFictionList.isEmpty()) {
                Category scienceFiction = categoryRepository.save(scienceFictionList.get(0)); // Reattach to session
                nineteenEightyFour.getCategories().add(scienceFiction);
            }

            if (!mysteryList.isEmpty()) {
                Category mystery = categoryRepository.save(mysteryList.get(0)); // Reattach to session
                murderOnOrient.getCategories().add(mystery);
            }

            // Save books with their categories
            bookRepository.save(harryPotter);
            bookRepository.save(lordOfRings);
            bookRepository.save(gameOfThrones);
            bookRepository.save(murderOnOrient);
            bookRepository.save(nineteenEightyFour);
        };
    }
}