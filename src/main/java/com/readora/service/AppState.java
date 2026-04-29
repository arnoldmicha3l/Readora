package com.readora.service;

import com.readora.database.BookDAO;
import com.readora.database.BorrowRecordDAO;
import com.readora.database.StudentDAO;
import com.readora.database.UserAccountDAO;
import com.readora.model.Book;
import com.readora.model.BorrowRecord;
import com.readora.model.BorrowStatus;
import com.readora.model.Student;
import com.readora.user.UserAccount;
import com.readora.user.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public final class AppState {

    private static final ObservableList<Book> BOOKS = FXCollections.observableArrayList();
    private static final ObservableList<Student> STUDENTS = FXCollections.observableArrayList();
    private static final ObservableList<BorrowRecord> BORROW_RECORDS = FXCollections.observableArrayList();

    private static final BookDAO bookDAO = new BookDAO();
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private static final UserAccountDAO userAccountDAO = new UserAccountDAO();

    private AppState() {
    }

    public static void initializeData() {
        seedDefaultData();
        refreshAll();
    }

    public static void refreshAll() {
        refreshBooks();
        refreshStudents();
        refreshBorrowRecords();
    }

    public static void refreshBooks() {
        BOOKS.setAll(bookDAO.findAll());
    }

    public static void refreshStudents() {
        STUDENTS.setAll(studentDAO.findAll());
    }

    public static void refreshBorrowRecords() {
        BORROW_RECORDS.setAll(borrowRecordDAO.findAll());
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

    private static void seedDefaultData() {
        seedUsers();
        seedStudents();
        seedBooks();
        seedBorrowRecords();
    }

    private static void seedUsers() {
        if (!userAccountDAO.findAll().isEmpty()) {
            return;
        }

        UserAccount admin = new UserAccount(
                "System Admin",
                "admin",
                "admin123",
                UserRole.ADMIN,
                null
        );
        admin.setEmail("admin@readora.edu");
        userAccountDAO.insert(admin);

        UserAccount librarian = new UserAccount(
                "Main Librarian",
                "librarian",
                "lib123",
                UserRole.LIBRARIAN,
                null
        );
        librarian.setEmail("librarian@readora.edu");
        userAccountDAO.insert(librarian);

        UserAccount student = new UserAccount(
                "Student User",
                "student",
                "stud123",
                UserRole.STUDENT,
                "STU-1000"
        );
        student.setEmail("student@readora.edu");
        userAccountDAO.insert(student);
    }

    private static void seedStudents() {
        if (!studentDAO.findAll().isEmpty()) {
            return;
        }

        studentDAO.insert(new Student("STU-1000", "Student User", "student@readora.edu", "Active"));
        studentDAO.insert(new Student("S002", "Ariana Cruz", "ariana.cruz@readora.edu", "Active"));
        studentDAO.insert(new Student("S003", "Miguel Santos", "miguel.santos@readora.edu", "Active"));
    }

    private static void seedBooks() {
        if (!bookDAO.findAll().isEmpty()) {
            return;
        }

        bookDAO.insert(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available"));
        bookDAO.insert(new Book("B002", "A Brief History of Time", "Stephen Hawking", "Science", "Borrowed"));
        bookDAO.insert(new Book("B003", "Clean Code", "Robert C. Martin", "Technology", "Available"));
        bookDAO.insert(new Book("B004", "Sapiens", "Yuval Noah Harari", "History", "Reserved"));
        bookDAO.insert(new Book("B005", "The Montessori Method", "Maria Montessori", "Education", "Available"));
    }

    private static void seedBorrowRecords() {
        if (!borrowRecordDAO.findAll().isEmpty()) {
            return;
        }

        borrowRecordDAO.insert(new BorrowRecord(
                "R001",
                "STU-1000",
                "Student User",
                "B001",
                "The Great Gatsby",
                LocalDate.now().minusDays(4),
                LocalDate.now().plusDays(3),
                null,
                BorrowStatus.BORROWED
        ));

        borrowRecordDAO.insert(new BorrowRecord(
                "R002",
                "S002",
                "Ariana Cruz",
                "B002",
                "A Brief History of Time",
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(2),
                null,
                BorrowStatus.OVERDUE
        ));

        borrowRecordDAO.insert(new BorrowRecord(
                "R003",
                "STU-1000",
                "Student User",
                "B003",
                "Clean Code",
                LocalDate.now().minusDays(12),
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(3),
                BorrowStatus.RETURNED
        ));
    }
}