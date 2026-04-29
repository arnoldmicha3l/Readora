package com.readora.service;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public final class SceneNavigator {

    private static final double DEFAULT_WIDTH = 1400;
    private static final double DEFAULT_HEIGHT = 850;

    private SceneNavigator() {
    }

    public static void switchScene(ActionEvent event, String fxmlPath) {
        switchScene(event, fxmlPath, "Readora");
    }

    public static void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            if (event == null || event.getSource() == null) {
                AlertHelper.showError("Navigation Error", "Unable to detect the current page.");
                return;
            }

            Node node = (Node) event.getSource();

            if (node.getScene() == null || node.getScene().getWindow() == null) {
                AlertHelper.showError("Navigation Error", "Unable to detect the current window.");
                return;
            }

            Stage stage = (Stage) node.getScene().getWindow();
            switchScene(stage, fxmlPath, title);

        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open the requested page.");
        }
    }

    public static void switchScene(ActionEvent event, Class<?> sourceClass, String fxmlPath, String title) throws IOException {
        if (event == null || event.getSource() == null) {
            throw new IOException("Unable to detect current event source.");
        }

        Node node = (Node) event.getSource();

        if (node.getScene() == null || node.getScene().getWindow() == null) {
            throw new IOException("Unable to detect current window.");
        }

        Stage stage = (Stage) node.getScene().getWindow();
        switchScene(stage, fxmlPath, title);
    }

    public static void switchScene(Stage stage, Class<?> sourceClass, String fxmlPath, String title) throws IOException {
        switchScene(stage, fxmlPath, title);
    }

    public static void switchSceneFromNode(Node node, String fxmlPath, String title) {
        try {
            if (node == null || node.getScene() == null || node.getScene().getWindow() == null) {
                AlertHelper.showError("Navigation Error", "Unable to detect the current window.");
                return;
            }

            Stage stage = (Stage) node.getScene().getWindow();
            switchScene(stage, fxmlPath, title);

        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Unable to open the requested page.");
        }
    }

    public static void switchScene(Stage stage, String fxmlPath, String title) throws IOException {
        if (stage == null) {
            throw new IOException("Stage is null.");
        }

        URL fxmlUrl = SceneNavigator.class.getResource(fxmlPath);

        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        double width = DEFAULT_WIDTH;
        double height = DEFAULT_HEIGHT;

        if (stage.getScene() != null) {
            width = Math.max(stage.getScene().getWidth(), DEFAULT_WIDTH);
            height = Math.max(stage.getScene().getHeight(), DEFAULT_HEIGHT);
        }

        Scene scene = new Scene(root, width, height);

        stage.setTitle(title == null || title.trim().isEmpty() ? "Readora" : title);
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();

        playTransition(root);
    }

    private static void playTransition(Parent root) {
        if (root == null) {
            return;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(180), root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(180), root);
        scale.setFromX(0.99);
        scale.setFromY(0.99);
        scale.setToX(1.0);
        scale.setToY(1.0);

        fade.play();
        scale.play();
    }

    public static void goBack(ActionEvent event) {
    }
}