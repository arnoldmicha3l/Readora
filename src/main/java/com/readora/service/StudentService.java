package com.readora.service;

import com.readora.database.StudentDAO;
import com.readora.model.Student;
import com.readora.user.ValidationService;

public final class StudentService {

    private static final StudentDAO studentDAO = new StudentDAO();

    private StudentService() {}

    public static boolean addStudent(Student student) {
        if (student == null
                || ValidationService.isBlank(student.getStudentId())
                || ValidationService.isBlank(student.getFullName())) {
            return false;
        }

        if (studentDAO.findById(student.getStudentId()) != null) {
            return false;
        }

        boolean success = studentDAO.insert(student);
        AppState.refreshStudents();
        return success;
    }

    public static boolean updateStudent(Student student) {
        if (student == null || ValidationService.isBlank(student.getStudentId())) {
            return false;
        }

        boolean success = studentDAO.update(student);
        AppState.refreshStudents();
        return success;
    }

    public static boolean deleteStudent(String studentId) {
        if (ValidationService.isBlank(studentId)) {
            return false;
        }

        boolean success = studentDAO.delete(studentId);
        AppState.refreshStudents();
        return success;
    }
}