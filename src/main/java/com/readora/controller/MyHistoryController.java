package com.readora.controller;

import com.readora.model.BorrowRecord;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyHistoryController {

    @FXML private TextField searchField;

    @FXML private TableView<BorrowRecord> historyTable;
    @FXML private TableColumn<BorrowRecord, String> recordIdCol;
    @FXML private TableColumn<BorrowRecord, String> bookCol;
    @FXML private TableColumn<BorrowRecord, String> statusCol;
    @FXML private TableColumn<BorrowRecord, String> borrowDateCol;
    @FXML private TableColumn<BorrowRecord, String> dueDateCol;
    @FXML private TableColumn<BorrowRecord, String> returnDateCol;

    @FXML private Button studentMenuButton;

    private ContextMenu studentMenu;
    private FilteredList<BorrowRecord> filteredHistory;

    @FXML
    public void initialize() {
        setupColumns();
        setupStudentMenu();
        loadHistory();
    }

    private void setupColumns() {
        recordIdCol.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    private void setupStudentMenu() {
        studentMenu = new ContextMenu();

        MenuItem profileItem = new MenuItem("View Profile");
        MenuItem passwordItem = new MenuItem("Change Password");
        MenuItem aboutItem = new MenuItem("About Us");
        MenuItem logoutItem = new MenuItem("Logout");

        profileItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/StudentProfile.fxml",
                        "Readora - Student Profile"
                )
        );

        passwordItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/ChangePassword.fxml",
                        "Readora - Change Password"
                )
        );

        aboutItem.setOnAction(event ->
                SceneNavigator.switchSceneFromNode(
                        studentMenuButton,
                        "/view/AboutUsView.fxml",
                        "Readora - About Us"
                )
        );

        logoutItem.setOnAction(event -> {
            boolean confirmed = AlertHelper.confirm(
                    "Confirm Logout",
                    "Are you sure you want to logout?"
            );

            if (!confirmed) {
                return;
            }

            SessionManager.clearSession();
            SceneNavigator.switchSceneFromNode(
                    studentMenuButton,
                    "/view/LoginView.fxml",
                    "Readora - Login"
            );
        });

        studentMenu.getItems().setAll(profileItem, passwordItem, aboutItem, logoutItem);

        if (studentMenuButton != null) {
            studentMenuButton.setTooltip(new Tooltip("Open student menu"));
        }
    }

    private void loadHistory() {
        AppState.refreshBorrowRecords();

        UserAccount currentUser = SessionManager.getCurrentUser();

        filteredHistory = new FilteredList<>(AppState.getBorrowRecords(), record ->
                belongsToCurrentStudent(record, currentUser)
        );

        historyTable.setItems(filteredHistory);
        historyTable.refresh();
    }

    private boolean belongsToCurrentStudent(BorrowRecord record, UserAccount currentUser) {
        if (record == null || currentUser == null) {
            return false;
        }

        if (record.getStudentId() == null || currentUser.getStudentId() == null) {
            return false;
        }

        return record.getStudentId().equalsIgnoreCase(currentUser.getStudentId());
    }

    @FXML
    private void handleSearch() {
        applySearch();
    }

    @FXML
    private void handleClearSearch() {
        if (searchField != null) {
            searchField.clear();
        }

        applySearch();
    }

    private void applySearch() {
        if (filteredHistory == null) {
            return;
        }

        UserAccount currentUser = SessionManager.getCurrentUser();

        String keyword = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        filteredHistory.setPredicate(record -> {
            boolean belongs = belongsToCurrentStudent(record, currentUser);

            boolean matchesKeyword = keyword.isEmpty()
                    || contains(record.getRecordId(), keyword)
                    || contains(record.getBookTitle(), keyword)
                    || contains(record.getStatus(), keyword)
                    || contains(record.getBorrowDate() == null ? "" : record.getBorrowDate().toString(), keyword)
                    || contains(record.getDueDate() == null ? "" : record.getDueDate().toString(), keyword)
                    || contains(record.getReturnDate() == null ? "not returned" : record.getReturnDate().toString(), keyword);

            return belongs && matchesKeyword;
        });
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/StudentView.fxml",
                "Readora - Student Dashboard"
        );
    }

    @FXML
    private void handleBrowseBooks(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/BrowseBooks.fxml",
                "Readora - Browse Books"
        );
    }

    @FXML
    private void handleHistoryTab(ActionEvent event) {
        loadHistory();
    }

    @FXML
    private void handleStudentMenu(ActionEvent event) {
        if (studentMenuButton == null || studentMenu == null) {
            AlertHelper.showError("Menu Error", "Student menu is not available.");
            return;
        }

        if (studentMenu.isShowing()) {
            studentMenu.hide();
        } else {
            studentMenu.show(
                    studentMenuButton,
                    studentMenuButton.localToScreen(0, studentMenuButton.getHeight()).getX(),
                    studentMenuButton.localToScreen(0, studentMenuButton.getHeight()).getY()
            );
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/StudentProfile.fxml",
                "Readora - Student Profile"
        );
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/ChangePassword.fxml",
                "Readora - Change Password"
        );
    }

    @FXML
    private void handleAboutUs(ActionEvent event) {
        SceneNavigator.switchScene(
                event,
                "/view/AboutUsView.fxml",
                "Readora - About Us"
        );
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        boolean confirmed = AlertHelper.confirm(
                "Confirm Logout",
                "Are you sure you want to logout?"
        );

        if (!confirmed) {
            return;
        }

        SessionManager.clearSession();
        SceneNavigator.switchScene(
                event,
                "/view/LoginView.fxml",
                "Readora - Login"
        );
    }
}