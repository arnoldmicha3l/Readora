package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.AppState;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchBookController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;

    @FXML private TableView<Book> table;
    @FXML private TableColumn<Book, String> idCol;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, String> categoryCol;
    @FXML private TableColumn<Book, String> statusCol;

    private FilteredList<Book> filteredBooks;

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        loadBooks();
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupFilters() {
        if (statusFilter != null) {
            statusFilter.getItems().setAll("All", "Available", "Borrowed", "Reserved");
            statusFilter.setValue("All");
        }
    }

    private void loadBooks() {
        AppState.refreshBooks();
        filteredBooks = new FilteredList<>(AppState.getBooks(), book -> true);
        table.setItems(filteredBooks);
        table.refresh();
    }

    @FXML
    private void handleSearch() {
        applyFilters();
    }

    @FXML
    private void handleClearSearch() {
        if (searchField != null) searchField.clear();
        if (statusFilter != null) statusFilter.setValue("All");
        applyFilters();
    }

    private void applyFilters() {
        String keyword = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String selectedStatus = statusFilter == null ? "All" : statusFilter.getValue();

        filteredBooks.setPredicate(book -> {
            boolean matchesKeyword = keyword.isEmpty()
                    || contains(book.getBookId(), keyword)
                    || contains(book.getTitle(), keyword)
                    || contains(book.getAuthor(), keyword)
                    || contains(book.getCategory(), keyword)
                    || contains(book.getStatus(), keyword);

            boolean matchesStatus = selectedStatus == null
                    || selectedStatus.equalsIgnoreCase("All")
                    || book.getStatus().equalsIgnoreCase(selectedStatus);

            return matchesKeyword && matchesStatus;
        });
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}