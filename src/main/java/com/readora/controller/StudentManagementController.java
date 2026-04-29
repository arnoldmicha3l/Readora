package com.readora.controller;

import com.readora.model.Student;
import com.readora.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentManagementController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idCol, nameCol, emailCol, statusCol;

    @FXML private TextField idField, nameField, emailField;
    @FXML private ComboBox<String> statusBox;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusBox.getItems().addAll("Active", "Inactive");

        studentTable.setItems(AppState.getStudents());
    }

    @FXML
    private void handleAdd() {
        Student student = new Student(
                idField.getText(),
                nameField.getText(),
                emailField.getText(),
                statusBox.getValue()
        );

        if (StudentService.addStudent(student)) {
            AlertHelper.showInfo("Success", "Student added");
            clearFields();
        }
    }

    @FXML
    private void handleUpdate() {
        Student s = studentTable.getSelectionModel().getSelectedItem();
        if (s == null) return;

        s.setFullName(nameField.getText());
        s.setEmail(emailField.getText());
        s.setStatus(statusBox.getValue());

        StudentService.updateStudent(s);
    }

    @FXML
    private void handleDelete() {
        Student s = studentTable.getSelectionModel().getSelectedItem();
        if (s == null) return;

        if (AlertHelper.confirm("Delete", "Delete student?")) {
            StudentService.deleteStudent(s.getStudentId());
        }
    }

    @FXML
    private void handleSelect() {
        Student s = studentTable.getSelectionModel().getSelectedItem();
        if (s == null) return;

        idField.setText(s.getStudentId());
        nameField.setText(s.getFullName());
        emailField.setText(s.getEmail());
        statusBox.setValue(s.getStatus());
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        statusBox.setValue(null);
    }
}