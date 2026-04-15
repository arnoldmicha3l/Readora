package com.readora.controller;

import com.readora.service.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class StudentProfileController implements Initializable {

    // Name Fields
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;

    // Attribute Fields
    @FXML private TextField idField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;

    /**
     * Initializes the controller class. This is called automatically
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate the Gender ComboBox
        ObservableList<String> genders = FXCollections.observableArrayList(
                "Male", "Female"
        );
        genderComboBox.setItems(genders);

        // Optional: Load existing user data here
        loadUserData();
    }

    private void loadUserData() {
        // This is where you would normally fetch data from a database
        // Example placeholder:
        idField.setText("2024-0001");
        idField.setEditable(false); // ID usually shouldn't be changed by the user
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Collect data from fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String gender = genderComboBox.getValue();

        // Validation Logic (Simple Example)
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            System.out.println("Error: Please fill in all required fields.");
            return;
        }

        // Logic to save to database would go here
        System.out.println("Saving profile for: " + firstName + " " + lastName);
        System.out.println("Selected Gender: " + gender);

        // Provide feedback to the user (e.g., Alert or Status Label)
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Logic to clear fields or return to the Dashboard
        System.out.println("Changes discarded.");
        // Example: Navigate back
        // NavigationUtil.navigateTo(event, "/com/readora/view/Dashboard.fxml");
    }

    // Navigation handlers (Matching the Top Bar buttons)
    @FXML
    private void handleDashboard(ActionEvent event) {
        System.out.println("Navigating to Dashboard...");
    }

    @FXML
    private void handleBrowseBooks(ActionEvent event) {
        System.out.println("Navigating to Browse Books...");
    }

    @FXML
    private void handleHistoryTab(ActionEvent event) {
        System.out.println("Navigating to History...");
    }



    @FXML private TextField phoneField;

    @FXML
    private void handleChangeUsername(ActionEvent event) {
        // Logic to make field editable or open a small popup
        usernameField.setEditable(true);
        usernameField.setStyle("-fx-background-color: white; -fx-border-color: #3b82f6;");
        usernameField.requestFocus();
        System.out.println("Username editing enabled.");
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        try {
            // This replaces the placeholder alert with actual navigation
            SceneNavigator.switchScene(
                    event,
                    getClass(),
                    "/view/ChangePassword.fxml",
                    "Readora - Change Password"
            );
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error", "Unable to open the Change Password screen.");
        }
    }

    private void showInfo(String error, String s) {
    }
}