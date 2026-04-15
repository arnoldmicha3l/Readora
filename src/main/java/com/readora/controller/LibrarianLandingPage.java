package com.readora.controller;

import com.readora.user.UserAccount;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarianLandingPage {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox navContainer;

    @FXML
    private Label moduleTitle;

    @FXML
    private Button librarianMenuButton;

    private ContextMenu librarianContextMenu;

    private final String ACTIVE_STYLE =
            "-fx-background-color: #E0F2FE; -fx-text-fill: #0369A1; -fx-background-radius: 10; -fx-padding: 10 20; -fx-font-weight: bold;";

    private final String IDLE_STYLE =
            "-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-padding: 10 20; -fx-font-weight: normal;";

    @FXML
    public void initialize() {
        setupLibrarianMenu();

        Platform.runLater(() -> {
            loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");

            if (navContainer != null && !navContainer.getChildren().isEmpty()) {
                if (navContainer.getChildren().get(0) instanceof Button dashboardButton) {
                    updateNavStyle(dashboardButton);
                }
            }
        });
    }

    private void setupLibrarianMenu() {
        librarianContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> handleViewProfile());
        settingsItem.setOnAction(event -> handleSettings());
        aboutUsItem.setOnAction(event -> handleAboutUs());
        logoutItem.setOnAction(event -> handleLogout());

        librarianContextMenu.getItems().addAll(
                viewProfileItem,
                settingsItem,
                aboutUsItem,
                logoutItem
        );
    }

    @FXML
    public void handleLibrarianMenu() {
        if (librarianContextMenu.isShowing()) {
            librarianContextMenu.hide();
        } else {
            double x = librarianMenuButton.localToScreen(0, 0).getX();
            double y = librarianMenuButton.localToScreen(0, librarianMenuButton.getHeight()).getY();
            librarianContextMenu.show(librarianMenuButton, x, y);
        }
    }

    @FXML
    public void openDashboard(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");
    }

    @FXML
    public void openSearchBook(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("SearchBookView.fxml", "Catalog Management");
    }

    @FXML
    public void openManageStudent(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("ManageMemberView.fxml", "Student Directory");
    }

    @FXML
    public void openBorrowingRecords(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("BorrowedBookView.fxml", "Circulation Desk");
    }

    @FXML
    public void openReturnBook(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("ReturnBookView.fxml", "Process Returns");
    }

    private void updateNavStyle(Button clickedButton) {
        if (navContainer == null) {
            return;
        }

        navContainer.getChildren().forEach(node -> {
            if (node instanceof Button button) {
                button.setStyle(IDLE_STYLE);
            }
        });

        clickedButton.setStyle(ACTIVE_STYLE);
    }

    private void loadView(String fxmlFileName, String titleText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxmlFileName));
            Parent view = loader.load();

            if (contentArea != null) {
                contentArea.getChildren().setAll(view);
            }

            if (moduleTitle != null) {
                moduleTitle.setText(titleText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Could not load " + fxmlFileName);
        }
    }

    private void handleViewProfile() {
        UserAccount currentUser = SessionManager.getCurrentUser();
        String fullName = currentUser != null ? currentUser.getFullName() : "Librarian User";
        showInfo("Profile", "Logged in as: " + fullName);
    }

    private void handleSettings() {
        showInfo("Settings", "Librarian settings feature will be added soon.");
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
            Stage stage = (Stage) librarianMenuButton.getScene().getWindow();
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