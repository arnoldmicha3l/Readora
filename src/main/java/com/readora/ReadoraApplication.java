package com.readora;

import com.readora.database.DatabaseInitializer;
import com.readora.service.AppState;
import com.readora.service.BorrowingService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReadoraApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initializeDatabase();
        AppState.initializeData();
        BorrowingService.updateOverdueRecords();

        FXMLLoader loader = new FXMLLoader(ReadoraApplication.class.getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 850);

        stage.setTitle("Readora - Login");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}