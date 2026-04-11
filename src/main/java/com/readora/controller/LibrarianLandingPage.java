package com.readora.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class LibrarianLandingPage {

    @FXML private StackPane contentArea;
    @FXML private HBox navContainer;
    @FXML private Label moduleTitle;

    private final String ACTIVE_STYLE = "-fx-background-color: #E0F2FE; -fx-text-fill: #0369A1; -fx-background-radius: 10; -fx-padding: 10 20; -fx-font-weight: bold;";
    private final String IDLE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-padding: 10 20; -fx-font-weight: normal;";

    /**
     * This method runs when the FXML is first loaded.
     * Platform.runLater prevents crashes by waiting for the UI to be ready.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Load the dashboard by default so the screen isn't empty
            loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");

            // Optional: If you have an fx:id for your dashboard button, highlight it here
            // e.g., dashboardBtn.setStyle(ACTIVE_STYLE);
        });
    }

    @FXML
    public void openDashboard(ActionEvent event) {
        updateNavStyle((Button) event.getSource());
        loadView("LibrarianDashboardContent.fxml", "Librarian Dashboard");
    }

    @FXML public void openSearchBook(ActionEvent event) { handleMenuClick((Button) event.getSource(), "SearchBookView.fxml", "Catalog Management"); }
    @FXML public void openManageMember(ActionEvent event) { handleMenuClick((Button) event.getSource(), "ManageMemberView.fxml", "Member Directory"); }
    @FXML public void openBorrowingRecords(ActionEvent event) { handleMenuClick((Button) event.getSource(), "BorrowedBookView.fxml", "Circulation Desk"); }

    /**
     * Added @FXML to fix the "Cannot resolve symbol" error in your FXML file
     */
    @FXML
    public void openReturnBook(ActionEvent event) {
        loadView("ReturnBookView.fxml", "Process Returns");
    }

    private void handleMenuClick(Button clickedButton, String fxmlName, String title) {
        updateNavStyle(clickedButton);
        loadView(fxmlName, title);
    }

    private void updateNavStyle(Button clickedButton) {
        // Null check to prevent crashes during initialization
        if (navContainer != null) {
            navContainer.getChildren().forEach(node -> {
                if (node instanceof Button) node.setStyle(IDLE_STYLE);
            });
            clickedButton.setStyle(ACTIVE_STYLE);
        }
    }

    private void loadView(String fxmlFileName, String titleText) {
        try {
            // Ensure the path "/view/" matches your resources folder structure exactly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxmlFileName));
            Parent view = loader.load();

            if (contentArea != null) {
                contentArea.getChildren().setAll(view);
            }
            if (moduleTitle != null) {
                moduleTitle.setText(titleText);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not load FXML file: " + fxmlFileName);
            e.printStackTrace();
        }
    }
}