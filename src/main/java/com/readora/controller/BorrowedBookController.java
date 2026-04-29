package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AppState;
import com.readora.service.TransitionHelper;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BorrowedBookController {

    @FXML private TextField searchField;

    @FXML private TableView<BorrowRecord> table;
    @FXML private TableColumn<BorrowRecord, String> recordCol;
    @FXML private TableColumn<BorrowRecord, String> studentCol;
    @FXML private TableColumn<BorrowRecord, String> bookCol;
    @FXML private TableColumn<BorrowRecord, String> statusCol;

    @FXML private Label resultLabel;
    @FXML private Button clearButton;

    private FilteredList<BorrowRecord> filteredRecords;

    @FXML
    public void initialize() {
        setupTable();
        setupTooltips();
        loadRecords();
        setupAnimations();
    }

    private void setupTable() {
        if (recordCol != null) {
            recordCol.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        }

        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.setPlaceholder(new Label("No active borrowed or overdue records found."));
    }

    private void setupTooltips() {
        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search by record ID, student name, book title, or status"));
        }

        if (clearButton != null) {
            clearButton.setTooltip(new Tooltip("Clear search field"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(table);
        TransitionHelper.pop(clearButton);
    }

    private void loadRecords() {
        AppState.refreshBorrowRecords();

        filteredRecords = new FilteredList<>(AppState.getBorrowRecords(), this::isActiveBorrowRecord);
        table.setItems(filteredRecords);
        table.refresh();

        updateResultLabel();
    }

    private boolean isActiveBorrowRecord(BorrowRecord record) {
        if (record == null || record.getStatus() == null) {
            return false;
        }

        return "BORROWED".equalsIgnoreCase(record.getStatus())
                || "OVERDUE".equalsIgnoreCase(record.getStatus());
    }

    @FXML
    private void handleSearch() {
        if (filteredRecords == null) {
            return;
        }

        String keyword = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        filteredRecords.setPredicate(record -> {
            boolean active = isActiveBorrowRecord(record);

            boolean matchesKeyword = keyword.isEmpty()
                    || contains(record.getRecordId(), keyword)
                    || contains(record.getStudentName(), keyword)
                    || contains(record.getBookTitle(), keyword)
                    || contains(record.getStatus(), keyword);

            return active && matchesKeyword;
        });

        updateResultLabel();
    }

    @FXML
    private void handleClearSearch() {
        if (searchField != null) {
            searchField.clear();
        }

        handleSearch();
        TransitionHelper.pulse(table);
    }

    private void updateResultLabel() {
        if (resultLabel == null || filteredRecords == null) {
            return;
        }

        int count = filteredRecords.size();

        if (count == 0) {
            resultLabel.setText("No active borrowed or overdue records found.");
        } else if (count == 1) {
            resultLabel.setText("1 active record found.");
        } else {
            resultLabel.setText(count + " active records found.");
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}