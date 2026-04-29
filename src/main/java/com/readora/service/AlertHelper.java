package com.readora.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class AlertHelper {

    private AlertHelper() {
    }

    public static void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title == null ? "Confirm" : title);
        alert.setHeaderText(null);
        alert.setContentText(message == null ? "Are you sure?" : message);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title == null ? "Readora" : title);
        alert.setHeaderText(null);
        alert.setContentText(message == null ? "" : message);
        alert.showAndWait();
    }
}