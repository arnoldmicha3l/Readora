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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BrowseBookController {

    @FXML private TextField bookSearchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private TableView<BookModel> booksTable;
    @FXML private TableColumn<BookModel, String> isbnCol;
    @FXML private TableColumn<BookModel, String> titleCol;
    @FXML private TableColumn<BookModel, String> authorCol;
    @FXML private TableColumn<BookModel, String> genreCol;
    @FXML private Button studentMenuButton;

    private final ObservableList<BookModel> bookList = FXCollections.observableArrayList();
    private FilteredList<BookModel> filteredData;
    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();
        isbnCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isbn()));
        titleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().title()));
        authorCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().author()));
        genreCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().genre()));
        genreFilter.setItems(FXCollections.observableArrayList(
                "All Genres", "Technology", "History", "Fiction", "Science"
        ));
        genreFilter.setValue("All Genres");
        filteredData = new FilteredList<>(bookList, p -> true);
        booksTable.setItems(filteredData);
        bookSearchField.textProperty().addListener((obs, old, newValue) -> applyFilters());
        genreFilter.valueProperty().addListener((obs, old, newValue) -> applyFilters());
        loadBooksFromDatabase();
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

    private void loadBooksFromDatabase() {
    }

    @FXML
    private void applyFilters() {
        if (filteredData == null) return;

        String searchText = bookSearchField.getText().toLowerCase().trim();
        String selectedGenre = genreFilter.getValue();

        filteredData.setPredicate(book -> {
            boolean matchesSearch = searchText.isEmpty() ||
                    book.title().toLowerCase().contains(searchText) ||
                    book.author().toLowerCase().contains(searchText) ||
                    book.isbn().contains(searchText);

            boolean matchesGenre = selectedGenre == null ||
                    selectedGenre.equals("All Genres") ||
                    book.genre().equals(selectedGenre);

            return matchesSearch && matchesGenre;
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
    public void handleHistoryTab(ActionEvent event) {
        navigateTo(event, "/view/MyHistory.fxml");
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
            var resource = getClass().getResource(fxmlPath);
            if (resource == null) throw new IOException("FXML not found: " + fxmlPath);

            Parent root = FXMLLoader.load(resource);
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
            System.err.println("Navigation error: " + e.getMessage());
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public record BookModel(String isbn, String title, String author, String genre) {}
}
