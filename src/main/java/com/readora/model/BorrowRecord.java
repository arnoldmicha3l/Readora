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

    public BorrowRecord(String recordId, String studentId, String studentName,
                        String bookId, String bookTitle,
                        LocalDate borrowDate, LocalDate dueDate,
                        LocalDate returnDate, String status) {

        this.recordId = recordId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public static BorrowRecord createReservation(String recordId,
                                                 String studentId,
                                                 String studentName,
                                                 String bookId,
                                                 String bookTitle) {
        LocalDate today = LocalDate.now();

        return new BorrowRecord(
                recordId,
                studentId,
                studentName,
                bookId,
                bookTitle,
                today,
                null,
                null,
                "RESERVED"
        );
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

    public void setRecordId(String recordId) { this.recordId = recordId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBorrowStatus(BorrowStatus status) {
        this.status = status.name();
    }

    public boolean isReserved() {
        return "RESERVED".equalsIgnoreCase(status);
    }

    public boolean isBorrowed() {
        return "BORROWED".equalsIgnoreCase(status);
    }

    public boolean isReturned() {
        return "RETURNED".equalsIgnoreCase(status);
    }

    public boolean isOverdue() {
        return returnDate == null
                && dueDate != null
                && dueDate.isBefore(LocalDate.now());
    }
}