package com.readora.service;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public final class TableHelper {

    private TableHelper() {}

    public static HBox createActionButtons(Button... buttons) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER);

        if (buttons != null) {
            box.getChildren().addAll(buttons);
        }

        return box;
    }

    public static Button createEditButton() {
        Button button = new Button("Edit");
        button.getStyleClass().add("table-edit-button");
        return button;
    }

    public static Button createDeleteButton() {
        Button button = new Button("Delete");
        button.getStyleClass().add("table-delete-button");
        return button;
    }

    public static Button createViewButton() {
        Button button = new Button("View");
        button.getStyleClass().add("table-edit-button");
        return button;
    }
}