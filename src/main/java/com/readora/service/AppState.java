package com.readora.service;

import com.readora.model.Book;
import com.readora.model.BorrowRecord;
import com.readora.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public final class AppState {

    private static final ObservableList<Book> BOOKS = FXCollections.observableArrayList();
    private static final ObservableList<Member> MEMBERS = FXCollections.observableArrayList();
    private static final ObservableList<BorrowRecord> BORROW_RECORDS = FXCollections.observableArrayList();

    static {
        BOOKS.addAll(
                new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available"),
                new Book("B002", "A Brief History of Time", "Stephen Hawking", "Science", "Borrowed"),
                new Book("B003", "Clean Code", "Robert C. Martin", "Technology", "Available"),
                new Book("B004", "Sapiens", "Yuval Noah Harari", "History", "Reserved"),
                new Book("B005", "The Montessori Method", "Maria Montessori", "Education", "Available")
        );

        MEMBERS.addAll(
                new Member("M001", "Ariana Cruz", "ariana.cruz@readora.edu", "Active"),
                new Member("M002", "Miguel Santos", "miguel.santos@readora.edu", "Active"),
                new Member("M003", "Jessa Lim", "jessa.lim@readora.edu", "Inactive")
        );

        BORROW_RECORDS.addAll(
                new BorrowRecord("R001", "Ariana Cruz", "A Brief History of Time", LocalDate.now().minusDays(3), LocalDate.now().plusDays(4), "Borrowed"),
                new BorrowRecord("R002", "Miguel Santos", "Sapiens", LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), "Overdue")
        );
    }

    private AppState() {
    }

    public static ObservableList<Book> getBooks() {
        return BOOKS;
    }

    public static ObservableList<Member> getMembers() {
        return MEMBERS;
    }

    public static ObservableList<BorrowRecord> getBorrowRecords() {
        return BORROW_RECORDS;
    }
}
