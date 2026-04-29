package com.readora.database;

import com.readora.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements GenericDAO<Student, String> {

    @Override
    public boolean insert(Student student) {
        String sql = "INSERT INTO students (student_id, full_name, email, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getStudentId());
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getStatus());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(Student student) {
        String sql = "UPDATE students SET full_name = ?, email = ?, status = ? WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFullName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getStatus());
            statement.setString(4, student.getStudentId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Student findById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapToStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id ASC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                students.add(mapToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    private Student mapToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("student_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("status")
        );
    }
}