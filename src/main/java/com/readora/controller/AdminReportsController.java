package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import com.readora.service.ReportService;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class AdminReportsController {

    @FXML private Label totalBooksLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label borrowedBooksLabel;
    @FXML private Label returnedBooksLabel;
    @FXML private Label overdueLabel;
    @FXML private Label reportStatusLabel;

    @FXML
    public void initialize() {
        loadData();
        setupAnimations();
    }

    private void loadData() {
        BorrowingService.updateOverdueRecords();
        AppState.refreshAll();

        setLabel(totalBooksLabel, String.valueOf(ReportService.getTotalBooks()));
        setLabel(totalStudentsLabel, String.valueOf(ReportService.getTotalStudents()));
        setLabel(borrowedBooksLabel, String.valueOf(ReportService.getBorrowedBooks()));
        setLabel(returnedBooksLabel, String.valueOf(ReportService.getReturnedRecords()));
        setLabel(overdueLabel, String.valueOf(ReportService.getOverdueRecords()));

        if (reportStatusLabel != null) {
            reportStatusLabel.setText("Reports updated successfully.");
            reportStatusLabel.setTooltip(new Tooltip("Report values are based on the latest database records."));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(totalBooksLabel);
        TransitionHelper.softLoad(totalStudentsLabel);
        TransitionHelper.softLoad(borrowedBooksLabel);
        TransitionHelper.softLoad(returnedBooksLabel);
        TransitionHelper.softLoad(overdueLabel);
    }

    private void pulseReports() {
        TransitionHelper.pulse(totalBooksLabel);
        TransitionHelper.pulse(totalStudentsLabel);
        TransitionHelper.pulse(borrowedBooksLabel);
        TransitionHelper.pulse(returnedBooksLabel);
        TransitionHelper.pulse(overdueLabel);
    }

    private void setLabel(Label label, String value) {
        if (label != null) {
            label.setText(value);
        }
    }

    @FXML
    private void handleRefreshReports() {
        loadData();
        pulseReports();
        AlertHelper.showInfo("Reports Updated", "Report data has been refreshed.");
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AdminView.fxml",
                "Readora - Admin Dashboard"
        );
    }

    @FXML
    private void handleBooks(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/BookFormView.fxml",
                "Readora - Book Management"
        );
    }

    @FXML
    private void handleStudents(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AdminStudentManagement.fxml",
                "Readora - Student Management"
        );
    }

    @FXML
    private void handleBorrowRecords(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/BorrowRecords.fxml",
                "Readora - Borrow Records"
        );
    }

    @FXML
    private void handleReports(ActionEvent event) {
        loadData();
        pulseReports();
    }
}