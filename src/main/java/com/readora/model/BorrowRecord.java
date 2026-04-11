package com.readora.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class BorrowRecord {

    private final StringProperty recordId;
    private final StringProperty memberName;
    private final StringProperty bookTitle;
    private final ObjectProperty<LocalDate> dateBorrowed;
    private final ObjectProperty<LocalDate> dueDate;
    private final StringProperty status;

    public BorrowRecord(String recordId, String memberName, String bookTitle, LocalDate dateBorrowed, LocalDate dueDate, String status) {
        this.recordId = new SimpleStringProperty(recordId);
        this.memberName = new SimpleStringProperty(memberName);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.dateBorrowed = new SimpleObjectProperty<>(dateBorrowed);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.status = new SimpleStringProperty(status);
    }

    public String getRecordId() {
        return recordId.get();
    }

    public StringProperty recordIdProperty() {
        return recordId;
    }

    public String getMemberName() {
        return memberName.get();
    }

    public StringProperty memberNameProperty() {
        return memberName;
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed.get();
    }

    public ObjectProperty<LocalDate> dateBorrowedProperty() {
        return dateBorrowed;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
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
