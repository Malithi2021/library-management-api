package com.example.backend.borrow;

import com.example.backend.book.Book;
import com.example.backend.book.BookRepository;
import com.example.backend.student.Student;
import com.example.backend.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class BorrowConfig {

    @Bean
    @Order(2) // Ensure this runs after books and students are created
    CommandLineRunner borrowCommandLineRunner(
            BorrowRepository borrowRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository) {

        return args -> {
            // Get students
            List<Student> students = studentRepository.findAll();
            if (students.size() < 2) {
                System.out.println("Not enough students to create borrow records");
                return; // Not enough students to create test data
            }

            // Get books
            List<Book> books = bookRepository.findAll();
            if (books.size() < 4) {
                System.out.println("Not enough books to create borrow records");
                return; // Not enough books to create test data
            }

            // Create current borrow records
            BorrowRecord currentBorrow1 = new BorrowRecord();
            currentBorrow1.setStudent(students.get(0));
            currentBorrow1.setBook(books.get(0));
            currentBorrow1.setBorrowDate(LocalDate.now().minusDays(5));
            currentBorrow1.setDueDate(LocalDate.now().plusDays(9)); // 14 days from borrow date
            currentBorrow1.setStatus(BorrowStatus.BORROWED);

            BorrowRecord currentBorrow2 = new BorrowRecord();
            currentBorrow2.setStudent(students.get(1));
            currentBorrow2.setBook(books.get(1));
            currentBorrow2.setBorrowDate(LocalDate.now().minusDays(10));
            currentBorrow2.setDueDate(LocalDate.now().plusDays(4)); // 14 days from borrow date
            currentBorrow2.setStatus(BorrowStatus.BORROWED);

            // Create overdue borrow record
            BorrowRecord overdueBorrow = new BorrowRecord();
            overdueBorrow.setStudent(students.get(0));
            overdueBorrow.setBook(books.get(2));
            overdueBorrow.setBorrowDate(LocalDate.now().minusDays(20));
            overdueBorrow.setDueDate(LocalDate.now().minusDays(6)); // 14 days from borrow date
            overdueBorrow.setReturnDate(LocalDate.now());
            overdueBorrow.setStatus(BorrowStatus.OVERDUE);
            overdueBorrow.setFineAmount(6.0); // $1 per day overdue
            overdueBorrow.setFinePaid(false);

            // Create returned borrow record
            BorrowRecord returnedBorrow = new BorrowRecord();
            returnedBorrow.setStudent(students.get(1));
            returnedBorrow.setBook(books.get(3));
            returnedBorrow.setBorrowDate(LocalDate.now().minusDays(15));
            returnedBorrow.setDueDate(LocalDate.now().minusDays(1)); // 14 days from borrow date
            returnedBorrow.setReturnDate(LocalDate.now().minusDays(3));
            returnedBorrow.setStatus(BorrowStatus.RETURNED);

            // Save borrow records
            borrowRepository.saveAll(
                    List.of(currentBorrow1, currentBorrow2, overdueBorrow, returnedBorrow)
            );

            // Update book available copies for borrowed books
            updateBookAvailableCopies(books.get(0));
            updateBookAvailableCopies(books.get(1));
            updateBookAvailableCopies(books.get(2));

            bookRepository.saveAll(List.of(books.get(0), books.get(1), books.get(2)));
        };
    }

    private void updateBookAvailableCopies(Book book) {
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
    }
}