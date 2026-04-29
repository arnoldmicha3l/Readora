package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BookService;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import com.readora.user.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class BookFormViewController {

    @FXML private Button adminMenuButton;

    @FXML private TextField bookIdField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> statusComboBox;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategoryComboBox;
    @FXML private ComboBox<String> filterStatusComboBox;

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> bookIdColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, String> statusColumn;
    @FXML private TableColumn<Book, String> actionColumn;

    @FXML private HBox popupOverlay;
    @FXML private Label popupTitleLabel;
    @FXML private Label resultLabel;

    private FilteredList<Book> filteredBookList;
    private ContextMenu adminContextMenu;
    private Book editingBook;

    @FXML
    public void initialize() {
        setupAdminMenu();
        setupComboBoxes();
        setupTable();
        setupTooltips();
        loadBooks();
        hidePopup();
        setupAnimations();
    }

    private void setupAdminMenu() {
        adminContextMenu = new ContextMenu();

        MenuItem viewProfileItem = new MenuItem("View Profile");
        MenuItem aboutUsItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        viewProfileItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        adminMenuButton,
                        "/view/AdminProfile.fxml",
                        "Readora - Admin Profile"
                )
        );

        aboutUsItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        adminMenuButton,
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
                    adminMenuButton,
                    "/view/LoginView.fxml",
                    "Readora - Login"
            );
        });

        adminContextMenu.getItems().setAll(viewProfileItem, aboutUsItem, logoutItem);
    }

    private void setupComboBoxes() {
        categoryComboBox.getItems().setAll(
                "Fiction",
                "Non-Fiction",
                "Science",
                "History",
                "Technology",
                "Education"
        );

        statusComboBox.getItems().setAll(
                "Available",
                "Borrowed",
                "Reserved"
        );

        filterCategoryComboBox.getItems().setAll(
                "All",
                "Fiction",
                "Non-Fiction",
                "Science",
                "History",
                "Technology",
                "Education"
        );

        filterStatusComboBox.getItems().setAll(
                "All",
                "Available",
                "Borrowed",
                "Reserved"
        );

        filterCategoryComboBox.setValue("All");
        filterStatusComboBox.setValue("All");
    }

    private void setupTable() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        bookTable.setPlaceholder(new Label("No books found."));

        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actionBox = new HBox(8, editButton, deleteButton);

            {
                actionBox.setAlignment(Pos.CENTER);

                editButton.getStyleClass().add("table-edit-button");
                deleteButton.getStyleClass().add("table-delete-button");

                editButton.setTooltip(new Tooltip("Edit this book"));
                deleteButton.setTooltip(new Tooltip("Delete this book"));

                editButton.setOnAction(event -> {
                    Book selectedBook = getTableView().getItems().get(getIndex());
                    openEditPopup(selectedBook);
                });

                deleteButton.setOnAction(event -> {
                    Book selectedBook = getTableView().getItems().get(getIndex());
                    deleteBook(selectedBook);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setGraphic(actionBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

                if (selectedBook != null) {
                    openEditPopup(selectedBook);
                }
            }
        });
    }

    private void setupTooltips() {
        if (adminMenuButton != null) {
            adminMenuButton.setTooltip(new Tooltip("Open admin menu"));
        }

        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search by book ID, title, author, category, or status"));
        }

        if (filterCategoryComboBox != null) {
            filterCategoryComboBox.setTooltip(new Tooltip("Filter books by category"));
        }

        if (filterStatusComboBox != null) {
            filterStatusComboBox.setTooltip(new Tooltip("Filter books by status"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(bookTable);
        TransitionHelper.pop(adminMenuButton);
    }

    private void loadBooks() {
        AppState.refreshBooks();

        filteredBookList = new FilteredList<>(AppState.getBooks(), book -> true);
        bookTable.setItems(filteredBookList);

        applyFilters();
        bookTable.refresh();
        updateResultLabel();
    }

    @FXML
    private void handleAdminMenu() {
        if (adminMenuButton == null || adminContextMenu == null) {
            AlertHelper.showError("Menu Error", "Admin menu is not available.");
            return;
        }

        TransitionHelper.pulse(adminMenuButton);

        if (adminContextMenu.isShowing()) {
            adminContextMenu.hide();
        } else {
            adminContextMenu.show(
                    adminMenuButton,
                    adminMenuButton.localToScreen(0, adminMenuButton.getHeight()).getX(),
                    adminMenuButton.localToScreen(0, adminMenuButton.getHeight()).getY()
            );
        }
    }

    @FXML
    private void handleOpenAddPopup() {
        editingBook = null;

        popupTitleLabel.setText("Add New Book");
        clearPopupFields();

        bookIdField.setEditable(true);
        showPopup();
        TransitionHelper.pop(popupOverlay);
    }

    private void openEditPopup(Book book) {
        if (book == null) {
            AlertHelper.showWarning("No Book Selected", "Please select a book first.");
            return;
        }

        editingBook = book;

        popupTitleLabel.setText("Edit Book");

        bookIdField.setText(book.getBookId());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryComboBox.setValue(book.getCategory());
        statusComboBox.setValue(book.getStatus());

        bookIdField.setEditable(false);
        showPopup();
        TransitionHelper.pop(popupOverlay);
    }

    @FXML
    private void handleSaveBook() {
        if (!isBookInputValid()) {
            return;
        }

        String bookId = bookIdField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryComboBox.getValue();
        String status = statusComboBox.getValue();

        boolean success;

        if (editingBook == null) {
            Book newBook = new Book(bookId, title, author, category, status);
            success = BookService.addBook(newBook);

            if (!success) {
                AlertHelper.showError(
                        "Save Failed",
                        "Book ID already exists or data is invalid."
                );
                return;
            }
        } else {
            editingBook.setTitle(title);
            editingBook.setAuthor(author);
            editingBook.setCategory(category);
            editingBook.setStatus(status);

            success = BookService.updateBook(editingBook);

            if (!success) {
                AlertHelper.showError(
                        "Update Failed",
                        "Unable to update selected book."
                );
                return;
            }
        }

        AlertHelper.showInfo("Success", "Book saved successfully.");
        hidePopup();
        loadBooks();
        TransitionHelper.pulse(bookTable);
    }

    private boolean isBookInputValid() {
        if (bookIdField.getText() == null || bookIdField.getText().trim().isEmpty()
                || titleField.getText() == null || titleField.getText().trim().isEmpty()
                || authorField.getText() == null || authorField.getText().trim().isEmpty()
                || categoryComboBox.getValue() == null
                || statusComboBox.getValue() == null) {

            AlertHelper.showWarning(
                    "Validation Error",
                    "Please complete all book fields."
            );

            return false;
        }

        return true;
    }

    private void deleteBook(Book book) {
        if (book == null) {
            AlertHelper.showWarning("No Book Selected", "Please select a book first.");
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Delete",
                "Are you sure you want to delete this book?\n\n" + book.getTitle()
        );

        if (!confirmed) {
            return;
        }

        boolean success = BookService.deleteBook(book.getBookId());

        if (success) {
            AlertHelper.showInfo("Deleted", "Book deleted successfully.");
            loadBooks();
            TransitionHelper.pulse(bookTable);
        } else {
            AlertHelper.showError("Delete Failed", "Unable to delete selected book.");
        }
    }

    @FXML
    private void handleCancelPopup() {
        TransitionHelper.fadeOutThenRun(popupOverlay, this::hidePopup);
    }

    @FXML
    private void handleSearch() {
        applyFilters();
    }

    @FXML
    private void handleFilter() {
        applyFilters();
    }

    @FXML
    private void handleClearFilters() {
        if (searchField != null) {
            searchField.clear();
        }

        if (filterCategoryComboBox != null) {
            filterCategoryComboBox.setValue("All");
        }

        if (filterStatusComboBox != null) {
            filterStatusComboBox.setValue("All");
        }

        applyFilters();
        TransitionHelper.pulse(bookTable);
    }

    private void applyFilters() {
        if (filteredBookList == null) {
            return;
        }

        String searchText = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String selectedCategory = filterCategoryComboBox == null || filterCategoryComboBox.getValue() == null
                ? "All"
                : filterCategoryComboBox.getValue();

        String selectedStatus = filterStatusComboBox == null || filterStatusComboBox.getValue() == null
                ? "All"
                : filterStatusComboBox.getValue();

        filteredBookList.setPredicate(book -> {
            boolean matchesSearch = searchText.isEmpty()
                    || contains(book.getBookId(), searchText)
                    || contains(book.getTitle(), searchText)
                    || contains(book.getAuthor(), searchText)
                    || contains(book.getCategory(), searchText)
                    || contains(book.getStatus(), searchText);

            boolean matchesCategory = selectedCategory.equalsIgnoreCase("All")
                    || safeEquals(book.getCategory(), selectedCategory);

            boolean matchesStatus = selectedStatus.equalsIgnoreCase("All")
                    || safeEquals(book.getStatus(), selectedStatus);

            return matchesSearch && matchesCategory && matchesStatus;
        });

        updateResultLabel();
    }

    private void updateResultLabel() {
        if (resultLabel == null || filteredBookList == null) {
            return;
        }

        int count = filteredBookList.size();

        if (count == 0) {
            resultLabel.setText("No books found.");
        } else if (count == 1) {
            resultLabel.setText("1 book found.");
        } else {
            resultLabel.setText(count + " books found.");
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean safeEquals(String value, String compareTo) {
        return value != null && compareTo != null && value.equalsIgnoreCase(compareTo);
    }

    private void showPopup() {
        if (popupOverlay != null) {
            popupOverlay.setOpacity(1.0);
            popupOverlay.setVisible(true);
            popupOverlay.setManaged(true);
        }
    }

    private void hidePopup() {
        clearPopupFields();

        if (popupOverlay != null) {
            popupOverlay.setOpacity(1.0);
            popupOverlay.setVisible(false);
            popupOverlay.setManaged(false);
        }
    }

    private void clearPopupFields() {
        if (bookIdField != null) {
            bookIdField.clear();
            bookIdField.setEditable(true);
        }

        if (titleField != null) {
            titleField.clear();
        }

        if (authorField != null) {
            authorField.clear();
        }

        if (categoryComboBox != null) {
            categoryComboBox.setValue(null);
        }

        if (statusComboBox != null) {
            statusComboBox.setValue(null);
        }

        editingBook = null;
    }

    @FXML
    private void handleGoDashboard(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AdminView.fxml",
                "Readora - Admin Dashboard"
        );
    }

    @FXML
    private void handleBooksTab(ActionEvent event) {
        loadBooks();
        TransitionHelper.pulse(bookTable);
    }

    @FXML
    private void handleMembersTab(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AdminStudentManagement.fxml",
                "Readora - Student Management"
        );
    }

    @FXML
    private void handleBorrowRecordsTab(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/BorrowRecords.fxml",
                "Readora - Borrow Records"
        );
    }

    @FXML
    private void handleReportsTab(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AdminReportsView.fxml",
                "Readora - Reports"
        );
    }
}