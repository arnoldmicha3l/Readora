package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class BorrowRecordsController {

    @FXML
    private TableView<BorrowRecord> borrowTable;

    @FXML
    private TableColumn<BorrowRecord, String> colId;

    @FXML
    private TableColumn<BorrowRecord, String> colBook;

    @FXML
    private TableColumn<BorrowRecord, String> colMember;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colBorrow;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colDue;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colReturn;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colBorrow.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        borrowTable.setItems(AppState.getBorrowRecords());
    }

    @FXML
    private void handleUpdateStatus() {
        BorrowRecord selected = borrowTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a record first.");
            return;
        }

        if ("Returned".equalsIgnoreCase(selected.getStatus())) {
            showAlert(Alert.AlertType.INFORMATION, "Already Returned", "This book is already marked as returned.");
            return;
        }

        selected.setStatus("Returned");
        selected.setReturnDate(LocalDate.now());
        borrowTable.refresh();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Borrow record updated successfully.");
    }

    @FXML
    private void handleClose() {
        borrowTable.getSelectionModel().clearSelection();
        showAlert(Alert.AlertType.INFORMATION, "Close", "Selection cleared. Use navigation buttons to move to another page.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}