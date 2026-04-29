package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.NavigationHelper;
import com.readora.user.AccountService;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChangePasswordController {

    @FXML private PasswordField currentPasswordField;
    @FXML private TextField currentPasswordTextField;

    @FXML private PasswordField newPasswordField;
    @FXML private TextField newPasswordTextField;

    @FXML private PasswordField confirmPasswordField;

    @FXML
    public void initialize() {
        if (currentPasswordTextField != null) {
            currentPasswordTextField.setVisible(false);
            currentPasswordTextField.setManaged(false);
        }

        if (newPasswordTextField != null) {
            newPasswordTextField.setVisible(false);
            newPasswordTextField.setManaged(false);
        }
    }

    @FXML
    private void toggleCurrentPassword() {
        togglePasswordVisibility(currentPasswordField, currentPasswordTextField);
    }

    @FXML
    private void toggleNewPassword() {
        togglePasswordVisibility(newPasswordField, newPasswordTextField);
    }

    private void togglePasswordVisibility(PasswordField hiddenField, TextField visibleField) {
        if (hiddenField == null || visibleField == null) return;

        if (hiddenField.isVisible()) {
            visibleField.setText(hiddenField.getText());
            hiddenField.setVisible(false);
            hiddenField.setManaged(false);
            visibleField.setVisible(true);
            visibleField.setManaged(true);
        } else {
            hiddenField.setText(visibleField.getText());
            visibleField.setVisible(false);
            visibleField.setManaged(false);
            hiddenField.setVisible(true);
            hiddenField.setManaged(true);
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            AlertHelper.showError("Session Error", "No active user session found.");
            return;
        }

        String currentPassword = currentPasswordField.isVisible()
                ? currentPasswordField.getText()
                : currentPasswordTextField.getText();

        String newPassword = newPasswordField.isVisible()
                ? newPasswordField.getText()
                : newPasswordTextField.getText();

        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            AlertHelper.showWarning("Validation Error", "Please complete all password fields.");
            return;
        }

        if (!currentUser.getPassword().equals(currentPassword)) {
            AlertHelper.showWarning("Validation Error", "Current password is incorrect.");
            return;
        }

        if (newPassword.length() < 6) {
            AlertHelper.showWarning("Validation Error", "New password must be at least 6 characters.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertHelper.showWarning("Validation Error", "New passwords do not match.");
            return;
        }

        currentUser.setPassword(newPassword);

        if (AccountService.updateAccount(currentUser)) {
            AlertHelper.showInfo("Success", "Password updated successfully.");
            clearFields();
        } else {
            AlertHelper.showError("Update Failed", "Unable to update password.");
        }
    }

    private void clearFields() {
        currentPasswordField.clear();
        currentPasswordTextField.clear();
        newPasswordField.clear();
        newPasswordTextField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            NavigationHelper.goToDashboard(event, getClass());
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to return to dashboard.");
        }
    }
}