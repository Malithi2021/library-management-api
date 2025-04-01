package com.example.backend.book;

import com.example.backend.category.Category;
import com.example.backend.category.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById (Long id){
        return bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Book with id" + id + "does not exist"));
    }

    public void addBook(Book book){
        Optional<Book> bookByISBN = bookRepository.findByISBN(book.getISBN());
        if (bookByISBN.isPresent()){
            throw new IllegalStateException("Book with this ISBN already exists");
        }
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(Long bookId, Book bookDetails){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(
                        "Book with id " + bookId + " does not exist"));

        if (bookDetails.getTitle() !=null &&
                bookDetails.getTitle().length()>0 &&
                !Objects.equals(book.getTitle(),bookDetails.getTitle())){
            book.setTitle(bookDetails.getTitle());
        }

        if (bookDetails.getISBN() != null &&
                bookDetails.getISBN().length() > 0 &&
                !Objects.equals(book.getISBN(), bookDetails.getISBN())) {

            Optional<Book> bookWithISBN = bookRepository.findByISBN(bookDetails.getISBN());
            if (bookWithISBN.isPresent() && !bookWithISBN.get().getId().equals(bookId)) {
                throw new IllegalStateException("ISBN already taken by another book");
            }

            book.setISBN(bookDetails.getISBN());
        }

        if (bookDetails.getPublicationYear() != null) {
            book.setPublicationYear(bookDetails.getPublicationYear());
        }

        if (bookDetails.getTotalCopies() > 0) {
            if (bookDetails.getTotalCopies() < book.getAvailableCopies()) {
                throw new IllegalStateException(
                        "Total copies cannot be less than available copies");
            }
            book.setTotalCopies(bookDetails.getTotalCopies());
        }

        if (bookDetails.getAvailableCopies() >= 0) {
            if (bookDetails.getAvailableCopies() > book.getTotalCopies()) {
                throw new IllegalStateException(
                        "Available copies cannot be more than total copies");
            }
            book.setAvailableCopies(bookDetails.getAvailableCopies());
        }
        if (bookDetails.getAuthors() != null && !bookDetails.getAuthors().isEmpty()) {
            book.setAuthors(bookDetails.getAuthors());
        }

        if (bookDetails.getCategories() != null && !bookDetails.getCategories().isEmpty()) {
            book.setCategories(bookDetails.getCategories());
        }
    }

    public void deleteBook(Long bookId) {
        boolean exists = bookRepository.existsById(bookId);
        if (!exists) {
            throw new IllegalStateException(
                    "Book with id " + bookId + " does not exist");
        }
        bookRepository.deleteById(bookId);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    @Transactional
    public void addCategoryToBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(
                        "Book with id " + bookId + " does not exist"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException(
                        "Category with id " + categoryId + " does not exist"));

        book.addCategory(category);
    }

    @Transactional
    public void removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(
                        "Book with id " + bookId + " does not exist"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException(
                        "Category with id " + categoryId + " does not exist"));

        book.removeCategory(category);
    }

    public Set<Category> getBookCategories(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(
                        "Book with id " + bookId + " does not exist"));

        return book.getCategories();
    }
}