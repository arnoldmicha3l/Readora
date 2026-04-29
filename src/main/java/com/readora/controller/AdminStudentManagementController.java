package com.readora.controller;

import com.readora.model.Student;
import com.readora.service.AlertHelper;
import com.readora.service.AppState;
import com.readora.service.SceneNavigator;
import com.readora.service.StudentService;
import com.readora.service.TransitionHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AdminStudentManagementController {

    @FXML private TextField studentIdField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField searchField;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> statusColumn;
    @FXML private TableColumn<Student, String> actionColumn;

    @FXML private Label resultLabel;

    private FilteredList<Student> filteredStudents;
    private Student selectedStudent;

    @FXML
    public void initialize() {
        setupComboBox();
        setupTable();
        setupTooltips();
        loadStudents();
        setupAnimations();
    }

    private void setupComboBox() {
        statusComboBox.getItems().setAll("Active", "Inactive");
    }

    private void setupTable() {
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        studentTable.setPlaceholder(new Label("No students found."));

        actionColumn.setCellValueFactory(data -> new SimpleStringProperty("Actions"));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actionBox = new HBox(8, editButton, deleteButton);

            {
                actionBox.setAlignment(Pos.CENTER);

                editButton.getStyleClass().add("table-edit-button");
                deleteButton.getStyleClass().add("table-delete-button");

                editButton.setTooltip(new Tooltip("Edit this student"));
                deleteButton.setTooltip(new Tooltip("Delete this student"));

                editButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    loadStudentToForm(student);
                });

                deleteButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    deleteStudent(student);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                } else {
                    setGraphic(actionBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        studentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Student student = studentTable.getSelectionModel().getSelectedItem();

                if (student != null) {
                    loadStudentToForm(student);
                }
            }
        });
    }

    private void setupTooltips() {
        if (searchField != null) {
            searchField.setTooltip(new Tooltip("Search by student ID, name, email, or status"));
        }
    }

    private void setupAnimations() {
        TransitionHelper.softLoad(studentTable);
    }

    private void loadStudents() {
        AppState.refreshStudents();

        filteredStudents = new FilteredList<>(AppState.getStudents(), student -> true);
        studentTable.setItems(filteredStudents);

        applySearch();
        studentTable.refresh();
        updateResultLabel();
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            return;
        }

        Student student = new Student(
                studentIdField.getText().trim(),
                fullNameField.getText().trim(),
                emailField.getText().trim(),
                statusComboBox.getValue()
        );

        boolean success = StudentService.addStudent(student);

        if (success) {
            AlertHelper.showInfo("Success", "Student added successfully.");
            clearFields();
            loadStudents();
            TransitionHelper.pulse(studentTable);
        } else {
            AlertHelper.showError(
                    "Add Failed",
                    "Student ID already exists or data is invalid."
            );
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedStudent == null) {
            AlertHelper.showWarning(
                    "No Student Selected",
                    "Please select a student to update."
            );
            return;
        }

        if (!validateFields()) {
            return;
        }

        selectedStudent.setFullName(fullNameField.getText().trim());
        selectedStudent.setEmail(emailField.getText().trim());
        selectedStudent.setStatus(statusComboBox.getValue());

        boolean success = StudentService.updateStudent(selectedStudent);

        if (success) {
            AlertHelper.showInfo("Success", "Student updated successfully.");
            clearFields();
            loadStudents();
            TransitionHelper.pulse(studentTable);
        } else {
            AlertHelper.showError(
                    "Update Failed",
                    "Unable to update selected student."
            );
        }
    }

    @FXML
    private void handleDelete() {
        Student student = studentTable.getSelectionModel().getSelectedItem();

        if (student == null) {
            AlertHelper.showWarning(
                    "No Student Selected",
                    "Please select a student to delete."
            );
            return;
        }

        deleteStudent(student);
    }

    private void deleteStudent(Student student) {
        if (student == null) {
            AlertHelper.showWarning("No Student Selected", "Please select a student first.");
            return;
        }

        boolean confirmed = AlertHelper.confirm(
                "Confirm Delete",
                "Are you sure you want to delete this student?\n\n" + student.getFullName()
        );

        if (!confirmed) {
            return;
        }

        boolean success = StudentService.deleteStudent(student.getStudentId());

        if (success) {
            AlertHelper.showInfo("Deleted", "Student deleted successfully.");
            clearFields();
            loadStudents();
            TransitionHelper.pulse(studentTable);
        } else {
            AlertHelper.showError(
                    "Delete Failed",
                    "Unable to delete selected student."
            );
        }
    }

    private void loadStudentToForm(Student student) {
        if (student == null) {
            return;
        }

        selectedStudent = student;

        studentIdField.setText(student.getStudentId());
        fullNameField.setText(student.getFullName());
        emailField.setText(student.getEmail());
        statusComboBox.setValue(student.getStatus());

        studentIdField.setEditable(false);
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
        TransitionHelper.pulse(studentTable);
    }

    private void applySearch() {
        if (filteredStudents == null) {
            return;
        }

        String keyword = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        filteredStudents.setPredicate(student -> {
            if (keyword.isEmpty()) {
                return true;
            }

            return contains(student.getStudentId(), keyword)
                    || contains(student.getFullName(), keyword)
                    || contains(student.getEmail(), keyword)
                    || contains(student.getStatus(), keyword);
        });

        updateResultLabel();
    }

    private boolean validateFields() {
        if (studentIdField.getText() == null || studentIdField.getText().trim().isEmpty()
                || fullNameField.getText() == null || fullNameField.getText().trim().isEmpty()
                || statusComboBox.getValue() == null) {

            AlertHelper.showWarning(
                    "Validation Error",
                    "Student ID, full name, and status are required."
            );

            return false;
        }

        return true;
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        selectedStudent = null;

        studentIdField.clear();
        fullNameField.clear();
        emailField.clear();
        statusComboBox.setValue(null);

        studentIdField.setEditable(true);
        studentTable.getSelectionModel().clearSelection();
    }

    private void updateResultLabel() {
        if (resultLabel == null || filteredStudents == null) {
            return;
        }

        int count = filteredStudents.size();

        if (count == 0) {
            resultLabel.setText("No students found.");
        } else if (count == 1) {
            resultLabel.setText("1 student found.");
        } else {
            resultLabel.setText(count + " students found.");
        }
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
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
        loadStudents();
        TransitionHelper.pulse(studentTable);
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
        SceneNavigator.switchScene(
                event,
                "/view/AdminReportsView.fxml",
                "Readora - Reports"
        );
    }
}