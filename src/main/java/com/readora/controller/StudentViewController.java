package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;

public class StudentViewController {

    @FXML private Label welcomeNameLabel;
    @FXML private Label studentIdLabel;
    @FXML private Label totalBorrowedLabel;
    @FXML private Label totalReturnedLabel;
    @FXML private Button studentMenuButton;

    private ContextMenu studentMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();
        loadDashboardData();
    }

    private void setupStudentMenu() {
        studentMenu = new ContextMenu();

        MenuItem profileItem = new MenuItem("View Profile");
        MenuItem passwordItem = new MenuItem("Change Password");
        MenuItem aboutItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        profileItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/StudentProfile.fxml",
                        "Readora - Student Profile"
                )
        );

        passwordItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/ChangePassword.fxml",
                        "Readora - Change Password"
                )
        );

        aboutItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/AboutUsView.fxml",
                        "Readora - About Us"
                )
        );

        logoutItem.setOnAction(event -> {
            boolean confirmed = AlertHelper.confirm(
                    "Confirm Logout",
                    "Are you sure you want to logout?"
            );

            if (!confirmed) {
                return;
            }

            SessionManager.clearSession();
            SceneNavigator.switchSceneFromNode(
                    studentMenuButton,
                    "/view/LoginView.fxml",
                    "Readora - Login"
            );
        });

        studentMenu.getItems().setAll(profileItem, passwordItem, aboutItem, logoutItem);

        if (studentMenuButton != null) {
            studentMenuButton.setTooltip(new Tooltip("Open student menu"));
        }
    }

    private void loadDashboardData() {
        AppState.refreshBorrowRecords();

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            setLabel(welcomeNameLabel, "Hello, Student");
            setLabel(studentIdLabel, "Student ID: N/A");
            setLabel(totalBorrowedLabel, "0");
            setLabel(totalReturnedLabel, "0");
            return;
        }

        setLabel(welcomeNameLabel, "Hello, " + currentUser.getFullName());

        if (currentUser.getStudentId() != null) {
            setLabel(studentIdLabel, "Student ID: " + currentUser.getStudentId());
        } else {
            setLabel(studentIdLabel, "Student ID: N/A");
        }

        long totalBorrowed = AppState.getBorrowRecords().stream()
                .filter(record -> belongsToCurrentStudent(record, currentUser))
                .count();

        long totalReturned = AppState.getBorrowRecords().stream()
                .filter(record -> belongsToCurrentStudent(record, currentUser))
                .filter(record -> "RETURNED".equalsIgnoreCase(record.getStatus()))
                .count();

        setLabel(totalBorrowedLabel, String.valueOf(totalBorrowed));
        setLabel(totalReturnedLabel, String.valueOf(totalReturned));
    }

    private boolean belongsToCurrentStudent(BorrowRecord record, UserAccount user) {
        if (record == null || user == null) {
            return false;
        }

        if (record.getStudentId() == null || user.getStudentId() == null) {
            return false;
        }

        return record.getStudentId().equalsIgnoreCase(user.getStudentId());
    }

    private void setLabel(Label label, String value) {
        if (label != null) {
            label.setText(value);
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadDashboardData();
    }

    @FXML
    private void handleBrowseBooks(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/BrowseBooks.fxml",
                "Readora - Browse Books"
        );
    }

    @FXML
    private void handleHistoryTab(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/MyHistory.fxml",
                "Readora - My History"
        );
    }

    @FXML
    private void handleStudentMenu(ActionEvent event) {
        if (studentMenuButton == null || studentMenu == null) {
            AlertHelper.showError("Menu Error", "Student menu is not available.");
            return;
        }

        if (studentMenu.isShowing()) {
            studentMenu.hide();
        } else {
            studentMenu.show(
                    studentMenuButton,
                    studentMenuButton.localToScreen(0, studentMenuButton.getHeight()).getX(),
                    studentMenuButton.localToScreen(0, studentMenuButton.getHeight()).getY()
            );
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/StudentProfile.fxml",
                "Readora - Student Profile"
        );
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/ChangePassword.fxml",
                "Readora - Change Password"
        );
    }

    @FXML
    private void handleAboutUs(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AboutUsView.fxml",
                "Readora - About Us"
        );
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        boolean confirmed = AlertHelper.confirm(
                "Confirm Logout",
                "Are you sure you want to logout?"
        );

        if (!confirmed) {
            return;
        }

        SessionManager.clearSession();
        SceneNavigator.switchScene(
                event,
                "/view/LoginView.fxml",
                "Readora - Login"
        );
    }
}