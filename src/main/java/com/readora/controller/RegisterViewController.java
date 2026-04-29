package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.SceneNavigator;
import com.readora.user.AccountService;
import com.readora.user.UserRole;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class RegisterViewController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private ComboBox<UserRole> roleComboBox;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                UserRole.ADMIN,
                UserRole.LIBRARIAN,
                UserRole.STUDENT
        ));

        fullNameField.setTooltip(new Tooltip("Enter your full name"));
        usernameField.setTooltip(new Tooltip("Choose a username"));
        roleComboBox.setTooltip(new Tooltip("Choose your account role"));
        passwordField.setTooltip(new Tooltip("Create a password with at least 6 characters"));
        confirmPasswordField.setTooltip(new Tooltip("Confirm your password"));
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText() == null ? "" : fullNameField.getText().trim();
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String confirmPassword = confirmPasswordField.getText() == null ? "" : confirmPasswordField.getText();
        UserRole role = roleComboBox.getValue();

        if (fullName.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
                || role == null) {
            AlertHelper.showWarning("Validation Error", "Please complete all fields.");
            return;
        }

        if (password.length() < 6) {
            AlertHelper.showWarning("Validation Error", "Password must be at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertHelper.showWarning("Validation Error", "Passwords do not match.");
            return;
        }

        boolean success = AccountService.registerAccount(fullName, username, password, role);

        if (!success) {
            AlertHelper.showError("Registration Failed", "Username already exists or data is invalid.");
            return;
        }

        AlertHelper.showInfo("Success", "Account created successfully.");
        SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
    }
}