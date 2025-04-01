package com.example.backend.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository <Book, Long>{
    // Find book by ISBN
    Optional<Book> findByISBN(String isbn);

    // Find books by title (containing the search term)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books with available copies
    List<Book> findByAvailableCopiesGreaterThan(int copies);
}
