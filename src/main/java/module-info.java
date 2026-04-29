module com.example.readora {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Open for FXML (controllers need reflection access)
    opens com.readora to javafx.fxml;
    opens com.readora.controller to javafx.fxml;

    // Export packages
    exports com.readora;
    exports com.readora.controller;
    exports com.readora.model;
    exports com.readora.service;
    exports com.readora.user;
    exports com.readora.database;
}