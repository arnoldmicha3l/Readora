package com.readora.user;

import com.readora.model.Person;

public class UserAccount extends Person {

    private String username;
    private String password;
    private UserRole role;
    private String studentId;

    public UserAccount(String fullName, String username, String password, UserRole role, String studentId) {
        super(fullName, "");
        this.username = username;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }

    public String getStudentId() { return studentId; }
}