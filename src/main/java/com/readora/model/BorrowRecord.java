package com.readora.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class BorrowRecord {

    private final StringProperty recordId;
    private final StringProperty studentName;
    private final StringProperty bookTitle;
    private final ObjectProperty<LocalDate> borrowDate;
    private final ObjectProperty<LocalDate> dueDate;
    private final ObjectProperty<LocalDate> returnDate;
    private final StringProperty status;

    public BorrowRecord(
            String recordId,
            String studentName,
            String bookTitle,
            LocalDate borrowDate,
            LocalDate dueDate,
            LocalDate returnDate,
            String status
    ) {
        this.recordId = new SimpleStringProperty(recordId);
        this.studentName = new SimpleStringProperty(studentName);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.status = new SimpleStringProperty(status);
    }

    public String getRecordId() {
        return recordId.get();
    }

    public StringProperty recordIdProperty() {
        return recordId;
    }

    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate.get();
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate.set(borrowDate);
    }

    public ObjectProperty<LocalDate> borrowDateProperty() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate.get();
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate);
    }

    public ObjectProperty<LocalDate> returnDateProperty() {
        return returnDate;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}