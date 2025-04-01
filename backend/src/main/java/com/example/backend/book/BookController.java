package com.example.backend.book;

import com.example.backend.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(path = "{bookId}")
    public Book getBookById(@PathVariable("bookId") Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping
    public void addNewBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @PutMapping(path = "{bookId}")
    public void updateBook(
            @PathVariable("bookId") Long bookId,
            @RequestBody Book book) {
        bookService.updateBook(bookId, book);
    }

    @DeleteMapping(path = "{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping(path = "/search")
    public List<Book> searchBooks(@RequestParam String title) {
        return bookService.searchBooksByTitle(title);
    }

    @GetMapping(path = "/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    @GetMapping(path = "{bookId}/categories")
    public Set<Category> getBookCategories(@PathVariable("bookId") Long bookId) {
        return bookService.getBookCategories(bookId);
    }

    @PostMapping(path = "{bookId}/categories/{categoryId}")
    public void addCategoryToBook(
            @PathVariable("bookId") Long bookId,
            @PathVariable("categoryId") Long categoryId) {
        bookService.addCategoryToBook(bookId, categoryId);
    }

    @DeleteMapping(path = "{bookId}/categories/{categoryId}")
    public void removeCategoryFromBook(
            @PathVariable("bookId") Long bookId,
            @PathVariable("categoryId") Long categoryId) {
        bookService.removeCategoryFromBook(bookId, categoryId);
    }
}
