package com.readora.service;

import com.readora.user.SessionManager;
import com.readora.user.UserAccount;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavigationHelper {

    private NavigationHelper() {
    }

    public static void goToDashboard(ActionEvent event, Class<?> sourceClass) throws IOException {
        UserAccount user = SessionManager.getCurrentUser();

        if (user == null || user.getRole() == null) {
            SceneNavigator.switchScene(event, sourceClass, "/view/LoginView.fxml", "Readora - Login");
            return;
        }

        switch (user.getRole()) {
            case ADMIN -> SceneNavigator.switchScene(event, sourceClass, "/view/AdminView.fxml", "Readora - Admin Dashboard");
            case LIBRARIAN -> SceneNavigator.switchScene(event, sourceClass, "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");
            case STUDENT -> SceneNavigator.switchScene(event, sourceClass, "/view/StudentView.fxml", "Readora - Student Dashboard");
        }
    }

    public static void goToDashboard(Stage stage, Class<?> sourceClass) throws IOException {
        UserAccount user = SessionManager.getCurrentUser();

        if (user == null || user.getRole() == null) {
            SceneNavigator.switchScene(stage, sourceClass, "/view/LoginView.fxml", "Readora - Login");
            return;
        }

        switch (user.getRole()) {
            case ADMIN -> SceneNavigator.switchScene(stage, sourceClass, "/view/AdminView.fxml", "Readora - Admin Dashboard");
            case LIBRARIAN -> SceneNavigator.switchScene(stage, sourceClass, "/view/LibrarianLandingPage.fxml", "Readora - Librarian Dashboard");
            case STUDENT -> SceneNavigator.switchScene(stage, sourceClass, "/view/StudentView.fxml", "Readora - Student Dashboard");
        }
    }

    public static void goToLogin(ActionEvent event, Class<?> sourceClass) throws IOException {
        SessionManager.clearSession();
        SceneNavigator.switchScene(event, sourceClass, "/view/LoginView.fxml", "Readora - Login");
    }

    public static void goToLogin(Stage stage, Class<?> sourceClass) throws IOException {
        SessionManager.clearSession();
        SceneNavigator.switchScene(stage, sourceClass, "/view/LoginView.fxml", "Readora - Login");
    }
}