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
            stmt.setString(6, record.getBorrowDate() != null ? record.getBorrowDate().toString() : null);
            stmt.setString(7, record.getDueDate() != null ? record.getDueDate().toString() : null);
            stmt.setString(8, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            stmt.setString(9, record.getStatus() != null ? record.getStatus().toUpperCase() : "BORROWED");

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
            stmt.setString(5, record.getBorrowDate() != null ? record.getBorrowDate().toString() : null);
            stmt.setString(6, record.getDueDate() != null ? record.getDueDate().toString() : null);
            stmt.setString(7, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            stmt.setString(8, record.getStatus() != null ? record.getStatus().toUpperCase() : "BORROWED");
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
             PreparedStatement stmt = connectionlessPrepare(conn, sql);
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

    public List<BorrowRecord> findReservedRecords() {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = """
                SELECT * FROM borrow_records
                WHERE UPPER(status) = 'RESERVED'
                ORDER BY borrow_date ASC
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

    private PreparedStatement connectionlessPrepare(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    private BorrowRecord map(ResultSet rs) throws SQLException {
        String borrowDateText = rs.getString("borrow_date");
        String dueDateText = rs.getString("due_date");
        String returnDateText = rs.getString("return_date");

        return new BorrowRecord(
                rs.getString("record_id"),
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getString("book_id"),
                rs.getString("book_title"),
                borrowDateText != null && !borrowDateText.isEmpty() ? LocalDate.parse(borrowDateText) : null,
                dueDateText != null && !dueDateText.isEmpty() ? LocalDate.parse(dueDateText) : null,
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
            case "RESERVED" -> BorrowStatus.RESERVED;
            default -> BorrowStatus.BORROWED;
        };
    }
}