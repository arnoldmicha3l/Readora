package com.readora;

import com.readora.database.DatabaseInitializer;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ReadoraApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.initializeDatabase();
        AppState.initializeData();
        BorrowingService.updateOverdueRecords();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        FXMLLoader loader = new FXMLLoader(
                ReadoraApplication.class.getResource("/view/LoginView.fxml")
        );

        Scene scene = new Scene(
                loader.load(),
                screenBounds.getWidth(),
                screenBounds.getHeight()
        );

        stage.setTitle("Readora - Login");
        stage.setScene(scene);

        // Lock app size to current monitor size
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        stage.setMinWidth(screenBounds.getWidth());
        stage.setMinHeight(screenBounds.getHeight());

        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());

        // Prevent resizing
        stage.setResizable(false);

        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}