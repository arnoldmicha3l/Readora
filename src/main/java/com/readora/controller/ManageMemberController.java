package com.readora.controller;

import com.readora.model.Student;
import com.readora.service.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageMemberController {

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> statusCol;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.setItems(AppState.getStudents());
    }
}