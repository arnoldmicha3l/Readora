package com.readora.controller;

import com.readora.service.ViewLoaderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class LibrarianDashboardContentController {

    private StackPane contentArea;

    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }

    @FXML
    private void openSearchBook(ActionEvent event) {
        if (contentArea != null) {
            ViewLoaderService.loadInto(
                    contentArea,
                    getClass(),
                    "SearchBookView.fxml"
            );
        }
    }

    @FXML
    private void openManageStudent(ActionEvent event) {
        if (contentArea != null) {
            ViewLoaderService.loadInto(
                    contentArea,
                    getClass(),
                    "ManageMemberView.fxml"
            );
        }
    }

    @FXML
    private void openBorrowingRecords(ActionEvent event) {
        if (contentArea != null) {
            ViewLoaderService.loadInto(
                    contentArea,
                    getClass(),
                    "BorrowedBookView.fxml"
            );
        }
    }

    @FXML
    private void openReturnBook(ActionEvent event) {
        if (contentArea != null) {
            ViewLoaderService.loadInto(
                    contentArea,
                    getClass(),
                    "ReturnBookView.fxml"
            );
        }
    }
}