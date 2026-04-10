package com.readora.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentViewController {

    @FXML
    private Label totalBorrowedLabel;

    @FXML
    private Button studentMenuButton;

    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();
        updateBorrowedCount(0);
    }

    private void setupStudentMenu() {
        studentContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> showInfo("Profile", "Student profile details can be added here soon."));
        settingsItem.setOnAction(event -> showInfo("Settings", "Student settings feature will be added soon."));
        helpItem.setOnAction(event -> showInfo("Help", "Readora Help Center is not yet available."));
        logoutItem.setOnAction(this::handleLogout);

        studentContextMenu.getItems().addAll(viewProfileItem, settingsItem, helpItem, logoutItem);
    }

    public void updateBorrowedCount(int count) {
        if (totalBorrowedLabel != null) {
            totalBorrowedLabel.setText(String.valueOf(count));
        }
    }

    @FXML
    public void handleStudentMenu() {
        if (studentContextMenu.isShowing()) {
            studentContextMenu.hide();
        } else {
            double x = studentMenuButton.localToScreen(0, 0).getX();
            double y = studentMenuButton.localToScreen(0, studentMenuButton.getHeight()).getY();
            studentContextMenu.show(studentMenuButton, x, y);
        }
    }

    @FXML
    public void handleBrowseBooks(ActionEvent event) {
        navigateTo(event, "/view/BrowseBooks.fxml");
    }

    @FXML
    public void handleHistoryTab(ActionEvent event) {
        navigateTo(event, "/view/MyHistory.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) studentMenuButton.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            boolean wasMaximized = stage.isMaximized();

            Scene newScene = new Scene(root, currentWidth, currentHeight);

            stage.setTitle("Readora - Login");
            stage.setScene(newScene);
            stage.setMinWidth(1200);
            stage.setMinHeight(700);

            Platform.runLater(() -> {
                stage.setMaximized(wasMaximized || true);
                stage.centerOnScreen();
                stage.show();
            });
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to return to the login page.");
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            boolean wasMaximized = stage.isMaximized();

            Scene newScene = new Scene(root, currentWidth, currentHeight);

            stage.setMinWidth(1200);
            stage.setMinHeight(700);
            stage.setScene(newScene);

            Platform.runLater(() -> {
                stage.setMaximized(wasMaximized);
                stage.centerOnScreen();
                stage.show();
            });
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
