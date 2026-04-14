package com.readora.controller;

import com.readora.user.UserRole;
import com.readora.user.AccountService;
import com.readora.service.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterViewController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                "Sign up as ADMIN",
                "Sign up as Librarian",
                "Sign up as Student"
        ));
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String selectedRole = roleComboBox.getValue();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || selectedRole == null
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Registration Error", "Please complete all registration fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Registration Error", "Passwords do not match.");
            return;
        }

        if (AccountService.usernameExists(username)) {
            showAlert(Alert.AlertType.WARNING, "Registration Error", "Username already exists.");
            return;
        }

        UserRole role = mapRole(selectedRole);
        boolean created = AccountService.registerAccount(fullName, username, password, role);

        if (!created) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Unable to create account.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                "Your account was created successfully as " + role.name() + ".");

        try {
            SceneNavigator.switchScene(event, getClass(), "/view/LoginView.fxml", "Readora - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to return to login.");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/LoginView.fxml", "Readora - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to return to login.");
        }
    }

    private UserRole mapRole(String selectedRole) {
        return switch (selectedRole) {
            case "Sign up as ADMIN" -> UserRole.ADMIN;
            case "Sign up as Librarian" -> UserRole.LIBRARIAN;
            case "Sign up as Student" -> UserRole.STUDENT;
            default -> UserRole.STUDENT;
        };
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}