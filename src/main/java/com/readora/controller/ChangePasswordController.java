package com.readora.controller;

import com.readora.service.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class ChangePasswordController {

    @FXML private PasswordField currentPasswordField;
    @FXML private TextField currentPasswordTextField;
    @FXML private Button showCurrentBtn;

    @FXML private PasswordField newPasswordField;
    @FXML private TextField newPasswordTextField;
    @FXML private Button showNewBtn;

    @FXML private PasswordField confirmPasswordField;

    /**
     * Toggles visibility for the Current Password field.
     */
    @FXML
    private void toggleCurrentPassword() {
        if (currentPasswordField.isVisible()) {
            currentPasswordTextField.setText(currentPasswordField.getText());
            currentPasswordTextField.setVisible(true);
            currentPasswordField.setVisible(false);
            showCurrentBtn.setText("HIDE"); // Or use an icon like
        } else {
            currentPasswordField.setText(currentPasswordTextField.getText());
            currentPasswordField.setVisible(true);
            currentPasswordTextField.setVisible(false);
            showCurrentBtn.setText("SHOW");
        }
    }

    /**
     * Toggles visibility for the New Password field.
     */
    @FXML
    private void toggleNewPassword() {
        if (newPasswordField.isVisible()) {
            newPasswordTextField.setText(newPasswordField.getText());
            newPasswordTextField.setVisible(true);
            newPasswordField.setVisible(false);
            showNewBtn.setText("HIDE");
        } else {
            newPasswordField.setText(newPasswordTextField.getText());
            newPasswordField.setVisible(true);
            newPasswordTextField.setVisible(false);
            showNewBtn.setText("SHOW");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        // Logic to get text regardless of which field is currently visible
        String currentPass = currentPasswordField.isVisible() ? currentPasswordField.getText() : currentPasswordTextField.getText();
        String newPass = newPasswordField.isVisible() ? newPasswordField.getText() : newPasswordTextField.getText();
        String confirmPass = confirmPasswordField.getText();

        // 1. Basic Validation
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        // 2. Confirm Password Match
        if (!newPass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Mismatch", "New password and confirmation do not match.");
            return;
        }

        // 3. TODO: Add logic to verify currentPass against Database/Session

        // Success Message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been updated successfully!");

        // Return to Profile
        handleBack(event);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/StudentProfile.fxml", "Readora - My Profile");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the profile view.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}