package com.readora.controller;

import com.readora.service.SceneNavigator;
import com.readora.user.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class AboutUsController {

    @FXML
    private Label profileNameLabel;

    @FXML
    private Label profileRoleLabel;

    @FXML
    private Label profileQuoteLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    public void initialize() {
        showFrancis();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        if (SessionManager.getCurrentUser() == null) {
            SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
            return;
        }

        switch (SessionManager.getCurrentUser().getRole()) {
            case ADMIN:
                SceneNavigator.switchScene(event, "/view/AdminView.fxml", "Readora - Admin Dashboard");
                break;

            case LIBRARIAN:
                SceneNavigator.switchScene(event, "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");
                break;

            case STUDENT:
                SceneNavigator.switchScene(event, "/view/StudentView.fxml", "Readora - Student Dashboard");
                break;

            default:
                SceneNavigator.switchScene(event, "/view/LoginView.fxml", "Readora - Login");
                break;
        }
    }

    @FXML
    private void showFrancis() {
        updateDeveloperProfile(
                "Francis Andrei L. Cabrera",
                "Developer",
                "Keep building, keep learning, and trust that every small improvement brings you closer to something great.",
                "francis.png"
        );
    }

    @FXML
    private void showNirhven() {
        updateDeveloperProfile(
                "Nirhven Kyle C. Dialimas",
                "Developer",
                "Success grows from patience, discipline, and the courage to keep moving forward even when the work feels hard.",
                "kyle.png"
        );
    }

    @FXML
    private void showDave() {
        updateDeveloperProfile(
                "Dave Laurence R. Repe",
                "Developer",
                "Every challenge is a chance to improve your skills and prove that persistence creates progress.",
                "dave.png"
        );
    }

    @FXML
    private void showRolando() {
        updateDeveloperProfile(
                "Rolando Supremo Jr.",
                "Developer",
                "Stay focused on the goal, trust the process, and keep improving one step at a time.",
                "rolando.png"
        );
    }

    @FXML
    private void showArnold() {
        updateDeveloperProfile(
                "Arnold Michael P. Tabada",
                "Developer",
                "Great systems are built through patience, effort, and the determination to finish strong.",
                "arnold.png"
        );
    }

    private void updateDeveloperProfile(
            String name,
            String role,
            String quote,
            String imageFileName
    ) {
        if (profileNameLabel != null) {
            profileNameLabel.setText(name);
        }

        if (profileRoleLabel != null) {
            profileRoleLabel.setText(role);
        }

        if (profileQuoteLabel != null) {
            profileQuoteLabel.setText(quote);
        }

        if (profileImageView != null) {
            URL imageUrl = getClass().getResource("/view/" + imageFileName);

            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                profileImageView.setImage(image);
            }
        }
    }
}