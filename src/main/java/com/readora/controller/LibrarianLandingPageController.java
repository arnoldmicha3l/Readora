package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import com.readora.service.ViewLoaderService;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LibrarianLandingPageController {

    @FXML private StackPane contentArea;
    @FXML private Button librarianMenuButton;

    private ContextMenu librarianMenu;

    @FXML
    public void initialize() {
        setupMenu();
        openDashboard(null);
        setupAnimations();
    }

    private void setupMenu() {
        librarianMenu = new ContextMenu();

        MenuItem profile = new MenuItem("Librarian Profile");
        MenuItem about = new MenuItem("About Us");
        MenuItem logout = new MenuItem("Logout");

        profile.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        librarianMenuButton,
                        "/view/LibrarianProfile.fxml",
                        "Readora - Librarian Profile"
                )
        );

        about.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        librarianMenuButton,
                        "/view/AboutUsView.fxml",
                        "Readora - About Us"
                )
        );

        logout.setOnAction(event -> {
            boolean confirmed = AlertHelper.confirm(
                    "Confirm Logout",
                    "Are you sure you want to logout?"
            );

            if (!confirmed) {
                return;
            }

            SessionManager.clearSession();

            SceneNavigator.switchSceneFromNode(
                    librarianMenuButton,
                    "/view/LoginView.fxml",
                    "Readora - Login"
            );
        });

        librarianMenu.getItems().setAll(profile, about, logout);

        if (librarianMenuButton != null) {
            librarianMenuButton.setTooltip(new Tooltip("Open librarian menu"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.pop(librarianMenuButton);
    }

    @FXML
    private void openDashboard(ActionEvent event) {
        loadDashboardContent();
    }

    private void loadDashboardContent() {
        if (contentArea == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LibrarianDashboardContent.fxml"));
            Parent view = loader.load();

            Button openBorrowedButton = (Button) view.lookup("#openBorrowedButton");
            Button openReturnButton = (Button) view.lookup("#openReturnButton");

            if (openBorrowedButton != null) {
                openBorrowedButton.setOnAction(event -> openBorrowingRecords(event));
                openBorrowedButton.setTooltip(new Tooltip("Open borrowed book records"));
                TransitionHelper.pop(openBorrowedButton);
            }

            if (openReturnButton != null) {
                openReturnButton.setOnAction(event -> openReturnBook(event));
                openReturnButton.setTooltip(new Tooltip("Open return book module"));
                TransitionHelper.pop(openReturnButton);
            }

            contentArea.getChildren().setAll(view);
            TransitionHelper.pageLoad(view);

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Loading Error", "Unable to load Librarian Dashboard.");
        }
    }

    @FXML
    private void openSearchBook(ActionEvent event) {
        ViewLoaderService.loadInto(contentArea, getClass(), "SearchBookView.fxml");
    }

    @FXML
    private void openManageStudent(ActionEvent event) {
        ViewLoaderService.loadInto(contentArea, getClass(), "ManageMemberView.fxml");
    }

    @FXML
    private void openBorrowingRecords(ActionEvent event) {
        ViewLoaderService.loadInto(contentArea, getClass(), "BorrowedBookView.fxml");
    }

    @FXML
    private void openReturnBook(ActionEvent event) {
        ViewLoaderService.loadInto(contentArea, getClass(), "ReturnBookView.fxml");
    }

    @FXML
    private void handleLibrarianMenu(ActionEvent event) {
        if (librarianMenuButton == null || librarianMenu == null) {
            AlertHelper.showError("Menu Error", "Librarian menu is not available.");
            return;
        }

        TransitionHelper.pulse(librarianMenuButton);

        if (librarianMenu.isShowing()) {
            librarianMenu.hide();
        } else {
            librarianMenu.show(
                    librarianMenuButton,
                    librarianMenuButton.localToScreen(0, librarianMenuButton.getHeight()).getX(),
                    librarianMenuButton.localToScreen(0, librarianMenuButton.getHeight()).getY()
            );
        }
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
        SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
    }
}