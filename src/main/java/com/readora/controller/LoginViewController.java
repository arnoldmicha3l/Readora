package com.readora.controller;

import com.readora.user.UserAccount;
import com.readora.user.UserRole;
import com.readora.user.AccountService;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView loginLogoView;

    @FXML
    public void initialize() {
        loadLogo();
    }

    private void loadLogo() {
        try {
            Image logo = new Image(
                    Objects.requireNonNull(getClass().getResource("/images/logo.png")).toExternalForm()
            );

            loginLogoView.setImage(logo);

            double imageWidth = logo.getWidth();
            double imageHeight = logo.getHeight();

            double cropSize = Math.min(imageWidth, imageHeight) * 0.59;
            double x = (imageWidth - cropSize) / 2;
            double y = (imageHeight - cropSize) / 2;

            loginLogoView.setViewport(new Rectangle2D(x, y, cropSize, cropSize));
        } catch (Exception e) {
            System.err.println("Logo could not be loaded: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter your username and password.");
            return;
        }

        UserAccount account = AccountService.loginAccount(username, password);

        if (account == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
            return;
        }

        SessionManager.setCurrentUser(account);

        try {
            if (account.getRole() == UserRole.ADMIN) {
                SceneNavigator.switchScene(event, getClass(), "/view/AdminView.fxml", "Readora - Admin Dashboard");
            } else if (account.getRole() == UserRole.LIBRARIAN) {
                SceneNavigator.switchScene(event, getClass(), "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");
            } else if (account.getRole() == UserRole.STUDENT) {
                SceneNavigator.switchScene(event, getClass(), "/view/StudentView.fxml", "Readora - Student Dashboard");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Unknown role detected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to open the correct dashboard.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/RegisterView.fxml", "Readora - Register");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to open the registration page.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}