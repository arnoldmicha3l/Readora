package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import com.readora.user.SessionManager;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BrowseBooksController {

    @FXML private TextField bookSearchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> isbnCol;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, String> genreCol;
    @FXML private TableColumn<Book, String> statusCol;

    @FXML private Button studentMenuButton;

    private ContextMenu studentMenu;
    private FilteredList<Book> filteredBooks;

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        setupStudentMenu();
        setupTooltips();
        loadBooks();
        setupAnimations();
    }

    private void setupTable() {
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        booksTable.setPlaceholder(new Label("No books found."));

        booksTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                borrowSelectedBook();
            }
        });
    }

    private void setupFilters() {
        if (genreFilter != null) {
            genreFilter.getItems().setAll(
                    "All Genres",
                    "Fiction",
                    "Non-Fiction",
                    "Science",
                    "Technology",
                    "History",
                    "Education"
            );
            genreFilter.setValue("All Genres");
        }

        if (statusFilter != null) {
            statusFilter.getItems().setAll(
                    "All",
                    "Available",
                    "Borrowed",
                    "Reserved"
            );
            statusFilter.setValue("All");
        }
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
    }

    private void setupTooltips() {
        if (studentMenuButton != null) {
            studentMenuButton.setTooltip(new Tooltip("Open student menu"));
        }

        if (bookSearchField != null) {
            bookSearchField.setTooltip(new Tooltip("Search by book ID, title, author, category, or status"));
        }

        if (genreFilter != null) {
            genreFilter.setTooltip(new Tooltip("Filter books by category"));
        }

        if (statusFilter != null) {
            statusFilter.setTooltip(new Tooltip("Filter books by availability status"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(booksTable);
        TransitionHelper.pop(studentMenuButton);
    }

    private void loadBooks() {
        AppState.refreshBooks();

        filteredBooks = new FilteredList<>(AppState.getBooks(), book -> true);
        booksTable.setItems(filteredBooks);

        applyFilters();
        booksTable.refresh();
    }

    @FXML
    private void applyFilters() {
        if (filteredBooks == null) {
            return;
        }

        String keyword = bookSearchField == null || bookSearchField.getText() == null
                ? ""
                : bookSearchField.getText().trim().toLowerCase();

        String selectedGenre = genreFilter == null || genreFilter.getValue() == null
                ? "All Genres"
                : genreFilter.getValue();

        String selectedStatus = statusFilter == null || statusFilter.getValue() == null
                ? "All"
                : statusFilter.getValue();

        filteredBooks.setPredicate(book -> {
            boolean matchesKeyword = keyword.isEmpty()
                    || contains(book.getBookId(), keyword)
                    || contains(book.getTitle(), keyword)
                    || contains(book.getAuthor(), keyword)
                    || contains(book.getCategory(), keyword)
                    || contains(book.getStatus(), keyword);

            boolean matchesGenre = selectedGenre.equalsIgnoreCase("All Genres")
                    || safeEquals(book.getCategory(), selectedGenre);

            boolean matchesStatus = selectedStatus.equalsIgnoreCase("All")
                    || safeEquals(book.getStatus(), selectedStatus);

            return matchesKeyword && matchesGenre && matchesStatus;
        });
    }

    @FXML
    private void handleClearFilters() {
        if (bookSearchField != null) {
            bookSearchField.clear();
        }

        if (genreFilter != null) {
            genreFilter.setValue("All Genres");
        }

        if (statusFilter != null) {
            statusFilter.setValue("All");
        }

        applyFilters();
        TransitionHelper.pulse(booksTable);
    }

    @FXML
    private void handleBorrowBook(ActionEvent event) {
        borrowSelectedBook();
    }

    private void borrowSelectedBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            AlertHelper.showWarning(
                    "No Book Selected",
                    "Please select a book first."
            );
            return;
        }

        if (!"Available".equalsIgnoreCase(selectedBook.getStatus())) {
            AlertHelper.showWarning(
                    "Unavailable Book",
                    "This book is not available for borrowing."
            );
            return;
        }

        if (SessionManager.getCurrentUser() == null) {
            AlertHelper.showError(
                    "Session Error",
                    "No active student session found. Please login again."
            );
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Borrow",
                "Borrow this book?\n\n" + selectedBook.getTitle()
        );

        if (!confirmed) {
            return;
        }

        boolean success = BorrowingService.borrowBook(
                SessionManager.getCurrentUser(),
                selectedBook
        );

        if (success) {
            AlertHelper.showInfo(
                    "Borrow Successful",
                    "You have successfully borrowed: " + selectedBook.getTitle()
            );

            AppState.refreshAll();
            loadBooks();
            TransitionHelper.pulse(booksTable);
        } else {
            AlertHelper.showError(
                    "Borrow Failed",
                    "Unable to borrow this book. Please try again."
            );
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean safeEquals(String value, String compareTo) {
        return value != null && compareTo != null && value.equalsIgnoreCase(compareTo);
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/StudentView.fxml",
                "Readora - Student Dashboard"
        );
    }

    @FXML
    private void handleBrowseBooks(ActionEvent event) {
        loadBooks();
        TransitionHelper.pulse(booksTable);
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

        TransitionHelper.pulse(studentMenuButton);

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