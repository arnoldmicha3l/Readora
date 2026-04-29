package com.readora.controller;

import com.readora.model.Book;
import com.readora.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookManagementController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> idCol, titleCol, authorCol, categoryCol, statusCol;

    @FXML private TextField idField, titleField, authorField, categoryField;
    @FXML private ComboBox<String> statusBox;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusBox.getItems().addAll("Available", "Borrowed", "Reserved");

        bookTable.setItems(AppState.getBooks());
    }

    @FXML
    private void handleAdd() {
        Book book = new Book(
                idField.getText(),
                titleField.getText(),
                authorField.getText(),
                categoryField.getText(),
                statusBox.getValue()
        );

        if (BookService.addBook(book)) {
            AlertHelper.showInfo("Success", "Book added successfully");
            clearFields();
        } else {
            AlertHelper.showError("Error", "Failed to add book");
        }
    }

    @FXML
    private void handleUpdate() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        selected.setTitle(titleField.getText());
        selected.setAuthor(authorField.getText());
        selected.setCategory(categoryField.getText());
        selected.setStatus(statusBox.getValue());

        if (BookService.updateBook(selected)) {
            AlertHelper.showInfo("Updated", "Book updated");
        }
    }

    @FXML
    private void handleDelete() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        if (AlertHelper.confirm("Confirm", "Delete this book?")) {
            BookService.deleteBook(selected.getBookId());
        }
    }

    @FXML
    private void handleSelect() {
        Book book = bookTable.getSelectionModel().getSelectedItem();

        if (book == null) return;

        idField.setText(book.getBookId());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        statusBox.setValue(book.getStatus());
    }

    private void clearFields() {
        idField.clear();
        titleField.clear();
        authorField.clear();
        categoryField.clear();
        statusBox.setValue(null);
    }
}