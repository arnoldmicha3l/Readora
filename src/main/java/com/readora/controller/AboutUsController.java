package com.readora.controller;

import com.readora.user.UserAccount;
import com.readora.user.UserRole;
import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AboutUsController {

    @FXML
    private Label profileInitialsLabel;

    @FXML
    private Label profileNameLabel;

    @FXML
    private Label profileRoleLabel;

    @FXML
    private Label profileQuoteLabel;

    @FXML
    public void initialize() {
        setDeveloperProfile(
                "FC",
                "Francis Andrei L. Cabrera",
                "Developer",
                "Keep building, keep learning, and trust that every small improvement brings you closer to something great."
        );
    }

    @FXML
    private void showFrancis() {
        setDeveloperProfile(
                "FC",
                "Francis Andrei L. Cabrera",
                "Developer",
                "Keep building, keep learning, and trust that every small improvement brings you closer to something great."
        );
    }

    @FXML
    private void showNirhven() {
        setDeveloperProfile(
                "ND",
                "Nirhven Kyle C. Dialimas",
                "Developer",
                "Success grows from patience, discipline, and the courage to keep moving forward even when the work feels hard."
        );
    }

    @FXML
    private void showDave() {
        setDeveloperProfile(
                "DR",
                "Dave Laurence R. Repe",
                "Developer",
                "Do not be afraid of challenges, because every problem you solve today becomes part of your strength tomorrow."
        );
    }

    @FXML
    private void showRolando() {
        setDeveloperProfile(
                "RS",
                "Rolando Supremo Jr.",
                "Developer",
                "Great things are achieved by people who stay consistent, stay humble, and continue learning with purpose."
        );
    }

    @FXML
    private void showArnold() {
        setDeveloperProfile(
                "AT",
                "Arnold Michael P. Tabada",
                "Developer",
                "Believe in your progress, even if it feels slow. Every line of effort you make is building your future."
        );
    }

    private void setDeveloperProfile(String initials, String name, String role, String quote) {
        profileInitialsLabel.setText(initials);
        profileNameLabel.setText(name);
        profileRoleLabel.setText(role);
        profileQuoteLabel.setText(quote);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        UserAccount currentUser = SessionManager.getCurrentUser();

        try {
            if (currentUser == null) {
                SceneNavigator.switchScene(event, getClass(), "/view/LoginView.fxml", "Readora - Login");
                return;
            }

            if (currentUser.getRole() == UserRole.ADMIN) {
                SceneNavigator.switchScene(event, getClass(), "/view/AdminView.fxml", "Readora - Admin Dashboard");
            } else if (currentUser.getRole() == UserRole.LIBRARIAN) {
                SceneNavigator.switchScene(event, getClass(), "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");
            } else if (currentUser.getRole() == UserRole.STUDENT) {
                SceneNavigator.switchScene(event, getClass(), "/view/StudentView.fxml", "Readora - Student Dashboard");
            } else {
                SceneNavigator.switchScene(event, getClass(), "/view/LoginView.fxml", "Readora - Login");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}