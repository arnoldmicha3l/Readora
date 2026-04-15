package com.readora.controller;

import com.readora.user.UserAccount;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewController {

    @FXML
    private Button adminMenuButton;

    private ContextMenu adminContextMenu;

    @FXML
    public void initialize() {
        adminContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> handleViewProfile());
        settingsItem.setOnAction(event -> handleSettings());
        aboutUsItem.setOnAction(event -> handleAboutUs());
        logoutItem.setOnAction(event -> handleLogout());

        adminContextMenu.getItems().addAll(viewProfileItem, settingsItem, aboutUsItem, logoutItem);
    }

    @FXML
    private void handleAdminMenu() {
        if (adminContextMenu.isShowing()) {
            adminContextMenu.hide();
        } else {
            double x = adminMenuButton.localToScreen(0, 0).getX();
            double y = adminMenuButton.localToScreen(0, adminMenuButton.getHeight()).getY();
            adminContextMenu.show(adminMenuButton, x, y);
        }
    }

    @FXML
    private void handleOpenBooks(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/BookFormView.fxml", "Readora - Book Management");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open Book Management.");
        }
    }

    @FXML
    private void handleOpenStudents(ActionEvent event) {
        showInfo("Students", "Student management module can be added next. This button is now active.");
    }

    @FXML
    private void handleOpenBorrowRecords(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/BorrowRecordsView.fxml", "Readora - Borrow Records");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open Borrow Records.");
        }
    }

    @FXML
    private void handleOpenReports(ActionEvent event) {
        showInfo("Reports", "Reports module can be expanded next. This button is now active.");
    }

    private void handleViewProfile() {
        UserAccount currentUser = SessionManager.getCurrentUser();
        String fullName = currentUser != null ? currentUser.getFullName() : "Admin";
        showInfo("Profile", "Logged in as: " + fullName);
    }

    private void handleSettings() {
        showInfo("Settings", "System settings feature will be added soon.");
    }

    private void handleAboutUs() {
        showInfo(
                "About Us",
                "Readora is a Smart Library Management System designed to help manage books, borrowing records, and student library services in a simple and organized way."
        );
    }

    private void handleLogout() {
        SessionManager.clearSession();
        try {
            Stage stage = (Stage) adminMenuButton.getScene().getWindow();
            SceneNavigator.switchScene(stage, getClass(), "/view/LoginView.fxml", "Readora - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to return to login.");
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