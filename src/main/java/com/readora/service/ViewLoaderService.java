package com.readora.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public final class ViewLoaderService {

    private ViewLoaderService() {}

    public static void loadInto(StackPane container, Class<?> sourceClass, String fxmlFileName) {
        if (container == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(sourceClass.getResource("/view/" + fxmlFileName));
            Parent view = loader.load();

            container.getChildren().setAll(view);
            TransitionHelper.fadeIn(view);
            TransitionHelper.slideInFromRight(view);

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Loading Error", "Unable to load " + fxmlFileName);
        }
    }
}