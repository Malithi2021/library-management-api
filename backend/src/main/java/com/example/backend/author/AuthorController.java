package com.example.backend.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping(path = "{authorId}")
    public Author getAuthorById(@PathVariable("authorId") Long authorId) {
        return authorService.getAuthorById(authorId);
    }

    @PostMapping
    public void addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
    }

    @PutMapping(path = "{authorId}")
    public void updateAuthor(
            @PathVariable("authorId") Long authorId,
            @RequestBody Author author) {
        authorService.updateAuthor(authorId, author);
    }

    @DeleteMapping(path = "{authorId}")
    public void deleteAuthor(@PathVariable("authorId") Long authorId) {
        authorService.deleteAuthor(authorId);
    }

    @GetMapping(path = "/search")
    public List<Author> searchAuthors(@RequestParam String name) {
        return authorService.searchAuthorsByName(name);
    }
}