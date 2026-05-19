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

        FXMLLoader loader = new FXMLLoader(ReadoraApplication.class.getResource("/view/LoginView.fxml"));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        Scene scene = new Scene(loader.load(), width, height);

        stage.setTitle("Readora - Login");
        stage.setScene(scene);

        stage.setMinWidth(1100);
        stage.setMinHeight(650);

        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(width);
        stage.setHeight(height);

        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}