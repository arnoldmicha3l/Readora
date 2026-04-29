package com.readora.service;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public final class MenuHelper {

    private MenuHelper() {}

    public static ContextMenu createUserMenu(
            Runnable profileAction,
            Runnable settingsAction,
            Runnable aboutAction,
            Runnable logoutAction
    ) {
        ContextMenu menu = new ContextMenu();

        MenuItem profile = new MenuItem("View Profile");
        MenuItem settings = new MenuItem("Settings");
        MenuItem about = new MenuItem("About Us");
        MenuItem logout = new MenuItem("Logout");

        profile.setOnAction(e -> profileAction.run());
        settings.setOnAction(e -> settingsAction.run());
        about.setOnAction(e -> aboutAction.run());
        logout.setOnAction(e -> logoutAction.run());

        menu.getItems().addAll(profile, settings, about, logout);
        return menu;
    }
}