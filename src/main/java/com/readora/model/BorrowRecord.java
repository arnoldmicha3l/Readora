package com.readora.model;

import java.time.LocalDate;

public class BorrowRecord {

    private String recordId;
    private String studentId;
    private String studentName;
    private String bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public BorrowRecord(String recordId, String studentId, String studentName,
                        String bookId, String bookTitle,
                        LocalDate borrowDate, LocalDate dueDate,
                        LocalDate returnDate, BorrowStatus status) {

        this.recordId = recordId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status.name();
    }

    public String getRecordId() { return recordId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setBorrowStatus(BorrowStatus status) {
        this.status = status.name();
    }

    public boolean isOverdue() {
        return returnDate == null && dueDate.isBefore(LocalDate.now());
    }
}