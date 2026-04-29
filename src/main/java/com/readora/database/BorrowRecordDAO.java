package com.readora.database;

import com.readora.model.BorrowRecord;
import com.readora.model.BorrowStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAO {

    public boolean insert(BorrowRecord record) {
        String sql = """
                INSERT INTO borrow_records
                (record_id, student_id, student_name, book_id, book_title, borrow_date, due_date, return_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getRecordId());
            stmt.setString(2, record.getStudentId());
            stmt.setString(3, record.getStudentName());
            stmt.setString(4, record.getBookId());
            stmt.setString(5, record.getBookTitle());
            stmt.setString(6, record.getBorrowDate().toString());
            stmt.setString(7, record.getDueDate().toString());
            stmt.setString(8, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            stmt.setString(9, record.getStatus().toUpperCase());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(BorrowRecord record) {
        String sql = """
                UPDATE borrow_records
                SET student_id = ?, student_name = ?, book_id = ?, book_title = ?,
                    borrow_date = ?, due_date = ?, return_date = ?, status = ?
                WHERE record_id = ?
                """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getStudentId());
            stmt.setString(2, record.getStudentName());
            stmt.setString(3, record.getBookId());
            stmt.setString(4, record.getBookTitle());
            stmt.setString(5, record.getBorrowDate().toString());
            stmt.setString(6, record.getDueDate().toString());
            stmt.setString(7, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            stmt.setString(8, record.getStatus().toUpperCase());
            stmt.setString(9, record.getRecordId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String recordId) {
        String sql = "DELETE FROM borrow_records WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recordId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BorrowRecord findById(String recordId) {
        String sql = "SELECT * FROM borrow_records WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recordId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<BorrowRecord> findAll() {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records ORDER BY borrow_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BorrowRecord> findActiveBorrowedRecords() {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = """
                SELECT * FROM borrow_records
                WHERE UPPER(status) = 'BORROWED' OR UPPER(status) = 'OVERDUE'
                ORDER BY due_date ASC
                """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private BorrowRecord map(ResultSet rs) throws SQLException {
        String returnDateText = rs.getString("return_date");

        return new BorrowRecord(
                rs.getString("record_id"),
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getString("book_id"),
                rs.getString("book_title"),
                LocalDate.parse(rs.getString("borrow_date")),
                LocalDate.parse(rs.getString("due_date")),
                returnDateText != null && !returnDateText.isEmpty() ? LocalDate.parse(returnDateText) : null,
                parseStatus(rs.getString("status"))
        );
    }

    private BorrowStatus parseStatus(String status) {
        if (status == null) {
            return BorrowStatus.BORROWED;
        }

        return switch (status.trim().toUpperCase()) {
            case "RETURNED" -> BorrowStatus.RETURNED;
            case "OVERDUE" -> BorrowStatus.OVERDUE;
            default -> BorrowStatus.BORROWED;
        };
    }
}