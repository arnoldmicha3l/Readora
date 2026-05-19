package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import com.readora.service.NavigationHelper;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BorrowRecordsController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;

    @FXML private TableView<BorrowRecord> borrowTable;
    @FXML private TableColumn<BorrowRecord, String> colId;
    @FXML private TableColumn<BorrowRecord, String> colBook;
    @FXML private TableColumn<BorrowRecord, String> colMember;
    @FXML private TableColumn<BorrowRecord, String> colBorrow;
    @FXML private TableColumn<BorrowRecord, String> colDue;
    @FXML private TableColumn<BorrowRecord, String> colReturn;
    @FXML private TableColumn<BorrowRecord, String> colStatus;

    @FXML private Label resultLabel;
    @FXML private Label selectedRecordLabel;

    @FXML private Label borrowedCountLabel;
    @FXML private Label returnedCountLabel;
    @FXML private Label overdueCountLabel;

    @FXML private Button returnButton;
    @FXML private Button clearButton;

    private FilteredList<BorrowRecord> filteredRecords;

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        setupTooltips();
        loadRecords();
        setupAnimations();
    }

    private void setupTable() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRecordId()));
        colBook.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
        colMember.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentName()));

        colBorrow.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getBorrowDate() == null ? "" : data.getValue().getBorrowDate().toString()
        ));

        colDue.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDueDate() == null ? "" : data.getValue().getDueDate().toString()
        ));

        colReturn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getReturnDate() == null ? "Not Returned" : data.getValue().getReturnDate().toString()
        ));

        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(status.toUpperCase());

                if (status.equalsIgnoreCase("BORROWED")) {
                    setStyle("-fx-text-fill: #2f7d4f; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("RETURNED")) {
                    setStyle("-fx-text-fill: #2f5f8f; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("OVERDUE")) {
                    setStyle("-fx-text-fill: #984545; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #173a5e; -fx-font-weight: bold;");
                }
            }
        });

        borrowTable.setPlaceholder(new Label("No borrow records found."));

        borrowTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                setSelectedRecordText("No record selected.");
            } else {
                setSelectedRecordText("Selected: " + selected.getBookTitle() + " borrowed by " + selected.getStudentName());
            }
        });

        borrowTable.getStyleClass().add("book-table");
    }

    private void setupFilters() {
        if (statusFilter != null) {
            statusFilter.getItems().setAll("All", "Borrowed", "Returned", "Overdue");
            statusFilter.setValue("All");
        }
    }

    private void setupTooltips() {
        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search by record, book, student, or status"));
        }

        if (statusFilter != null) {
            statusFilter.setTooltip(new Tooltip("Filter borrow records by status"));
        }

        if (returnButton != null) {
            returnButton.setTooltip(new Tooltip("Mark selected record as returned"));
        }

        if (clearButton != null) {
            clearButton.setTooltip(new Tooltip("Clear search and filter"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(borrowTable);

        if (returnButton != null) TransitionHelper.pop(returnButton);
        if (clearButton != null) TransitionHelper.pop(clearButton);
    }

    private void loadRecords() {
        AppState.refreshBorrowRecords();

        filteredRecords = new FilteredList<>(AppState.getBorrowRecords(), record -> true);
        borrowTable.setItems(filteredRecords);

        applyFilters();
        borrowTable.refresh();

        setSelectedRecordText("No record selected.");
        updateResultLabel();
        updateStatusCards();
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
        TransitionHelper.pulse(borrowTable);
    }

    private void applyFilters() {
        if (filteredRecords == null) return;

        String keyword = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        String selectedStatus = statusFilter == null || statusFilter.getValue() == null
                ? "All"
                : statusFilter.getValue();

        filteredRecords.setPredicate(record -> {
            boolean matchesKeyword = keyword.isEmpty()
                    || contains(record.getRecordId(), keyword)
                    || contains(record.getBookTitle(), keyword)
                    || contains(record.getStudentName(), keyword)
                    || contains(record.getStatus(), keyword)
                    || contains(record.getBorrowDate() == null ? "" : record.getBorrowDate().toString(), keyword)
                    || contains(record.getDueDate() == null ? "" : record.getDueDate().toString(), keyword)
                    || contains(record.getReturnDate() == null ? "not returned" : record.getReturnDate().toString(), keyword);

            boolean matchesStatus = selectedStatus.equalsIgnoreCase("All")
                    || record.getStatus().equalsIgnoreCase(selectedStatus);

            return matchesKeyword && matchesStatus;
        });

        updateResultLabel();
        updateStatusCards();
    }

    private void updateStatusCards() {
        if (AppState.getBorrowRecords() == null) return;

        long borrowed = AppState.getBorrowRecords().stream()
                .filter(record -> "BORROWED".equalsIgnoreCase(record.getStatus()))
                .count();

        long returned = AppState.getBorrowRecords().stream()
                .filter(record -> "RETURNED".equalsIgnoreCase(record.getStatus()))
                .count();

        long overdue = AppState.getBorrowRecords().stream()
                .filter(record -> "OVERDUE".equalsIgnoreCase(record.getStatus()))
                .count();

        if (borrowedCountLabel != null) borrowedCountLabel.setText(String.valueOf(borrowed));
        if (returnedCountLabel != null) returnedCountLabel.setText(String.valueOf(returned));
        if (overdueCountLabel != null) overdueCountLabel.setText(String.valueOf(overdue));
    }

    @FXML
    private void handleUpdateStatus() {
        BorrowRecord selected = borrowTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertHelper.showWarning("No Record Selected", "Please select a borrow record first.");
            return;
        }

        if ("RETURNED".equalsIgnoreCase(selected.getStatus())) {
            AlertHelper.showInfo("Already Returned", "This book has already been returned.");
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Return",
                "Mark this book as returned?\n\n" + selected.getBookTitle()
        );

        if (!confirmed) return;

        boolean success = BorrowingService.returnBook(selected);

        if (success) {
            AlertHelper.showInfo("Success", "Borrow record updated successfully.");

            AppState.refreshAll();
            loadRecords();
            TransitionHelper.pulse(borrowTable);
            TransitionHelper.pulse(resultLabel);
        } else {
            AlertHelper.showError("Update Failed", "Unable to update the selected borrow record.");
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        try {
            NavigationHelper.goToDashboard(event, getClass());
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to return to dashboard.");
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminView.fxml", "Readora - Admin Dashboard");
    }

    @FXML
    private void handleBooks(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/BookFormView.fxml", "Readora - Book Management");
    }

    @FXML
    private void handleStudents(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminStudentManagement.fxml", "Readora - Student Management");
    }

    @FXML
    private void handleReports(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/AdminReportsView.fxml", "Readora - Reports");
    }

    private void updateResultLabel() {
        if (resultLabel == null || filteredRecords == null) return;

        int count = filteredRecords.size();

        if (count == 0) {
            resultLabel.setText("No borrow records found.");
        } else if (count == 1) {
            resultLabel.setText("1 borrow record found.");
        } else {
            resultLabel.setText(count + " borrow records found.");
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