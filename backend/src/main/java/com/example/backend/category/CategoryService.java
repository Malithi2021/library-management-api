package com.example.backend.category;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Category with id " + id + " does not exist"));
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long categoryId, Category categoryDetails) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException(
                        "Category with id " + categoryId + " does not exist"));

        if (categoryDetails.getName() != null &&
                categoryDetails.getName().length() > 0 &&
                !Objects.equals(category.getName(), categoryDetails.getName())) {
            category.setName(categoryDetails.getName());
        }

        if (categoryDetails.getDescription() != null &&
                categoryDetails.getDescription().length() > 0 &&
                !Objects.equals(category.getDescription(), categoryDetails.getDescription())) {
            category.setDescription(categoryDetails.getDescription());
        }
    }

    public void deleteCategory(Long categoryId) {
        boolean exists = categoryRepository.existsById(categoryId);
        if (!exists) {
            throw new IllegalStateException(
                    "Category with id " + categoryId + " does not exist");
        }
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}