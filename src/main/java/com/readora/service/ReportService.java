package com.readora.service;

public class ReportService {

    public static int getTotalBooks() {
        return AppState.getBooks().size();
    }

    public static int getTotalStudents() {
        return AppState.getStudents().size();
    }

    public static int getBorrowedBooks() {
        return (int) AppState.getBorrowRecords().stream()
                .filter(r -> "BORROWED".equalsIgnoreCase(r.getStatus()))
                .count();
    }

    public static int getReturnedRecords() {
        return (int) AppState.getBorrowRecords().stream()
                .filter(r -> "RETURNED".equalsIgnoreCase(r.getStatus()))
                .count();
    }

    public static int getOverdueRecords() {
        return (int) AppState.getBorrowRecords().stream()
                .filter(r -> "OVERDUE".equalsIgnoreCase(r.getStatus()))
                .count();
    }
}