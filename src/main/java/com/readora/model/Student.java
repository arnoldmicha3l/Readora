package com.readora.model;

public class Student extends Person implements Identifiable<String> {

    private String studentId;
    private String status;

    public Student() {}

    public Student(String studentId, String fullName, String email, String status) {
        super(fullName, email);
        this.studentId = studentId;
        this.status = status;
    }

    @Override
    public String getId() {
        return studentId;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}