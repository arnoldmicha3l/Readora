package com.readora.service;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {

    private SceneNavigator() {
    }

    public static void switchScene(ActionEvent event, Class<?> sourceClass, String fxmlPath, String title) throws IOException {
        if (!(event.getSource() instanceof Node node)) {
            throw new IllegalArgumentException("Event source is not a JavaFX Node.");
        }

        Stage stage = (Stage) node.getScene().getWindow();
        switchScene(stage, sourceClass, fxmlPath, title);
    }

    public static void switchScene(Stage stage, Class<?> sourceClass, String fxmlPath, String title) throws IOException {
        boolean wasMaximized = stage.isMaximized();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(sourceClass.getResource(fxmlPath));
        Parent root = loader.load();

        Scene newScene = new Scene(root, currentWidth, currentHeight);
        stage.setScene(newScene);
        stage.setTitle(title);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);

        Platform.runLater(() -> {
            stage.setMaximized(wasMaximized || true);
            stage.centerOnScreen();
            stage.show();
        });
    }
}