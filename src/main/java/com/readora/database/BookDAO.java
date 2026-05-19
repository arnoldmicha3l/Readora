package com.readora.database;

import com.readora.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements GenericDAO<Book, String> {

    @Override
    public boolean insert(Book book) {
        String sql = "INSERT INTO books (book_id, title, author, category, status, cover_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getBookId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setString(5, book.getStatus());
            statement.setString(6, book.getCoverPath());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, status = ?, cover_path = ? WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getCategory());
            statement.setString(4, book.getStatus());
            statement.setString(5, book.getCoverPath());
            statement.setString(6, book.getBookId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Book findById(String bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapToBook(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY book_id ASC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                books.add(mapToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    private Book mapToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getString("status"),
                rs.getString("cover_path")
        );
    }
}