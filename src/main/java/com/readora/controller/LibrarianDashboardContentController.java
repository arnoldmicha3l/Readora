package com.readora.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LibrarianDashboardContentController {

    private LibrarianLandingPage mainController;

    // This allows the Landing Page to pass itself to this controller
    public void setMainController(LibrarianLandingPage mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void openBorrowingRecords(ActionEvent event) {
        if (mainController != null) {
            mainController.showBorrowingRecords();
        }
    }

    @FXML
    private void openReturnBook(ActionEvent event) {
        if (mainController != null) {
            mainController.showReturnBook();
        }
    }
}