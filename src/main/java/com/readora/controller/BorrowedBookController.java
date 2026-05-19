package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AppState;
import com.readora.service.TransitionHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BorrowedBookController {

    @FXML private TextField searchField;

    @FXML private TableView<BorrowRecord> table;
    @FXML private TableColumn<BorrowRecord, String> recordCol;
    @FXML private TableColumn<BorrowRecord, String> bookCol;
    @FXML private TableColumn<BorrowRecord, String> currentBorrowerCol;
    @FXML private TableColumn<BorrowRecord, String> nextInLineCol;
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

        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        currentBorrowerCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudentName())
        );

        nextInLineCol.setCellValueFactory(data ->
                new SimpleStringProperty(getNextInLine(data.getValue()))
        );

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(status.toUpperCase());

                if ("BORROWED".equalsIgnoreCase(status)) {
                    setStyle("-fx-text-fill: #b27920; -fx-font-weight: bold;");
                } else if ("OVERDUE".equalsIgnoreCase(status)) {
                    setStyle("-fx-text-fill: #984545; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #173a5e; -fx-font-weight: bold;");
                }
            }
        });

        table.setPlaceholder(new Label("No active borrowed or overdue records found."));
    }

    private String getNextInLine(BorrowRecord currentRecord) {
        if (currentRecord == null || currentRecord.getBookId() == null) {
            return "No reservation";
        }

        return AppState.getBorrowRecords().stream()
                .filter(record ->
                        record != null
                                && record != currentRecord
                                && record.getBookId() != null
                                && record.getStatus() != null
                                && record.getBookId().equalsIgnoreCase(currentRecord.getBookId())
                                && "RESERVED".equalsIgnoreCase(record.getStatus())
                )
                .sorted((first, second) -> {
                    if (first.getBorrowDate() == null && second.getBorrowDate() == null) return 0;
                    if (first.getBorrowDate() == null) return 1;
                    if (second.getBorrowDate() == null) return -1;
                    return first.getBorrowDate().compareTo(second.getBorrowDate());
                })
                .map(record -> record.getStudentName() == null || record.getStudentName().isBlank()
                        ? "Reserved student"
                        : record.getStudentName())
                .findFirst()
                .orElse("No reservation");
    }

    private void setupTooltips() {
        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search by record ID, book title, current borrower, next reserved student, or status"));
        }

        if (clearButton != null) {
            clearButton.setTooltip(new Tooltip("Clear search field"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(table);

        if (clearButton != null) {
            TransitionHelper.pop(clearButton);
        }
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
                    || contains(record.getStatus(), keyword)
                    || contains(getNextInLine(record), keyword);

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
            resultLabel.setText("No active borrowed or overdue books found.");
        } else if (count == 1) {
            resultLabel.setText("1 active borrowed book found.");
        } else {
            resultLabel.setText(count + " active borrowed books found.");
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}