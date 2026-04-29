package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import com.readora.service.TransitionHelper;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReturnBookController {

    @FXML private TextField searchField;

    @FXML private TableView<BorrowRecord> table;
    @FXML private TableColumn<BorrowRecord, String> recordCol;
    @FXML private TableColumn<BorrowRecord, String> studentCol;
    @FXML private TableColumn<BorrowRecord, String> bookCol;
    @FXML private TableColumn<BorrowRecord, String> statusCol;

    @FXML private Label selectedRecordLabel;
    @FXML private Label resultLabel;

    @FXML private Button returnButton;
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

        table.setPlaceholder(new Label("No records available for return."));

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                setSelectedRecordText("No record selected.");
            } else {
                setSelectedRecordText(
                        "Selected: " + selected.getBookTitle()
                                + " borrowed by "
                                + selected.getStudentName()
                );
            }
        });
    }

    private void setupTooltips() {
        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search records available for return"));
        }

        if (returnButton != null) {
            returnButton.setTooltip(new Tooltip("Mark selected book as returned"));
        }

        if (clearButton != null) {
            clearButton.setTooltip(new Tooltip("Clear search field"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(table);
        TransitionHelper.pop(returnButton);
        TransitionHelper.pop(clearButton);
    }

    private void loadRecords() {
        AppState.refreshBorrowRecords();

        filteredRecords = new FilteredList<>(AppState.getBorrowRecords(), this::isReturnableRecord);
        table.setItems(filteredRecords);
        table.refresh();

        setSelectedRecordText("No record selected.");
        updateResultLabel();
    }

    private boolean isReturnableRecord(BorrowRecord record) {
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
            boolean returnable = isReturnableRecord(record);

            boolean matchesKeyword = keyword.isEmpty()
                    || contains(record.getRecordId(), keyword)
                    || contains(record.getStudentName(), keyword)
                    || contains(record.getBookTitle(), keyword)
                    || contains(record.getStatus(), keyword);

            return returnable && matchesKeyword;
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

    @FXML
    private void handleReturn() {
        BorrowRecord selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertHelper.showWarning(
                    "No Record Selected",
                    "Please select a borrow record first."
            );
            return;
        }

        if ("RETURNED".equalsIgnoreCase(selected.getStatus())) {
            AlertHelper.showInfo(
                    "Already Returned",
                    "This book has already been returned."
            );
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Return",
                "Mark this book as returned?\n\n" + selected.getBookTitle()
        );

        if (!confirmed) {
            return;
        }

        boolean success = BorrowingService.returnBook(selected);

        if (success) {
            AlertHelper.showInfo(
                    "Returned",
                    "Book returned successfully."
            );

            AppState.refreshAll();
            loadRecords();

            TransitionHelper.pulse(table);
            TransitionHelper.pulse(resultLabel);
        } else {
            AlertHelper.showError(
                    "Return Failed",
                    "Unable to return this book."
            );
        }
    }

    private void updateResultLabel() {
        if (resultLabel == null || filteredRecords == null) {
            return;
        }

        int count = filteredRecords.size();

        if (count == 0) {
            resultLabel.setText("No records available for return.");
        } else if (count == 1) {
            resultLabel.setText("1 record available for return.");
        } else {
            resultLabel.setText(count + " records available for return.");
        }
    }

    private void setSelectedRecordText(String value) {
        if (selectedRecordLabel != null) {
            selectedRecordLabel.setText(value);
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}