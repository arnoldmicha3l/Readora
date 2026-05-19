package com.readora.controller;

import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import com.readora.service.ReportService;
import com.readora.service.SceneNavigator;
import com.readora.service.TransitionHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class AdminReportsController {

    @FXML private Label totalBooksLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label borrowedBooksLabel;
    @FXML private Label returnedBooksLabel;
    @FXML private Label overdueLabel;
    @FXML private Label reportStatusLabel;
    @FXML private Label insightLabel;

    @FXML private PieChart statusPieChart;
    @FXML private BarChart<String, Number> metricsBarChart;

    @FXML
    public void initialize() {
        loadData();
        setupAnimations();
    }

    private void loadData() {
        BorrowingService.updateOverdueRecords();
        AppState.refreshAll();

        int totalBooks = ReportService.getTotalBooks();
        int totalStudents = ReportService.getTotalStudents();
        int borrowedBooks = ReportService.getBorrowedBooks();
        int returnedRecords = ReportService.getReturnedRecords();
        int overdueRecords = ReportService.getOverdueRecords();

        setLabel(totalBooksLabel, String.valueOf(totalBooks));
        setLabel(totalStudentsLabel, String.valueOf(totalStudents));
        setLabel(borrowedBooksLabel, String.valueOf(borrowedBooks));
        setLabel(returnedBooksLabel, String.valueOf(returnedRecords));
        setLabel(overdueLabel, String.valueOf(overdueRecords));

        updatePieChart(borrowedBooks, returnedRecords, overdueRecords);
        updateBarChart(totalBooks, totalStudents, borrowedBooks, returnedRecords, overdueRecords);
        updateInsight(totalBooks, totalStudents, borrowedBooks, returnedRecords, overdueRecords);

        if (reportStatusLabel != null) {
            reportStatusLabel.setText("Reports updated successfully from the SQLite database.");
            reportStatusLabel.setTooltip(new Tooltip("Report values are based on the latest database records."));
        }
    }

    private void updatePieChart(int borrowed, int returned, int overdue) {
        if (statusPieChart == null) return;

        statusPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Borrowed", borrowed),
                new PieChart.Data("Returned", returned),
                new PieChart.Data("Overdue", overdue)
        ));
    }

    private void updateBarChart(int books, int students, int borrowed, int returned, int overdue) {
        if (metricsBarChart == null) return;

        metricsBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Books", books));
        series.getData().add(new XYChart.Data<>("Students", students));
        series.getData().add(new XYChart.Data<>("Borrowed", borrowed));
        series.getData().add(new XYChart.Data<>("Returned", returned));
        series.getData().add(new XYChart.Data<>("Overdue", overdue));

        metricsBarChart.getData().add(series);
    }

    private void updateInsight(int books, int students, int borrowed, int returned, int overdue) {
        if (insightLabel == null) return;

        if (overdue > 0) {
            insightLabel.setText("There are " + overdue + " overdue record(s). Prioritise follow-up with students to keep circulation healthy.");
        } else if (borrowed == 0) {
            insightLabel.setText("There are currently no borrowed books. Encourage students to explore the library collection.");
        } else {
            insightLabel.setText("Library activity is running smoothly. Borrowed and returned records are being tracked properly.");
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(totalBooksLabel);
        TransitionHelper.softLoad(totalStudentsLabel);
        TransitionHelper.softLoad(borrowedBooksLabel);
        TransitionHelper.softLoad(returnedBooksLabel);
        TransitionHelper.softLoad(overdueLabel);

        if (statusPieChart != null) TransitionHelper.softLoad(statusPieChart);
        if (metricsBarChart != null) TransitionHelper.softLoad(metricsBarChart);
    }

    private void pulseReports() {
        TransitionHelper.pulse(totalBooksLabel);
        TransitionHelper.pulse(totalStudentsLabel);
        TransitionHelper.pulse(borrowedBooksLabel);
        TransitionHelper.pulse(returnedBooksLabel);
        TransitionHelper.pulse(overdueLabel);

        if (statusPieChart != null) TransitionHelper.pulse(statusPieChart);
        if (metricsBarChart != null) TransitionHelper.pulse(metricsBarChart);
    }

    private void setLabel(Label label, String value) {
        if (label != null) label.setText(value);
    }

    @FXML
    private void handleRefreshReports() {
        loadData();
        pulseReports();
        AlertHelper.showInfo("Reports Updated", "Report data has been refreshed.");
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
    private void handleBorrowRecords(ActionEvent event) {
        SceneNavigator.switchScene(event, "/view/BorrowRecords.fxml", "Readora - Borrow Records");
    }

    @FXML
    private void handleReports(ActionEvent event) {
        loadData();
        pulseReports();
    }
}