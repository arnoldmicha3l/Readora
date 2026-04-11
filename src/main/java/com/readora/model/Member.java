package com.readora.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {

    private final StringProperty memberId;
    private final StringProperty fullName;
    private final StringProperty email;
    private final StringProperty status;

    public Member(String memberId, String fullName, String email, String status) {
        this.memberId = new SimpleStringProperty(memberId);
        this.fullName = new SimpleStringProperty(fullName);
        this.email = new SimpleStringProperty(email);
        this.status = new SimpleStringProperty(status);
    }

    public String getMemberId() {
        return memberId.get();
    }

    public void setMemberId(String memberId) {
        this.memberId.set(memberId);
    }

    public StringProperty memberIdProperty() {
        return memberId;
    }

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
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
