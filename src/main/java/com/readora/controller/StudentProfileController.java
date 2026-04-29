package com.readora.controller;

import com.readora.service.AccountSyncService;
import com.readora.service.AlertHelper;
import com.readora.service.NavigationHelper;
import com.readora.service.SceneNavigator;
import com.readora.user.AccountService;
import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import com.readora.user.ValidationService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentProfileController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField idField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField phoneField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        loadUserData();
    }

    private void loadUserData() {
        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        String[] nameParts = splitName(currentUser.getFullName());

        firstNameField.setText(nameParts[0]);
        middleNameField.setText(nameParts[1]);
        lastNameField.setText(nameParts[2]);

        idField.setText(currentUser.getStudentId() != null ? currentUser.getStudentId() : "N/A");
        idField.setEditable(false);

        ageField.setText(currentUser.getAge() != null ? String.valueOf(currentUser.getAge()) : "");
        genderComboBox.setValue(currentUser.getGender() != null && !currentUser.getGender().isEmpty() ? currentUser.getGender() : null);
        emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
        usernameField.setText(currentUser.getUsername());
        phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            AlertHelper.showError("Error", "No active session found.");
            return;
        }

        String firstName = firstNameField.getText().trim();
        String middleName = middleNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String gender = genderComboBox.getValue();
        String ageText = ageField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()) {
            AlertHelper.showWarning("Validation Error", "First name, last name, and username are required.");
            return;
        }

        if (!ValidationService.isValidEmail(email)) {
            AlertHelper.showWarning("Validation Error", "Please enter a valid email address.");
            return;
        }

        if (!ValidationService.isValidPhone(phone)) {
            AlertHelper.showWarning("Validation Error", "Please enter a valid phone number.");
            return;
        }

        if (!ValidationService.isValidAge(ageText)) {
            AlertHelper.showWarning("Validation Error", "Age must be a valid number from 1 to 120.");
            return;
        }

        if (!AccountService.updateUsername(currentUser, username)) {
            AlertHelper.showWarning("Validation Error", "Username already exists. Please choose another one.");
            return;
        }

        currentUser.setFullName(buildFullName(firstName, middleName, lastName));
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setGender(gender != null ? gender : "");

        if (!ageText.isEmpty()) {
            currentUser.setAge(Integer.parseInt(ageText));
        } else {
            currentUser.setAge(null);
        }

        AccountService.updateAccount(currentUser);
        AccountSyncService.syncStudentFromSession(currentUser);

        AlertHelper.showInfo("Success", "Profile updated successfully.");
    }

    @FXML
    protected void handleCancel(ActionEvent event) {
        loadUserData();
    }

    @FXML
    protected void handleDashboard(ActionEvent event) {
        try {
            NavigationHelper.goToDashboard(event, getClass());
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open dashboard.");
        }
    }

    @FXML
    protected void handleBrowseBooks(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/BrowseBooks.fxml", "Readora - Browse Books");
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open Browse Books.");
        }
    }

    @FXML
    protected void handleHistoryTab(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/MyHistory.fxml", "Readora - My History");
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open My History.");
        }
    }

    @FXML
    protected void handleChangePassword(ActionEvent event) {
        try {
            SceneNavigator.switchScene(event, getClass(), "/view/ChangePassword.fxml", "Readora - Change Password");
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open Change Password screen.");
        }
    }

    private String[] splitName(String fullName) {
        String[] result = new String[]{"", "", ""};

        if (fullName == null || fullName.trim().isEmpty()) {
            return result;
        }

        String[] parts = fullName.trim().split("\\s+");

        if (parts.length == 1) {
            result[0] = parts[0];
        } else if (parts.length == 2) {
            result[0] = parts[0];
            result[2] = parts[1];
        } else {
            result[0] = parts[0];
            result[2] = parts[parts.length - 1];

            StringBuilder middle = new StringBuilder();

            for (int i = 1; i < parts.length - 1; i++) {
                if (i > 1) {
                    middle.append(" ");
                }

                middle.append(parts[i]);
            }

            result[1] = middle.toString();
        }

        return result;
    }

    private String buildFullName(String first, String middle, String last) {
        StringBuilder name = new StringBuilder(first);

        if (!middle.isEmpty()) {
            name.append(" ").append(middle);
        }

        name.append(" ").append(last);

        return name.toString().trim();
    }
}