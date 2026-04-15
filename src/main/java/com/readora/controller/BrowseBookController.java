package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

public class BrowseBookController {

    @FXML private TextField bookSearchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> isbnCol;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, String> genreCol;
    @FXML private Button studentMenuButton;

    private FilteredList<Book> filteredData;
    private ContextMenu studentContextMenu;

    @FXML
    public void initialize() {
        setupStudentMenu();

        isbnCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBookId()));
        titleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
        authorCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAuthor()));
        genreCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));

        genreFilter.setItems(FXCollections.observableArrayList(
                "All Genres", "Fiction", "Non-Fiction", "Science", "History", "Technology", "Education"
        ));
        genreFilter.setValue("All Genres");

        filteredData = new FilteredList<>(AppState.getBooks(), book -> true);
        booksTable.setItems(filteredData);

        bookSearchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        genreFilter.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
    }

    private void setupStudentMenu() {
        studentContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event -> showInfo("Profile", "Student profile details can be added here."));
        settingsItem.setOnAction(event -> showInfo("Settings", "Student settings feature will be added soon."));
        aboutUsItem.setOnAction(event -> handleAboutUs());
        logoutItem.setOnAction(event -> handleLogout());

        studentContextMenu.getItems().addAll(viewProfileItem, settingsItem, aboutUsItem, logoutItem);
    }

    private void handleAboutUs() {
        showInfo(
                "About Us",
                "Readora is a Smart Library Management System designed to help manage books, borrowing records, and student library services in a simple and organized way."
        );
    }

    @FXML
    private void applyFilters() {
        if (filteredData == null) {
            return;
        }

        String searchText = bookSearchField.getText() == null ? "" : bookSearchField.getText().toLowerCase().trim();
        String selectedGenre = genreFilter.getValue();

        filteredData.setPredicate(book -> {
            boolean matchesSearch = searchText.isEmpty()
                    || book.getTitle().toLowerCase().contains(searchText)
                    || book.getAuthor().toLowerCase().contains(searchText)
                    || book.getBookId().toLowerCase().contains(searchText);

            boolean matchesGenre = selectedGenre == null
                    || selectedGenre.equals("All Genres")
                    || book.getCategory().equalsIgnoreCase(selectedGenre);

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
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/StudentView.fxml", "Readora - Student Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open the student dashboard.");
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