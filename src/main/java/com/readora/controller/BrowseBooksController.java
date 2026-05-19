package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BookService;
import com.readora.service.BorrowingService;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import com.readora.user.SessionManager;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class BrowseBooksController {

    @FXML private TextField bookSearchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML private FlowPane booksFlowPane;
    @FXML private Label bookCountLabel;

    @FXML private HBox bookDetailsOverlay;
    @FXML private ImageView detailsCoverImage;
    @FXML private Label detailsTitleLabel;
    @FXML private Label detailsAuthorLabel;
    @FXML private Label detailsCategoryLabel;
    @FXML private Label detailsStatusLabel;
    @FXML private Button detailsBorrowButton;

    @FXML private Button studentMenuButton;

    private ContextMenu studentMenu;
    private FilteredList<Book> filteredBooks;
    private Book selectedBookForDetails;

    @FXML
    public void initialize() {
        setupFilters();
        setupStudentMenu();
        setupTooltips();
        loadBooks();
        hideBookDetails();
        setupAnimations();
    }

    private void setupFilters() {
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

        statusFilter.getItems().setAll(
                "All",
                "Available",
                "Borrowed",
                "Reserved"
        );
        statusFilter.setValue("All");
    }

    private void loadBooks() {
        AppState.refreshBooks();
        filteredBooks = new FilteredList<>(AppState.getBooks(), book -> true);
        applyFilters();
    }

    @FXML
    private void applyFilters() {
        if (filteredBooks == null) return;

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

        renderBookCards();
    }

    private void renderBookCards() {
        booksFlowPane.getChildren().clear();

        if (filteredBooks == null || filteredBooks.isEmpty()) {
            Label emptyLabel = new Label("No books found.");
            emptyLabel.getStyleClass().add("welcome-subtitle");
            booksFlowPane.getChildren().add(emptyLabel);

            if (bookCountLabel != null) {
                bookCountLabel.setText("No books found.");
            }

            return;
        }

        for (Book book : filteredBooks) {
            booksFlowPane.getChildren().add(createBookCard(book));
        }

        if (bookCountLabel != null) {
            int count = filteredBooks.size();
            bookCountLabel.setText(count == 1 ? "1 book available." : count + " books available.");
        }
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.getStyleClass().add("book-card");

        ImageView cover = new ImageView();
        cover.setFitWidth(130);
        cover.setFitHeight(190);
        cover.setPreserveRatio(true);
        cover.getStyleClass().add("book-card-cover");
        setCoverImage(cover, book.getCoverPath());

        Label title = new Label(book.getTitle());
        title.setWrapText(true);
        title.setMaxWidth(150);
        title.getStyleClass().add("book-card-title");

        Label author = new Label(book.getAuthor());
        author.setWrapText(true);
        author.setMaxWidth(150);
        author.getStyleClass().add("book-card-author");

        Label status = new Label(book.getStatus());
        status.getStyleClass().add(getStatusStyle(book.getStatus()));

        Button viewButton = new Button("View Details");
        viewButton.getStyleClass().add("secondary-action-button");
        viewButton.setOnAction(event -> openBookDetails(book));

        card.getChildren().addAll(cover, title, author, status, viewButton);
        card.setOnMouseClicked(event -> openBookDetails(book));

        return card;
    }

    private void openBookDetails(Book book) {
        if (book == null) return;

        selectedBookForDetails = book;

        setCoverImage(detailsCoverImage, book.getCoverPath());

        detailsTitleLabel.setText(book.getTitle());
        detailsAuthorLabel.setText("by " + book.getAuthor());
        detailsCategoryLabel.setText("Category: " + book.getCategory());
        detailsStatusLabel.setText("Status: " + book.getStatus());

        if ("Available".equalsIgnoreCase(book.getStatus())) {
            detailsBorrowButton.setText("Borrow Book");
            detailsBorrowButton.setDisable(false);
        } else if ("Borrowed".equalsIgnoreCase(book.getStatus())) {
            detailsBorrowButton.setText("Reserve Book");
            detailsBorrowButton.setDisable(false);
        } else if ("Reserved".equalsIgnoreCase(book.getStatus())) {
            detailsBorrowButton.setText("Already Reserved");
            detailsBorrowButton.setDisable(true);
        } else {
            detailsBorrowButton.setText("Not Available");
            detailsBorrowButton.setDisable(true);
        }

        bookDetailsOverlay.setOpacity(1.0);
        bookDetailsOverlay.setVisible(true);
        bookDetailsOverlay.setManaged(true);

        TransitionHelper.pop(bookDetailsOverlay);
    }

    @FXML
    private void handleCloseBookDetails() {
        hideBookDetails();
    }

    private void hideBookDetails() {
        selectedBookForDetails = null;

        if (bookDetailsOverlay != null) {
            bookDetailsOverlay.setOpacity(1.0);
            bookDetailsOverlay.setVisible(false);
            bookDetailsOverlay.setManaged(false);
        }
    }

    @FXML
    private void handleBorrowFromDetails() {
        if (selectedBookForDetails == null) {
            AlertHelper.showWarning("No Book Selected", "Please select a book first.");
            return;
        }

        if ("Available".equalsIgnoreCase(selectedBookForDetails.getStatus())) {
            borrowBook(selectedBookForDetails);
            return;
        }

        if ("Borrowed".equalsIgnoreCase(selectedBookForDetails.getStatus())) {
            reserveBook(selectedBookForDetails);
            return;
        }

        AlertHelper.showWarning(
                "Unavailable Book",
                "This book is already reserved or unavailable."
        );
    }

    @FXML
    private void handleBorrowBook(ActionEvent event) {
        if (selectedBookForDetails == null) {
            AlertHelper.showWarning(
                    "Select a Book",
                    "Please click a book cover first, then choose an action from the details popup."
            );
            return;
        }

        handleBorrowFromDetails();
    }

    private void borrowBook(Book selectedBook) {
        if (SessionManager.getCurrentUser() == null) {
            AlertHelper.showError("Session Error", "No active student session found. Please login again.");
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Borrow",
                "Borrow this book?\n\n" + selectedBook.getTitle()
        );

        if (!confirmed) return;

        boolean success = BorrowingService.borrowBook(
                SessionManager.getCurrentUser(),
                selectedBook
        );

        if (success) {
            AlertHelper.showInfo(
                    "Borrow Successful",
                    "You have successfully borrowed: " + selectedBook.getTitle()
            );

            hideBookDetails();
            AppState.refreshAll();
            loadBooks();

            if (booksFlowPane != null) {
                TransitionHelper.pulse(booksFlowPane);
            }
        } else {
            AlertHelper.showError("Borrow Failed", "Unable to borrow this book. Please try again.");
        }
    }

    private void reserveBook(Book selectedBook) {
        boolean confirmed = AlertHelper.confirm(
                "Confirm Reservation",
                "This book is currently borrowed by another student.\n\nReserve this book?\n\n" + selectedBook.getTitle()
        );

        if (!confirmed) return;

        selectedBook.setStatus("Reserved");

        boolean success = BookService.updateBook(selectedBook);

        if (success) {
            AlertHelper.showInfo(
                    "Book Reserved",
                    "You have successfully reserved: " + selectedBook.getTitle()
            );

            hideBookDetails();
            AppState.refreshAll();
            loadBooks();

            if (booksFlowPane != null) {
                TransitionHelper.pulse(booksFlowPane);
            }
        } else {
            AlertHelper.showError(
                    "Reservation Failed",
                    "Unable to reserve this book. Please try again."
            );
        }
    }

    private String getStatusStyle(String status) {
        if ("Available".equalsIgnoreCase(status)) {
            return "book-status-available";
        }

        if ("Borrowed".equalsIgnoreCase(status)) {
            return "book-status-borrowed";
        }

        if ("Reserved".equalsIgnoreCase(status)) {
            return "book-status-reserved";
        }

        return "book-status-unavailable";
    }

    private void setCoverImage(ImageView imageView, String coverPath) {
        if (imageView == null) return;

        if (coverPath == null || coverPath.isBlank()) {
            imageView.setImage(null);
            return;
        }

        File file = new File(coverPath);

        if (!file.exists()) {
            imageView.setImage(null);
            return;
        }

        imageView.setImage(new Image(file.toURI().toString(), true));
    }



    @FXML
    private void handleClearFilters() {
        if (bookSearchField != null) bookSearchField.clear();
        if (genreFilter != null) genreFilter.setValue("All Genres");
        if (statusFilter != null) statusFilter.setValue("All");

        applyFilters();

        if (booksFlowPane != null) {
            TransitionHelper.pulse(booksFlowPane);
        }
    }

    private void setupStudentMenu() {
        studentMenu = new ContextMenu();

        MenuItem profileItem = new MenuItem("View Profile");
        MenuItem passwordItem = new MenuItem("Change Password");
        MenuItem aboutItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        profileItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(studentMenuButton, "/view/StudentProfile.fxml", "Readora - Student Profile")
        );

        passwordItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(studentMenuButton, "/view/ChangePassword.fxml", "Readora - Change Password")
        );

        aboutItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(studentMenuButton, "/view/AboutUsView.fxml", "Readora - About Us")
        );

        logoutItem.setOnAction(event -> {
            boolean confirmed = AlertHelper.confirm("Confirm Logout", "Are you sure you want to logout?");
            if (!confirmed) return;

            SessionManager.clearSession();
            SceneNavigator.switchSceneFromNode(studentMenuButton, "/view/LoginView.fxml", "Readora - Login");
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
        if (booksFlowPane != null) TransitionHelper.softLoad(booksFlowPane);
        if (studentMenuButton != null) TransitionHelper.pop(studentMenuButton);
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean safeEquals(String value, String compareTo) {
        return value != null && compareTo != null && value.equalsIgnoreCase(compareTo);
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/StudentView.fxml", "Readora - Student Dashboard");
    }

    @FXML
    private void handleBrowseBooks(ActionEvent event) {
        loadBooks();

        if (booksFlowPane != null) {
            TransitionHelper.pulse(booksFlowPane);
        }
    }

    @FXML
    private void handleHistoryTab(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/MyHistory.fxml", "Readora - My History");
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
        SceneNavigator.switchScene(event, "/view/StudentProfile.fxml", "Readora - Student Profile");
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/ChangePassword.fxml", "Readora - Change Password");
    }

    @FXML
    private void handleAboutUs(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AboutUsView.fxml", "Readora - About Us");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        boolean confirmed = AlertHelper.confirm("Confirm Logout", "Are you sure you want to logout?");
        if (!confirmed) return;

        SessionManager.clearSession();
        SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
    }
}