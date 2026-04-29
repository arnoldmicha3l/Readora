package com.readora.database;

import com.readora.user.UserAccount;
import com.readora.user.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDAO implements GenericDAO<UserAccount, String> {

    @Override
    public boolean insert(UserAccount account) {
        String sql = """
                INSERT INTO users
                (username, full_name, password, role, student_id, email, phone, age, gender)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            fillStatement(statement, account);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(UserAccount account) {
        String sql = """
                UPDATE users
                SET full_name = ?, password = ?, role = ?, student_id = ?, email = ?, phone = ?, age = ?, gender = ?
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getFullName());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getRole().name());
            statement.setString(4, account.getStudentId());
            statement.setString(5, account.getEmail());
            statement.setString(6, account.getPhone());

            if (account.getAge() != null) {
                statement.setInt(7, account.getAge());
            } else {
                statement.setNull(7, Types.INTEGER);
            }

            statement.setString(8, account.getGender());
            statement.setString(9, account.getUsername());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateUsername(String oldUsername, UserAccount account) {
        String sql = """
                UPDATE users
                SET username = ?, full_name = ?, password = ?, role = ?, student_id = ?, email = ?, phone = ?, age = ?, gender = ?
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getUsername());
            statement.setString(2, account.getFullName());
            statement.setString(3, account.getPassword());
            statement.setString(4, account.getRole().name());
            statement.setString(5, account.getStudentId());
            statement.setString(6, account.getEmail());
            statement.setString(7, account.getPhone());

            if (account.getAge() != null) {
                statement.setInt(8, account.getAge());
            } else {
                statement.setNull(8, Types.INTEGER);
            }

            statement.setString(9, account.getGender());
            statement.setString(10, oldUsername);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public UserAccount findById(String username) {
        String sql = "SELECT * FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapToUserAccount(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UserAccount> findAll() {
        List<UserAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username ASC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                accounts.add(mapToUserAccount(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    private void fillStatement(PreparedStatement statement, UserAccount account) throws SQLException {
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getFullName());
        statement.setString(3, account.getPassword());
        statement.setString(4, account.getRole().name());
        statement.setString(5, account.getStudentId());
        statement.setString(6, account.getEmail());
        statement.setString(7, account.getPhone());

        if (account.getAge() != null) {
            statement.setInt(8, account.getAge());
        } else {
            statement.setNull(8, Types.INTEGER);
        }

        statement.setString(9, account.getGender());
    }

    private UserAccount mapToUserAccount(ResultSet rs) throws SQLException {
        UserAccount account = new UserAccount(
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("password"),
                UserRole.valueOf(rs.getString("role")),
                rs.getString("student_id")
        );

        account.setEmail(rs.getString("email"));
        account.setPhone(rs.getString("phone"));

        int age = rs.getInt("age");
        if (!rs.wasNull()) {
            account.setAge(age);
        }

        account.setGender(rs.getString("gender"));

        return account;
    }
}