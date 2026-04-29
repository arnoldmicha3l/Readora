package com.readora.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        createUsersTable();
        createBooksTable();
        createStudentsTable();
        createBorrowRecordsTable();
    }

    private static void createUsersTable() {
        execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    student_id TEXT,
                    email TEXT,
                    phone TEXT,
                    age INTEGER,
                    gender TEXT
                );
                """);
    }

    private static void createBooksTable() {
        execute("""
                CREATE TABLE IF NOT EXISTS books (
                    book_id TEXT PRIMARY KEY,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    category TEXT NOT NULL,
                    status TEXT NOT NULL
                );
                """);
    }

    private static void createStudentsTable() {
        execute("""
                CREATE TABLE IF NOT EXISTS students (
                    student_id TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    email TEXT,
                    status TEXT NOT NULL
                );
                """);
    }

    private static void createBorrowRecordsTable() {
        execute("""
                CREATE TABLE IF NOT EXISTS borrow_records (
                    record_id TEXT PRIMARY KEY,
                    student_id TEXT,
                    student_name TEXT NOT NULL,
                    book_id TEXT,
                    book_title TEXT NOT NULL,
                    borrow_date TEXT NOT NULL,
                    due_date TEXT NOT NULL,
                    return_date TEXT,
                    status TEXT NOT NULL
                );
                """);
    }

    private static void execute(String sql) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            System.err.println("Database initialization error.");
            e.printStackTrace();
        }
    }
}