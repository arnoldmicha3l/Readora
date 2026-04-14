package com.readora.user;

import com.readora.user.UserAccount;
import com.readora.user.UserRole;

import java.util.HashMap;
import java.util.Map;

public final class AccountService {

    private static final Map<String, UserAccount> ACCOUNTS = new HashMap<>();
    private static int studentCounter = 1001;

    static {
        ACCOUNTS.put("admin",
                new UserAccount("System Admin", "admin", "admin123", UserRole.ADMIN, null));

        ACCOUNTS.put("librarian",
                new UserAccount("Main Librarian", "librarian", "lib123", UserRole.LIBRARIAN, null));

        ACCOUNTS.put("student",
                new UserAccount("Student User", "student", "stud123", UserRole.STUDENT, "STU-1000"));
    }

    private AccountService() {
    }

    public static boolean registerAccount(String fullName, String username, String password, UserRole role) {
        String normalizedUsername = username.toLowerCase().trim();

        if (ACCOUNTS.containsKey(normalizedUsername)) {
            return false;
        }

        String studentId = null;
        if (role == UserRole.STUDENT) {
            studentId = generateStudentId();
        }

        ACCOUNTS.put(normalizedUsername, new UserAccount(fullName, username, password, role, studentId));
        return true;
    }

    public static boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }
        return ACCOUNTS.containsKey(username.toLowerCase().trim());
    }

    public static UserAccount loginAccount(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        UserAccount account = ACCOUNTS.get(username.toLowerCase().trim());

        if (account == null) {
            return null;
        }

        if (!account.getPassword().equals(password)) {
            return null;
        }

        return account;
    }

    private static String generateStudentId() {
        return "STU-" + studentCounter++;
    }
}