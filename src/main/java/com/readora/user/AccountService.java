package com.readora.user;

import com.readora.database.StudentDAO;
import com.readora.database.UserAccountDAO;
import com.readora.model.Student;
import com.readora.service.AppState;

public final class AccountService {

    private static final UserAccountDAO userAccountDAO = new UserAccountDAO();
    private static final StudentDAO studentDAO = new StudentDAO();

    private AccountService() {}

    public static boolean registerAccount(String fullName, String username, String password, UserRole role) {
        if (ValidationService.isBlank(fullName)
                || ValidationService.isBlank(username)
                || ValidationService.isBlank(password)
                || role == null) {
            return false;
        }

        String normalizedUsername = username.toLowerCase().trim();

        if (userAccountDAO.findById(normalizedUsername) != null) {
            return false;
        }

        String studentId = null;

        if (role == UserRole.STUDENT) {
            studentId = generateStudentId();
        }

        UserAccount account = new UserAccount(
                fullName.trim(),
                normalizedUsername,
                password,
                role,
                studentId
        );

        boolean inserted = userAccountDAO.insert(account);

        if (inserted && role == UserRole.STUDENT) {
            studentDAO.insert(new Student(studentId, fullName.trim(), "", "Active"));
        }

        AppState.refreshAll();
        return inserted;
    }

    public static UserAccount loginAccount(String username, String password) {
        if (ValidationService.isBlank(username) || password == null) {
            return null;
        }

        UserAccount account = userAccountDAO.findById(username.trim());

        if (account == null) {
            return null;
        }

        return account.getPassword().equals(password) ? account : null;
    }

    public static boolean updateUsername(UserAccount user, String newUsername) {
        if (user == null || ValidationService.isBlank(newUsername)) {
            return false;
        }

        String oldUsername = user.getUsername();
        String normalizedNewUsername = newUsername.toLowerCase().trim();

        if (!oldUsername.equalsIgnoreCase(normalizedNewUsername)
                && userAccountDAO.findById(normalizedNewUsername) != null) {
            return false;
        }

        user.setUsername(normalizedNewUsername);

        boolean updated = userAccountDAO.updateUsername(oldUsername, user);
        AppState.refreshAll();

        return updated;
    }

    public static boolean updateAccount(UserAccount account) {
        if (account == null) {
            return false;
        }

        boolean updated = userAccountDAO.update(account);
        AppState.refreshAll();

        return updated;
    }

    private static String generateStudentId() {
        int nextNumber = 1001;

        while (studentDAO.findById("STU-" + nextNumber) != null) {
            nextNumber++;
        }

        return "STU-" + nextNumber;
    }
}