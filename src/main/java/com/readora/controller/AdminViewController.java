package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;

public class AdminViewController {

    @FXML private Button adminMenuButton;

    private ContextMenu adminMenu;

    @FXML
    public void initialize() {
        setupMenu();

        if (adminMenuButton != null) {
            adminMenuButton.setTooltip(new Tooltip("Open admin menu"));
        }
    }

    private void setupMenu() {
        adminMenu = new ContextMenu();

        MenuItem profile = new MenuItem("Admin Profile");
        MenuItem reports = new MenuItem("Reports");
        MenuItem about = new MenuItem("About Us");
        MenuItem logout = new MenuItem("Logout");

        profile.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(adminMenuButton, "/view/AdminProfile.fxml", "Readora - Admin Profile")
        );

        reports.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(adminMenuButton, "/view/AdminReportsView.fxml", "Readora - Reports")
        );

        about.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(adminMenuButton, "/view/AboutUsView.fxml", "Readora - About Us")
        );

        logout.setOnAction(event -> {
            boolean confirm = AlertHelper.confirm("Confirm Logout", "Are you sure you want to logout?");
            if (!confirm) return;

            SessionManager.clearSession();
            SceneNavigator.switchSceneFromNode(adminMenuButton, "/view/LoginView.fxml", "Readora - Login");
        });

        adminMenu.getItems().setAll(profile, reports, about, logout);
    }

    @FXML
    private void handleMenu(ActionEvent event) {
        if (adminMenu == null || adminMenuButton == null) return;

        if (adminMenu.isShowing()) {
            adminMenu.hide();
        } else {
            adminMenu.show(
                    adminMenuButton,
                    adminMenuButton.localToScreen(0, adminMenuButton.getHeight()).getX(),
                    adminMenuButton.localToScreen(0, adminMenuButton.getHeight()).getY()
            );
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminView.fxml", "Readora - Admin Dashboard");
    }

    @FXML
    private void handleBooks(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/BookFormView.fxml", "Readora - Book Management");
    }

    @FXML
    private void handleStudents(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminStudentManagement.fxml", "Readora - Student Management");
    }

    @FXML
    private void handleBorrowRecords(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/BorrowRecords.fxml", "Readora - Borrow Records");
    }

    @FXML
    private void handleReports(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminReportsView.fxml", "Readora - Reports");
    }
}