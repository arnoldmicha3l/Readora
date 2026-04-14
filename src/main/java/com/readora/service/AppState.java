package com.readora.service;

import com.readora.model.Book;
import com.readora.model.BorrowRecord;
import com.readora.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public final class AppState {

    private static final ObservableList<Book> BOOKS = FXCollections.observableArrayList();
    private static final ObservableList<Student> STUDENTS = FXCollections.observableArrayList();
    private static final ObservableList<BorrowRecord> BORROW_RECORDS = FXCollections.observableArrayList();

    static {
        BOOKS.addAll(
                new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available"),
                new Book("B002", "A Brief History of Time", "Stephen Hawking", "Science", "Borrowed"),
                new Book("B003", "Clean Code", "Robert C. Martin", "Technology", "Available"),
                new Book("B004", "Sapiens", "Yuval Noah Harari", "History", "Reserved"),
                new Book("B005", "The Montessori Method", "Maria Montessori", "Education", "Available")
        );

        STUDENTS.addAll(
                new Student("S001", "Student User", "student@readora.edu", "Active"),
                new Student("S002", "Ariana Cruz", "ariana.cruz@readora.edu", "Active"),
                new Student("S003", "Miguel Santos", "miguel.santos@readora.edu", "Active")
        );

        BORROW_RECORDS.addAll(
                new BorrowRecord(
                        "R001",
                        "Student User",
                        "The Great Gatsby",
                        LocalDate.now().minusDays(4),
                        LocalDate.now().plusDays(3),
                        null,
                        "Borrowed"
                ),
                new BorrowRecord(
                        "R002",
                        "Ariana Cruz",
                        "A Brief History of Time",
                        LocalDate.now().minusDays(10),
                        LocalDate.now().minusDays(2),
                        null,
                        "Overdue"
                ),
                new BorrowRecord(
                        "R003",
                        "Student User",
                        "Clean Code",
                        LocalDate.now().minusDays(12),
                        LocalDate.now().minusDays(5),
                        LocalDate.now().minusDays(3),
                        "Returned"
                )
        );
    }

    private AppState() {
    }

    public static ObservableList<Book> getBooks() {
        return BOOKS;
    }

    public static ObservableList<Student> getStudents() {
        return STUDENTS;
    }

    public static ObservableList<BorrowRecord> getBorrowRecords() {
        return BORROW_RECORDS;
    }
}