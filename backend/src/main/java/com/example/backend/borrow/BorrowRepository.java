package com.example.backend.borrow;

import com.example.backend.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
    // Find all borrowed books by a student
    List<BorrowRecord> findByStudentAndStatusNot(Student student, BorrowStatus status);

    // Find overdue books
    List<BorrowRecord> findByDueDateBeforeAndStatus(LocalDate date, BorrowStatus status);

    List<BorrowRecord> findByStudent(Student student);
}