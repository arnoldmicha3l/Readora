package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.BackgroundTaskService;
import com.readora.service.SceneNavigator;
import com.readora.user.AccountService;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView logoImage;

    @FXML
    public void initialize() {
        setupLogo();

        if (usernameField != null) {
            usernameField.setTooltip(new Tooltip("Enter your username"));
        }

        if (passwordField != null) {
            passwordField.setTooltip(new Tooltip("Enter your password"));
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showWarning("Validation Error", "Please enter your username and password.");
            return;
        }

        setLoginDisabled(true);

        BackgroundTaskService.runTask(
                () -> AccountService.loginAccount(username, password),

                user -> {
                    setLoginDisabled(false);

                    if (user == null) {
                        AlertHelper.showError("Login Failed", "Invalid username or password.");
                        return;
                    }

                    SessionManager.setCurrentUser(user);

                    switch (user.getRole()) {
                        case ADMIN ->
                                SceneNavigator.switchScene(event, "/view/AdminView.fxml", "Readora - Admin Dashboard");

                        case LIBRARIAN ->
                                SceneNavigator.switchScene(event, "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");

                        case STUDENT ->
                                SceneNavigator.switchScene(event, "/view/StudentView.fxml", "Readora - Student Dashboard");
                    }
                },

                error -> {
                    setLoginDisabled(false);
                    error.printStackTrace();
                    AlertHelper.showError("Login Error", "Unable to process login request.");
                }
        );
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/RegisterView.fxml", "Readora - Register");
    }

    private void setLoginDisabled(boolean disabled) {
        if (usernameField != null) {
            usernameField.setDisable(disabled);
        }

        if (passwordField != null) {
            passwordField.setDisable(disabled);
        }
    }

    private void setupLogo() {
        if (logoImage == null) {
            return;
        }

        Image image = new Image(getClass().getResource("/view/logo.png").toExternalForm());
        logoImage.setImage(image);

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        double cropSize = Math.min(imageWidth, imageHeight) * 0.60;
        double cropX = (imageWidth - cropSize) / 2;
        double cropY = (imageHeight - cropSize) / 2;

        logoImage.setViewport(new Rectangle2D(cropX, cropY, cropSize, cropSize));
    }
}