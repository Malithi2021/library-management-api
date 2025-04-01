package com.example.backend.borrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/borrows")
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<BorrowRecord> getAllBorrows() {
        return borrowService.getAllBorrows();
    }

    @GetMapping(path = "/student/{studentId}")
    public List<BorrowRecord> getStudentBorrows(@PathVariable("studentId") Long studentId) {
        return borrowService.getBooksBorrowedByStudent(studentId);
    }

    @GetMapping(path = "/overdue")
    public List<BorrowRecord> getOverdueBooks() {
        return borrowService.getOverdueBooks();
    }

    @PostMapping(path = "/borrow")
    public BorrowRecord borrowBook(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long bookId = request.get("bookId");

        if (studentId == null || bookId == null) {
            throw new IllegalArgumentException("Both studentId and bookId are required");
        }

        return borrowService.borrowBook(studentId, bookId);
    }

    @PutMapping(path = "/return/{borrowId}")
    public void returnBook(@PathVariable("borrowId") Long borrowId) {
        borrowService.returnBook(borrowId);
    }

    // Add endpoints for fine-related operations
    @GetMapping(path = "/fines/student/{studentId}")
    public Map<String, Object> getStudentFines(@PathVariable("studentId") Long studentId) {
        return borrowService.getStudentFinesDetails(studentId);
    }

    @PutMapping(path = "/fines/pay/{borrowId}")
    public void payFine(@PathVariable("borrowId") Long borrowId) {
        borrowService.payFine(borrowId);
    }
}