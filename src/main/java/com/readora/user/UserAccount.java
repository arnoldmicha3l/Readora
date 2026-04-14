package com.readora.user;

public class UserAccount {

    private final String fullName;
    private final String username;
    private final String password;
    private final UserRole role;
    private final String studentId;

    public UserAccount(String fullName, String username, String password, UserRole role, String studentId) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public String getStudentId() {
        return studentId;
    }
}