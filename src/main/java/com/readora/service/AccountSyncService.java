package com.readora.service;

import com.readora.database.StudentDAO;
import com.readora.model.Student;
import com.readora.user.UserAccount;

public final class AccountSyncService {

    private static final StudentDAO studentDAO = new StudentDAO();

    private AccountSyncService() {}

    public static void syncStudentFromSession(UserAccount user) {
        if (user == null || user.getStudentId() == null) {
            return;
        }

        Student existing = studentDAO.findById(user.getStudentId());

        if (existing != null) {
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail() != null ? user.getEmail() : "");
            studentDAO.update(existing);
        }

        AppState.refreshStudents();
    }
}