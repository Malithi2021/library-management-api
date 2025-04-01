package com.example.backend.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Find categories by name containing the search term (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);
}