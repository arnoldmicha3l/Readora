package com.readora.controller;

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

import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;

public class LibrarianLandingPage {

    @FXML private StackPane contentArea;
    @FXML private Label moduleTitle;
    @FXML private HBox navContainer;
    @FXML private Button dashboardButton;
    @FXML private Button catalogButton;
    @FXML private Button studentsButton;
    @FXML private Button circulationButton;
    @FXML private Button returnsButton;
    @FXML private Button librarianMenuButton;

    private ContextMenu librarianContextMenu;

    @FXML
    public void initialize() {
        setupLibrarianMenu();
        if (librarianMenuButton != null) {
            librarianMenuButton.setText("\u2630");
        }
        Platform.runLater(() -> {
            setActiveNav(dashboardButton);
            loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");
        });
    }

    // This handles the programmatic switching of views
    public void showBorrowingRecords() {
        setActiveNav(circulationButton);
        loadView("BorrowedBookView.fxml", "Circulation Desk");
    }

    public void showReturnBook() {
        setActiveNav(returnsButton);
        loadView("ReturnBookView.fxml", "Process Returns");
    }

    @FXML
    public void openBorrowingRecords(ActionEvent event) {
        showBorrowingRecords();
    }

    @FXML
    public void openReturnBook(ActionEvent event) {
        showReturnBook();
    }

    @FXML
    public void openSearchBook(ActionEvent event) {
        setActiveNav(catalogButton);
        loadView("SearchBookView.fxml", "Catalog");
    }

    @FXML
    public void openManageStudent(ActionEvent event) {
        setActiveNav(studentsButton);
        loadView("ManageMemberView.fxml", "Students");
    }

    @FXML
    public void handleLibrarianMenu() {
        if (librarianContextMenu == null || librarianMenuButton == null) {
            return;
        }

        if (librarianContextMenu.isShowing()) {
            librarianContextMenu.hide();
        } else {
            double x = librarianMenuButton.localToScreen(0, 0).getX();
            double y = librarianMenuButton.localToScreen(0, librarianMenuButton.getHeight()).getY();
            librarianContextMenu.show(librarianMenuButton, x, y);
        }
    }

    private void setupLibrarianMenu() {
        librarianContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> handleViewProfile());
        aboutUsItem.setOnAction(event -> showInfo(
                "About Us",
                "Readora is a Smart Library Management System designed to organize books, members, and circulation tasks."
        ));
        logoutItem.setOnAction(event -> handleLogout());

        librarianContextMenu.getItems().addAll(viewProfileItem, aboutUsItem, logoutItem);
    }

    private void loadView(String fxmlFileName, String titleText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxmlFileName));
            Parent view = loader.load();

            // CRITICAL: Link the dashboard controller to this main controller
            if (fxmlFileName.equals("LibrarianDashboardContent.fxml")) {
                LibrarianDashboardContentController dashboardCtrl = loader.getController();
                if (dashboardCtrl != null) {
                    dashboardCtrl.setMainController(this);
                }
            }

            if (contentArea != null) {
                contentArea.getChildren().setAll(view);
            }
            if (moduleTitle != null) {
                moduleTitle.setText(titleText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Navigation Error", "Could not load " + fxmlFileName);
        }
    }

    @FXML
    public void openDashboard(ActionEvent event) {
        setActiveNav(dashboardButton);
        loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");
    }

    private void setActiveNav(Button activeButton) {
        if (navContainer == null || activeButton == null) {
            return;
        }

        navContainer.getChildren().forEach(node -> {
            if (node instanceof Button button) {
                button.getStyleClass().remove("menu-button-active");
                if (!button.getStyleClass().contains("menu-button")) {
                    button.getStyleClass().add("menu-button");
                }
            }
        });

        activeButton.getStyleClass().remove("menu-button");
        if (!activeButton.getStyleClass().contains("menu-button-active")) {
            activeButton.getStyleClass().add("menu-button-active");
        }
    }

    private void handleViewProfile() {
        UserAccount currentUser = SessionManager.getCurrentUser();
        String fullName = currentUser != null ? currentUser.getFullName() : "Librarian";
        showInfo("Profile", "Logged in as: " + fullName);
    }

    private void handleLogout() {
        SessionManager.clearSession();
        try {
            Stage stage = (Stage) librarianMenuButton.getScene().getWindow();
            SceneNavigator.switchScene(stage, getClass(), "/view/LoginView.fxml", "Readora - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Navigation Error", "Unable to return to login.");
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
