package com.example.backend.borrow;

import com.example.backend.book.Book;
import com.example.backend.book.BookRepository;
import com.example.backend.student.Student;
import com.example.backend.student.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    // Constants for fine configuration
    private static final int GRACE_PERIOD_DAYS = 2; // 2 days grace period
    private static final double DAILY_FINE_RATE = 1.0; // $1 per day
    private static final double MAX_FINE_MULTIPLIER = 5.0; // Maximum fine is 5x the daily rate

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository, StudentRepository studentRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public BorrowRecord borrowBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(
                        "Book with id " + bookId + " does not exist"));

        // Check if book is available
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setStudent(student);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks loan period
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        return borrowRepository.save(borrowRecord);
    }

    @Transactional
    public void returnBook(Long borrowId) {
        BorrowRecord borrowRecord = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalStateException(
                        "Borrow record with id " + borrowId + " does not exist"));

        if (borrowRecord.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("Book already returned");
        }

        // Set return date to today
        LocalDate returnDate = LocalDate.now();
        borrowRecord.setReturnDate(returnDate);

        // Calculate fine with grace period
        LocalDate effectiveDueDate = borrowRecord.getDueDate().plusDays(GRACE_PERIOD_DAYS);

        if (returnDate.isAfter(effectiveDueDate)) {
            // Calculate days late after grace period
            long daysLate = java.time.Period.between(effectiveDueDate, returnDate).getDays();

            // Apply daily rate with maximum cap
            double calculatedFine = Math.min(
                    daysLate * DAILY_FINE_RATE,
                    DAILY_FINE_RATE * MAX_FINE_MULTIPLIER
            );

            borrowRecord.setFineAmount(calculatedFine);
            borrowRecord.setStatus(BorrowStatus.OVERDUE);
        } else {
            // Returned on time (within grace period)
            borrowRecord.setFineAmount(0.0);
            borrowRecord.setStatus(BorrowStatus.RETURNED);
        }

        borrowRepository.save(borrowRecord);

        // Update book available copies
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }

    // Method to pay fine
    @Transactional
    public void payFine(Long borrowId) {
        BorrowRecord borrowRecord = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalStateException(
                        "Borrow record with id " + borrowId + " does not exist"));

        if (borrowRecord.getFineAmount() <= 0) {
            throw new IllegalStateException("No fine to pay");
        }

        if (borrowRecord.getFinePaid()) {
            throw new IllegalStateException("Fine has already been paid");
        }

        // Mark fine as paid
        borrowRecord.setFinePaid(true);

        // Update status to RETURNED since the book is physically returned and fine is paid
        borrowRecord.setStatus(BorrowStatus.RETURNED);

        borrowRepository.save(borrowRecord);
    }

    // Get detailed fine information for a student
    public Map<String, Object> getStudentFinesDetails(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        List<BorrowRecord> allRecords = borrowRepository.findByStudent(student);

        // Unpaid fines
        List<BorrowRecord> unpaidFines = allRecords.stream()
                .filter(record -> !record.getFinePaid() && record.getFineAmount() > 0)
                .collect(Collectors.toList());

        // Calculate total unpaid amount
        double totalUnpaidAmount = unpaidFines.stream()
                .mapToDouble(BorrowRecord::getFineAmount)
                .sum();

        // Format the response
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", student.getId());
        response.put("studentName", student.getName());
        response.put("totalUnpaidFines", totalUnpaidAmount);
        response.put("unpaidFineDetails", unpaidFines);

        return response;
    }

    public List<BorrowRecord> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public List<BorrowRecord> getBooksBorrowedByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        return borrowRepository.findByStudentAndStatusNot(student, BorrowStatus.RETURNED);
    }

    public List<BorrowRecord> getOverdueBooks() {
        return borrowRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(), BorrowStatus.BORROWED);
    }

    // Method to get student borrow history
    public List<BorrowRecord> getStudentBorrowHistory(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        return borrowRepository.findByStudent(student);
    }
}