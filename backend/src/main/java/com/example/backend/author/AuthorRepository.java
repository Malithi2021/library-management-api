package com.example.backend.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long>{
    // Find authors by name
    List<Author> findByNameContainingIgnoreCase(String name);
}
