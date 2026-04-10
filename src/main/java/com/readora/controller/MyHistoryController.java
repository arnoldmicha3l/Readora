package com.readora.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MyHistoryController {

    @FXML private TextField historySearchField;
    @FXML private ComboBox<String> yearFilter;
    @FXML private TableView<HistoryEntry> historyTable;
    @FXML private TableColumn<HistoryEntry, String> titleCol;
    @FXML private TableColumn<HistoryEntry, String> authorCol;
    @FXML private TableColumn<HistoryEntry, String> borrowDateCol;
    @FXML private TableColumn<HistoryEntry, String> returnDateCol;
    @FXML private TableColumn<HistoryEntry, String> statusCol;
    @FXML private Button studentMenuButton;

    private final ObservableList<HistoryEntry> masterData = FXCollections.observableArrayList();
    private final FilteredList<HistoryEntry> filteredEntries = new FilteredList<>(masterData, entry -> true);
    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();
        historyTable.setPlaceholder(new Label("No history records found."));
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().title()));
        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().author()));
        borrowDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().borrowDate()));
        returnDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().returnDate()));
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().status()));
        yearFilter.setItems(FXCollections.observableArrayList("All", "2026", "2025", "2024"));
        yearFilter.setValue("All");
        yearFilter.setOnAction(event -> applyFilters());
        historySearchField.textProperty().addListener((obs, old, newValue) -> applyFilters());

        historyTable.setItems(filteredEntries);
    }

    private void setupStudentMenu() {
        studentContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> showInfo("Profile", "Student profile details can be added here."));
        settingsItem.setOnAction(event -> showInfo("Settings", "Student settings feature will be added soon."));
        helpItem.setOnAction(event -> showInfo("Help", "Readora Help Center is not yet available."));
        logoutItem.setOnAction(this::handleLogout);

        studentContextMenu.getItems().addAll(viewProfileItem, settingsItem, helpItem, logoutItem);
    }

    private void applyFilters() {
        String searchText = historySearchField.getText() == null ? "" : historySearchField.getText().trim().toLowerCase();
        String selectedYear = yearFilter.getValue();

        filteredEntries.setPredicate(entry -> {
            boolean hasRequiredDates = entry.borrowDate() != null && !entry.borrowDate().isEmpty() &&
                    entry.returnDate() != null && !entry.returnDate().isEmpty();

            boolean matchesSearch = searchText.isEmpty()
                    || entry.title().toLowerCase().contains(searchText)
                    || entry.author().toLowerCase().contains(searchText);

            boolean matchesYear = selectedYear == null || "All".equals(selectedYear)
                    || (entry.borrowDate() != null && entry.borrowDate().startsWith(selectedYear));

            return hasRequiredDates && matchesSearch && matchesYear;
        });
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
    public void handleDashboard(ActionEvent event) {
        navigateTo(event, "/view/StudentView.fxml");
    }

    @FXML
    public void handleBrowse(ActionEvent event) {
        navigateTo(event, "/view/BrowseBooks.fxml");
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
            System.err.println("Could not load FXML at: " + fxmlPath);
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

// Keeping the record at the bottom or moving to a separate file
record HistoryEntry(String title, String author, String borrowDate, String returnDate, String status) {}
