package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.user.UserAccount;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
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

    private final ObservableList<HistoryEntry> masterHistory = FXCollections.observableArrayList();
    private FilteredList<HistoryEntry> filteredEntries;
    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();

        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().title()));
        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().author()));
        borrowDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().borrowDate()));
        returnDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().returnDate()));
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().status()));

        yearFilter.getItems().setAll("All", "2026", "2025", "2024");
        yearFilter.setValue("All");

        filteredEntries = new FilteredList<>(masterHistory, entry -> true);
        historyTable.setItems(filteredEntries);

        loadHistory();

        yearFilter.setOnAction(event -> applyFilters());
        historySearchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());
    }

    private void loadHistory() {
        masterHistory.clear();

        UserAccount currentUser = SessionManager.getCurrentUser();
        String fullName = currentUser != null ? currentUser.getFullName() : "";

        for (BorrowRecord record : AppState.getBorrowRecords()) {
            if (record.getStudentName().equalsIgnoreCase(fullName)) {
                masterHistory.add(new HistoryEntry(
                        record.getBookTitle(),
                        findAuthor(record.getBookTitle()),
                        record.getBorrowDate() != null ? record.getBorrowDate().toString() : "",
                        record.getReturnDate() != null ? record.getReturnDate().toString() : "",
                        record.getStatus()
                ));
            }
        }

        applyFilters();
    }

    private String findAuthor(String bookTitle) {
        return AppState.getBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(bookTitle))
                .map(book -> book.getAuthor())
                .findFirst()
                .orElse("Unknown Author");
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
        logoutItem.setOnAction(event -> handleLogout());

        studentContextMenu.getItems().addAll(viewProfileItem, settingsItem, helpItem, logoutItem);
    }

    private void applyFilters() {
        String searchText = historySearchField.getText() == null
                ? ""
                : historySearchField.getText().trim().toLowerCase();
        String selectedYear = yearFilter.getValue();

        filteredEntries.setPredicate(entry -> {
            boolean matchesSearch = searchText.isEmpty()
                    || entry.title().toLowerCase().contains(searchText)
                    || entry.author().toLowerCase().contains(searchText);

            boolean matchesYear = selectedYear == null
                    || "All".equals(selectedYear)
                    || (!entry.borrowDate().isEmpty() && entry.borrowDate().startsWith(selectedYear));

            return matchesSearch && matchesYear;
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
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/StudentView.fxml", "Readora - Student Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open student dashboard.");
        }
    }

    @FXML
    public void handleBrowse(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/BrowseBooks.fxml", "Readora - Browse Books");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open Browse Books.");
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

    public record HistoryEntry(String title, String author, String borrowDate, String returnDate, String status) {
    }
}