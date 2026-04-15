package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.user.UserAccount;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentViewController {

    @FXML
    private Label welcomeNameLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label totalBorrowedLabel;

    @FXML
    private Label totalReturnedLabel;

    @FXML
    private Button studentMenuButton;

    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();
        loadStudentHeader();
        refreshCounts();
    }

    private void loadStudentHeader() {
        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            if (welcomeNameLabel != null) {
                welcomeNameLabel.setText("Hello " + currentUser.getFullName());
            }

            if (studentIdLabel != null) {
                String idText = currentUser.getStudentId() != null
                        ? "Student ID: " + currentUser.getStudentId()
                        : "Student ID: Not Available";
                studentIdLabel.setText(idText);
            }
        } else {
            if (welcomeNameLabel != null) {
                welcomeNameLabel.setText("Hello Student User");
            }

            if (studentIdLabel != null) {
                studentIdLabel.setText("Student ID: Not Available");
            }
        }
    }

    private void setupStudentMenu() {
        studentContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> handleViewProfile());
        settingsItem.setOnAction(event -> showInfo("Settings", "Student settings feature will be added soon."));
        aboutUsItem.setOnAction(event -> handleAboutUs());
        logoutItem.setOnAction(event -> handleLogout());

        studentContextMenu.getItems().addAll(viewProfileItem, settingsItem, aboutUsItem, logoutItem);
    }

    private void refreshCounts() {
        UserAccount currentUser = SessionManager.getCurrentUser();
        String fullName = currentUser != null ? currentUser.getFullName() : "";

        ObservableList<BorrowRecord> records = FXCollections.observableArrayList(AppState.getBorrowRecords());

        long borrowedCount = records.stream()
                .filter(record -> record.getStudentName().equalsIgnoreCase(fullName))
                .filter(record -> !"Returned".equalsIgnoreCase(record.getStatus()))
                .count();

        long returnedCount = records.stream()
                .filter(record -> record.getStudentName().equalsIgnoreCase(fullName))
                .filter(record -> "Returned".equalsIgnoreCase(record.getStatus()))
                .count();

        if (totalBorrowedLabel != null) {
            totalBorrowedLabel.setText(String.valueOf(borrowedCount));
        }

        if (totalReturnedLabel != null) {
            totalReturnedLabel.setText(String.valueOf(returnedCount));
        }
    }

    private void handleViewProfile() {
        try {

            Stage stage = (Stage) studentMenuButton.getScene().getWindow();


            SceneNavigator.switchScene(
                    stage,
                    getClass(),
                    "/view/StudentProfile.fxml",
                    "Readora - My Profile"
            );

        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open Student Profile view.");
        }
    }

    private void handleAboutUs() {
        try {
            Stage stage = (Stage) studentMenuButton.getScene().getWindow();
            SceneNavigator.switchScene(stage, getClass(), "/view/AboutUsView.fxml", "Readora - About Us");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open About Us page.");
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
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/BrowseBooks.fxml", "Readora - Browse Books");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open Browse Books.");
        }
    }

    @FXML
    public void handleHistoryTab(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/MyHistory.fxml", "Readora - My History");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open My History.");
        }
    }

    private void handleLogout() {
        SessionManager.clearSession();

        try {
            Stage stage = (Stage) studentMenuButton.getScene().getWindow();
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